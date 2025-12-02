/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.beans.Admin;

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
 * @author blonyagoncillo
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
@WebServlet(name = "AdminSaveProfile", urlPatterns = {"/admin/do.saveprofile"})
public class AdminSaveProfile extends HttpServlet {

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
        
        String uri = "/WEB-INF/jsp/admin/homepage.jsp";
        String error = "";

        UserIO dbIO = new UserIO();

        User userToEdit = new User();

        // Getting user ID from the request
        String userIDparam = request.getParameter("id");
        if (userIDparam == null) {
            error += "USER ID NOT PROVIDED<br>";

            uri = "/WEB-INF/jsp/admin/error.jsp";

        }

        int userId = 0;
        try {
            userId = Integer.parseInt(userIDparam);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            error += "USER ID NOT PARSED<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";

        }

        // Retrieving user data from the form
        String editedUsername = request.getParameter("username");
        String editedEmail = request.getParameter("email");
        String editedFirstname = request.getParameter("firstname");
        String editedLastname = request.getParameter("lastname");
        String editedContact = request.getParameter("contact");
        String editedAbtme = request.getParameter("aboutme");

        // value validation
        if (editedUsername == null || editedUsername.isEmpty()) {
            error += "Invalid username!<br>";
        } else if (editedEmail == null || editedEmail.isEmpty()) {
            error += "Invalid email!<br>";
        } else if (editedFirstname == null || editedFirstname.isEmpty()) {
            error += "Invalid first name!<br>";
        } else if (editedLastname == null || editedLastname.isEmpty()) {
            error += "Invalid last name!<br>";
        } else if (editedContact == null || editedContact.isEmpty()) {
            error += "Invalid contact!<br>";
        }

        // if there is an error, redirect back to edit profile
        if (!error.isEmpty()) {
            request.setAttribute("username", editedUsername);
            request.setAttribute("email", editedEmail);
            request.setAttribute("firstname", editedFirstname);
            request.setAttribute("lastname", editedLastname);
            request.setAttribute("contact", editedContact);
            request.setAttribute("aboutme", editedAbtme);
            error = "Please fill in the missing fields.";
            request.setAttribute("errmsg", error);
            uri = "/WEB-INF/jsp/admin/editprofile.jsp"; // Redirect back to the edit profile page
        } else {
            // Format contact (phone or URL)
            String formattedContact = editedContact.startsWith("http://") || editedContact.startsWith("https://") ?
                    editedContact : (editedContact.matches("^[+\\d\\s\\-()]*$") ? "tel:" + editedContact : "https://" + editedContact);

            userToEdit.setUsername(editedUsername);
            userToEdit.setEmail(editedEmail);
            userToEdit.setFirstname(editedFirstname);
            userToEdit.setLastname(editedLastname);
            userToEdit.setContact(formattedContact);
            userToEdit.setAboutme(editedAbtme);

            // update user profile in the db
            try {
                dbIO.updateUser(userId, userToEdit);
                request.setAttribute("SuccessMessage", "Updated user profile successfully");

            } catch (SQLException ex) {
                ex.printStackTrace();

                error += "Error with updating user<br>";
                uri = "/WEB-INF/jsp/admin/error.jsp"; 
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
