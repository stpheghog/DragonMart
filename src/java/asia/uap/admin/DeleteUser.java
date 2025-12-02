/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.beans.Admin;
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
@WebServlet(name = "DeleteUser", urlPatterns = {"/admin/do.deleteuser"})
public class DeleteUser extends HttpServlet {

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
        
        HttpSession session = request.getSession(); // Get or create session
        Admin loggedin = (Admin) session.getAttribute("admin");
        
        String id = request.getParameter("userId");
        if(id==null || id.isEmpty()){
             response.sendRedirect("do.homepage?error=NO%20USER%20ID");
             return;
        }
        
        int parsedID=0;
        try{
            parsedID = Integer.parseInt(id);
        }catch(NumberFormatException ex){
            parsedID = 0;
            ex.printStackTrace();
            error+="Cannot parse id<br>";
            uri="/WEB-INF/jsp/admin/error.jsp";
        }
       
        RequestIO requestIO = new RequestIO();
        
        
        try{
            requestIO.addDeleteRequest(parsedID, loggedin.getAdmin_id());
            
        }catch(SQLException ex){
            ex.printStackTrace();
            error+="Cannot add delete request<br>";
            uri="/WEB-INF/jsp/admin/error.jsp";
        }
        
        request.setAttribute("error", error);
        request.setAttribute("SuccessMessage", "ADDED DELETE REQUEST");
        
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
