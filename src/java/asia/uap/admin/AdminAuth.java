/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.authreg.Encryption;
import asia.uap.beans.Admin;
import asia.uap.sql.AdminIO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
@WebServlet(name = "AdminAuth", urlPatterns = {"/admin/do.auth"})
public class AdminAuth extends HttpServlet {

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

        String uri = "../WEB-INF/jsp/admin/homepage.jsp";
        AdminIO dbIO = new AdminIO();
        Admin currentAdmin = null;
        HttpSession session = request.getSession();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String error = "";

        if (session.getAttribute("currentAdmin") == null) {

            // Check if email or password is empty
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {

                error += "Missing email or password";
                uri = "login.jsp";
            } else {

                ArrayList<Admin> adminList = null;
                try {
                    adminList = dbIO.getAllAdmins(); // Retrieve all admins from the database
                } catch (SQLException e) {
                    e.printStackTrace();

                    error += "Unable to get admins from the database.<br>";
                    uri = "login.jsp";
                }

                if (adminList == null || adminList.isEmpty()) {
                    error += "No admins found in the database<br>";
                    uri = "login.jsp";

                } else {

                    Encryption pass = new Encryption();

                    try {
                        currentAdmin = dbIO.authenticateAdmin(email, pass.encrypt(password));

                        if (currentAdmin != null) {
                            session.setAttribute("admin", currentAdmin);

                        } else {

                            session.setAttribute("admin", null);
                            error += "Wrong email or password<br>";
                            uri = "login.jsp";
                        }
                    } catch (SQLException ex) {

                        error += "Admin database error<br>";
                        uri = "login.jsp";
                    }

                }
            }
        }

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
