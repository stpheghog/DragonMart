/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.beans.Admin;
import asia.uap.beans.DeleteListingRequest;
import asia.uap.beans.Listing;
import asia.uap.sql.ListingIO;
import asia.uap.sql.RequestIO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
@WebServlet(name = "AdminRemoveListing", urlPatterns = {"/admin/do.removelisting"})
public class AdminRemoveListing extends HttpServlet {

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
                HttpSession session = request.getSession();

        ListingIO listingIO = new ListingIO();
        
        String uri = "/WEB-INF/jsp/admin/homepage.jsp";
        String error = "";
        Admin loggedin = (Admin) session.getAttribute("admin");
        ArrayList<Listing> listings=null;
       
        try{
           listings = listingIO.getAllListings();
        }catch(SQLException ex){
            ex.printStackTrace();
            error += "Error with getting all listings<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";
        }
        
        //checkbox values
        String[] listingsToRemove = request.getParameterValues("removeListing");

        if (listingsToRemove == null || listingsToRemove.length <= 0) {
            request.setAttribute("ownerListings", listings);
            
            request.setAttribute("error", "No listings selected");
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/admin/viewlistings.jsp");
            rd.forward(request, response);
            return;
        }
        
        ArrayList<String> listingIDs = new ArrayList<>();
        
        listingIDs.addAll(Arrays.asList(listingsToRemove));
        
        RequestIO reqIO = new RequestIO();
        
        DeleteListingRequest listingreq = new DeleteListingRequest();
        
        listingreq.setListingIds(listingIDs);
        listingreq.setAdminId(loggedin.getAdmin_id());
        
        if(error.isEmpty()){
            try{
                reqIO.addDeleteListingRequest(listingreq);
            }catch(SQLException ex){
                ex.printStackTrace();
                request.setAttribute("error", "cannot add delete listing request");
                uri = "/WEB-INF/jsp/admin/error.jsp";
            }
        }
        
        request.setAttribute("ownerListings", listings);
  
        request.setAttribute("error", error);
        request.setAttribute("SuccessMessage", "Added delete listing request");
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
