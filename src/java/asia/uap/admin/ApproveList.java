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
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author blonyagoncillo
 */
@WebServlet(name = "ApproveList", urlPatterns = {"/admin/do.approvelist"})
public class ApproveList extends HttpServlet {

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
        
        String uri = "/WEB-INF/jsp/admin/approvelist.jsp";
        String error ="";
        
        UserIO dbIO = new UserIO();
        
        String searchQuery = request.getParameter("searchQuery");
        ArrayList<User> userList = new ArrayList<>();

        
        try {
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                // Perform search
                userList = dbIO.searchUnapprovedUsersByUsername(searchQuery.trim());
            } else {
                // Retrieve all unapproved users if no search query
                userList = dbIO.getAllUnapprovedUsers();
            }
        } catch (SQLException e) {
            e.printStackTrace();

            error="cannot get unapproved users from database<br>";
            uri="/WEB-INF/jsp/admin/error.jsp";
        }
        
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("userList", userList);
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
