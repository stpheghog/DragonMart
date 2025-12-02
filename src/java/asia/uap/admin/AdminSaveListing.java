/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.admin;

import asia.uap.beans.Admin;
import asia.uap.beans.Listing;
import asia.uap.sql.ListingIO;
import asia.uap.sql.RequestIO;
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
@WebServlet(name = "AdminSaveListing", urlPatterns = {"/admin/do.savelisting"})
public class AdminSaveListing extends HttpServlet {

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
        HttpSession session = request.getSession(); //gets or creates session
        // value validation

        String uri = "/WEB-INF/jsp/admin/do.homepage";
        String error ="";

        ListingIO listingIO = new ListingIO();
        RequestIO reqIO = new RequestIO();

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.sendRedirect("do.listingview?error=NO_LISTING_ID");
            return;
        }

        int parsedID = 0;

        try {
            parsedID = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            parsedID = 0;
            ex.printStackTrace();

            error += "CANNOT PARSE ID<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";
        }
        
        Listing listingToEdit = null;
        try {
            listingToEdit = listingIO.getListingByID(parsedID);
        } catch (SQLException e) {
            e.printStackTrace();
            error += "CANNOT GET LISTING BY ID<br>";
            uri = "/WEB-INF/jsp/admin/error.jsp";
        }
        
        if (listingToEdit == null) {
            response.sendRedirect("do.homepage?error=LISTING%20NOT%20FOUND");
            return;
        }


        //new title
        String editedTitle = request.getParameter("title");
        if (editedTitle == null || editedTitle.isEmpty()) {
            response.sendRedirect("do.homepage?error=INVALID%20TITLE");
            return;
        }

        //desc
        String desc = request.getParameter("desc");
        if (desc == null || desc.isEmpty()) {
            response.sendRedirect("do.homepage?error=INVALID%20DESCRIPTION");
            return;
        }

        listingToEdit.setDesc(desc); // Update description
        listingToEdit.setTitle(editedTitle); // update title
        
        if(error.isEmpty()){
            try{
                reqIO.updateListing(listingToEdit);
                request.setAttribute("SuccessMessage", "Updated Listing");
            }catch(SQLException ex){
                ex.printStackTrace();

                error += "Error with editing listing in db<br>";
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
