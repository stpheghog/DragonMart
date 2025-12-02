package asia.uap.listing;

import asia.uap.beans.User;
import asia.uap.sql.ListingIO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "RemoveComment", urlPatterns = {"/user/do.removecomment"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class RemoveComment extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // fixed 1:07pm

        //get params / attributes
        HttpSession session = request.getSession();
        String listingID = request.getParameter("listingid");
        String commentID = request.getParameter("id");
        ListingIO dbIO = new ListingIO();
        request.setAttribute("type", "Remove Comment");
        String uri = "/WEB-INF/jsp/error.jsp";

        User loggedin = (User) session.getAttribute("loggedin");

        if (loggedin == null || listingID == null || listingID.isEmpty()) {
            if (loggedin == null) {
                response.sendRedirect("../do.login?errmsg=You are not logged in!");
                return;
            }
            if (listingID == null || listingID.isEmpty()) {
                uri = "/WEB-INF/jsp/error.jsp";
                request.setAttribute("finalErr", "Listing Does Not Exist!");
                request.setAttribute("backLink", "../do.shop");
                RequestDispatcher rd = request.getRequestDispatcher(uri);
                rd.forward(request, response);
            }
        } else {

            int parsedID = 0;
            boolean commentRemoved = false;

            try {
                parsedID = Integer.parseInt(commentID);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                log("Error with parsing ID");
            }

            try {
                dbIO.archiveCommentByID(parsedID);
                commentRemoved = true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                response.sendRedirect("../do.itemview?id = " + listingID + "errmsg=Comment does not exist!");
                return;
            }

            if (commentRemoved) {
                response.sendRedirect("../do.itemview?id=" + listingID);
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
