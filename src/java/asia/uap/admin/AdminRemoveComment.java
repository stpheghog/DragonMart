/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.beans.Admin;
import asia.uap.sql.ListingIO;
import asia.uap.sql.RequestIO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author blonyagoncillo
 */
@WebServlet(name = "AdminRemoveComment", urlPatterns = {"/admin/do.removecomment"})
public class AdminRemoveComment extends HttpServlet {

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
        // fixed 1:07pm

        String uri = "/WEB-INF/jsp/admin/do.homepage";
        String error = "";
        //get params / attributes
        HttpSession session = request.getSession();
        String listingID = request.getParameter("listingid");
        String commentID = request.getParameter("id");

        Admin loggedin = (Admin) session.getAttribute("admin");

        if (loggedin == null || listingID == null || listingID.isEmpty()) {
            if (loggedin == null) {
                error += "Not logged in<br>";
                uri = "/WEB-INF/jsp/admin/login.jsp";
                
            }
            if (listingID == null || listingID.isEmpty()) {

                error += "Listing does not exist!<br>";
                uri = "/WEB-INF/jsp/admin/homepage.jsp";
            }
        } else {

            int parsedID = 0;

            try {
                parsedID = Integer.parseInt(commentID);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();

                error += "Error with parsing ID<br>";
                uri = "/WEB-INF/jsp/admin/error.jsp";
            }

            RequestIO reqIO = new RequestIO();

            if(error.isEmpty()){
                try {
                    reqIO.addCommentDeleteRequest(parsedID, loggedin.getAdmin_id());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    error += "Error with adding comment delete request<br>";
                    uri = "/WEB-INF/jsp/admin/error.jsp";
                }
            }

        }
        request.setAttribute("error", error);
        request.setAttribute("SuccessMessage", "Added delete comment request");
        RequestDispatcher rd = request.getRequestDispatcher(uri);
        rd.forward(request, response);

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
