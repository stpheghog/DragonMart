/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.listing;

import asia.uap.beans.Listing;
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

/**
 *
 * @author top1g
 */
@WebServlet(name = "SaveComment", urlPatterns = {"/user/do.savecomment"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class SaveComment extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Boolean proceed = false;
        Listing listingWithComment = null;
        ListingIO listingIO = new ListingIO();
        request.setAttribute("backLink", "../do.shop");
        request.setAttribute("type", "Comment");
        String uri = "/WEB-INF/jsp/error.jsp";
        String finalErr = ""; //final string of errors
        RequestDispatcher rd = request.getRequestDispatcher(uri);

        User loggedin = (User) session.getAttribute("loggedin");
        if (loggedin == null) {
            session.setAttribute("loggedin", null);
        }

        if (loggedin == null) {
            proceed = false;
        }

        if (proceed == false) {

            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                finalErr = "Listing Unavailable!<br>";
                request.setAttribute("finalErr", finalErr);
                rd.forward(request, response);
            }

            int parsedID = 0;
            try {
                parsedID = Integer.parseInt(id); // Convert the string to an integer
            } catch (NumberFormatException e) {

                throw e;
            }

            try {
                listingWithComment = listingIO.getListingByID(parsedID);
            } catch (SQLException ex) {
                ex.printStackTrace();
                log("Error with getting listing");
            }

            // Get parameters from the request
            String comment = request.getParameter("comment");
            if (comment == null || comment.trim().isEmpty()) {
                finalErr = "Comment Must Have Content!<br>";
                request.setAttribute("finalErr", finalErr);
                rd.forward(request, response);
            } else {

                if (listingWithComment != null && loggedin != null) {
                    // Create and add the comment

                    try {
                        listingIO.addComment(listingWithComment.getId(), comment, loggedin.getId());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        log("Error with adding comment");
                    }

                } else {
                    finalErr = "Listing Unavailable!<br>";
                    request.setAttribute("finalErr", finalErr);
                    rd.forward(request, response);
                }

                response.sendRedirect("../do.itemview?id=" + listingWithComment.getId());
            }
        } else {
            response.sendRedirect("do.login?error= Sorry! You must be logged in to comment!");
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
