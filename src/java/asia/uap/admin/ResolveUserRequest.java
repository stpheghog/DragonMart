/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.beans.Admin;
import asia.uap.sql.RequestIO;
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
 * @author blonyagoncillo
 */
@WebServlet(name = "ResolveUserRequest", urlPatterns = {"/admin/do.resolveuserrequest"})
public class ResolveUserRequest extends HttpServlet {

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
        String error ="";
        
        // Get parameters from the form
        String requestIdParam = request.getParameter("requestId");
        String userIdParam = request.getParameter("userId");
        String action = request.getParameter("action");
        
        // Check if any of the required parameters are null or empty
        if (requestIdParam == null || requestIdParam.isEmpty() ||
            userIdParam == null || userIdParam.isEmpty() ||
            action == null || action.isEmpty()) {
            
            
            request.setAttribute("error", "Missing Parameters");
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/admin/error.jsp");
            rd.forward(request, response);
            return;
        }
        
        int requestId=0;
        int userId=0;
        try{
            requestId = Integer.parseInt(requestIdParam);
            
        }catch(NumberFormatException ex){
            ex.printStackTrace();

            error += "Error with parsing request id<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";
        }    
        
        try{
            userId = Integer.parseInt(userIdParam);
            
        }catch(NumberFormatException ex){
            ex.printStackTrace();

            error += "Error with parsing user id<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";
        }    
        RequestIO reqIO = new RequestIO();
        
        
        if(error.isEmpty()){
            try {
                // Handle the approval or denial of the delete user request
                if ("approve".equals(action)) {
                    // Approve the delete user request
                    reqIO.approveDeleteRequest(requestId, userId);
                } else if ("deny".equals(action)) {
                    // Deny the delete user request
                    reqIO.denyDeleteRequest(requestId);
                } else {
                    // Invalid action provided
                    error+="Invalid action<br>";
                    uri = "/WEB-INF/jsp/admin/error.jsp";
                }

            } catch (SQLException e) {
                e.printStackTrace();
                error += "Error with getting approving or denying delete request<br>";
                uri = "/WEB-INF/jsp/admin/error.jsp";

            }
        }
        
        request.setAttribute("SuccessMessage", "Request "+action+"d successfully");
        
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
