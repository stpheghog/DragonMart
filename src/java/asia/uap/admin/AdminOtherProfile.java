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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stephanie
 */
@WebServlet(name = "AdminOtherProfile", urlPatterns = {"/admin/do.adminother"})
public class AdminOtherProfile extends HttpServlet {

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
        
        String uri = "/WEB-INF/jsp/admin/otherprofile.jsp";
        
        UserIO dbIO = new UserIO();

        String errmsg = "";
        boolean proceed = true;
        String id = request.getParameter("id");
        User user = new User();

        if (id == null || id.isEmpty()) {
            proceed = false;
            errmsg += "No user id provided<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";
        }

        if (proceed == true) {
            boolean userExists = false;
            int parsedID = 0;

            try {
                parsedID = Integer.parseInt(id); // Convert the string to an integer
            } catch (NumberFormatException ex) {
                parsedID = 0;
                ex.printStackTrace();
                errmsg += "Unable to parse id<br>";
                    uri = "/WEB-INF/jsp/admin/error.jsp";
            }


            try {
                if (dbIO.userExists(parsedID)) {
                    userExists = true;
                } else{
                    errmsg += "User was not found<br>";
                    uri = "/WEB-INF/jsp/admin/error.jsp";
                }
            } catch (SQLException e) {
                e.printStackTrace();

                errmsg += "Error with checking if user is in database<br>";
                uri = "/WEB-INF/jsp/admin/error.jsp";
            }

            if(userExists){
                try {
                    user = dbIO.getUserByID(parsedID);
                } catch (SQLException e) {
                    e.printStackTrace();

                    errmsg += "Error with getting user based on ID from database<br>";
                    uri = "/WEB-INF/jsp/admin/error.jsp";
                }
            }
        }

        request.setAttribute("user", user);
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
