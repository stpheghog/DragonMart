/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.user;

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
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stephanie
 */
@WebServlet(name = "OtherProfile", urlPatterns = {"/user/do.otherprofile"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class OtherProfile extends HttpServlet {

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

        UserIO dbIO = new UserIO();
        HttpSession session = request.getSession(); // Get or create session
        String errmsg = "";
        boolean proceed = true;
        String id = request.getParameter("id");
        User user = new User();
        RequestDispatcher rd = null;
        String uri = "/WEB-INF/jsp/user/otherprofile.jsp";

        User loggedin = (User) session.getAttribute("loggedin");

        if (loggedin == null) {
            session.setAttribute("loggedin", null);
        }

        if (loggedin == null) {
            proceed = false;
            errmsg += "You must be logged in to view profiles<br>";
        }

        if (id == null || id.isEmpty()) {
            proceed = false;
            errmsg += "User was not found<br>";
        } else {
            boolean userExists = false;
            int parsedID = 0;

            try {
                parsedID = Integer.parseInt(id); // Convert the string to an integer
            } catch (NumberFormatException e) {
                // Handle the case where the string cannot be parsed as an integer needs !!!!
            }

            try {
                if (dbIO.userExists(parsedID)) {
                    userExists = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                log("UNABLE TO CHECK IF USER EXISTS");
            }

            try {
                user = dbIO.getUserByID(parsedID);
            } catch (SQLException e) {
                e.printStackTrace();
                log("UNABLE TO GET USER BY ID");
            }

            if (userExists == false) {
                proceed = false;
                errmsg = "User was not found<br>";
            }
        }

        if (proceed == true) {

            request.setAttribute("user", user);
            rd = request.getRequestDispatcher(uri);
            rd.forward(request, response);
        } else {
            uri = "/WEB-INF/jsp/error.jsp";
            request.setAttribute("type", "View Other Profile");
            request.setAttribute("backLink", "../do.shop");        
            request.setAttribute("finalErr", errmsg);
            rd = request.getRequestDispatcher(uri);
            rd.forward(request, response);
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
