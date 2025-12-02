/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.beans.User;
import asia.uap.sql.AdminIO;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author blonyagoncillo
 */
@WebServlet(name = "Approve", urlPatterns = {"/admin/do.approve"})
public class Approve extends HttpServlet {

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
        
        String uri = "/WEB-INF/jsp/admin/approve.jsp";
        
        AdminIO adminIO = new AdminIO();
        UserIO dbIO = new UserIO();

        String action = request.getParameter("action");
        ArrayList<User> userList = new ArrayList<>();
        ArrayList<User> approveUserList = new ArrayList<>();
        String[] ids = request.getParameterValues("approveUserId");
        ArrayList<Integer> userIDs = new ArrayList<>();

        String error = "";



        if (ids == null || ids.length < 0) { //if no users in general, or if there is not one to accept

            error += "Error: No Account Selected To " + action+"<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";
        } else {

            try {
                userList = dbIO.getAllUnapprovedUsers();
            } catch (SQLException e) {
                e.printStackTrace();

                error+="Cannot get users<br>";
                uri = "/WEB-INF/jsp/admin/error.jsp";
            }

            for (String id : ids) {
                for (User user : userList) {
                    if (String.valueOf(user.getId()).equals(id)) {
                        try {
                            // Collect user IDs to process in batch
                            userIDs.add(user.getId());
                        } catch (NumberFormatException e) {
                            
                            error+="Cannot add ID because of its data type<br>";
                            uri = "/WEB-INF/jsp/admin/error.jsp";
                        }
                    }
                }
            }

            if(error.isEmpty()){
           
                try { //updates user to accepted
                    approveUserList = dbIO.getUsersByIDs(userIDs);

                } catch (SQLException e) {
                    e.printStackTrace();
   
                    error+="UNABLE TO GET ALL USERS FROM DB<br>";
                    uri = "/WEB-INF/jsp/admin/error.jsp";
                }

                try {
                    if ("Approve".equals(action)) {
                        adminIO.approveUsersByIDs(userIDs); // Batch approve
                        request.setAttribute("action", action);
                    } else if ("Deny".equals(action)) {
                        adminIO.archiveUsersByIDs(userIDs); // Batch archive
                        request.setAttribute("action", action);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    error = "Cannot " + action + " users in batch.<br>";
  
                    uri = "/WEB-INF/jsp/admin/error.jsp";
                }
            }
        }
        
        request.setAttribute("approveUserList", approveUserList);
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
