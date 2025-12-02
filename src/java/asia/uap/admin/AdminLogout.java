/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.beans.Admin;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stephanie
 */
@WebServlet(name = "AdminLogout", urlPatterns = {"/admin/do.logout"})
public class AdminLogout extends HttpServlet {

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
        // fixed 1pm
        
        
        String uri = "login.jsp";
        String message ="";
                // Get the current session
        HttpSession session = request.getSession();

        // Get the logged-in admin from session
        Admin loggedinAdmin = (Admin) session.getAttribute("admin");

        String errmsg = "";

        if (loggedinAdmin == null) {
            errmsg = "NO ACCOUNT CURRENTLY LOGGED IN<br>";
            uri= "/WEB-INF/jsp/admin/error.jsp";

        } else {
            // Invalidate session if logged in
            session.invalidate(); // Completely invalidate the session
            message="Successfully logged out";
        }

        // Set headers to prevent caching of the logout page
        response.setHeader("Cache-Control", "no-store"); // Prevent caching
        response.setHeader("Pragma", "no-cache"); // Backward compatibility
        response.setDateHeader("Expires", 0); // Expire immediately
        
        request.setAttribute("SuccessMessage", message);
        request.setAttribute("error", errmsg);
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
