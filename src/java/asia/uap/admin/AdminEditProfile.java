/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.beans.User;
import asia.uap.sql.UserIO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author blonyagoncillo
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
@WebServlet(name = "AdminEditProfile", urlPatterns = {"/admin/do.editprofile"})
public class AdminEditProfile extends HttpServlet {

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

        String uri = "/WEB-INF/jsp/admin/editprofile.jsp";
        String error = "";

        String userIdParam = request.getParameter("userId");
        if (userIdParam == null) {
            error += "NO USER ID FOUND<br>";
            request.setAttribute("error", error);
            uri = "/WEB-INF/jsp/admin/error.jsp";
            RequestDispatcher rd = request.getRequestDispatcher(uri);
            rd.forward(request, response);
            return;
        }

        int userId = 0;

        try {
            userId = Integer.parseInt(userIdParam);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            error += "CANNOT PARSE INT<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";
        }

        User currentUser = null; // To be fetched from the database
        try {
            // Fetch the user from the database using userId 
            UserIO userDAO = new UserIO();
            currentUser = userDAO.getUserByID(userId);

            if (currentUser == null) {
                error += "USER NOT FOUND<br>";
                uri = "/WEB-INF/jsp/admin/error.jsp";
                request.setAttribute("error", error);
                RequestDispatcher rd = request.getRequestDispatcher(uri);
                rd.forward(request, response);
                return;

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            error += "DATABASE ERROR FETCHING USER<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";
        }

        request.setAttribute("userId", userId);
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("error", error);
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
