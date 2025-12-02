/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.listing;

import asia.uap.beans.Listing;
import asia.uap.beans.User;
import asia.uap.sql.ListingIO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
@WebServlet(name = "EditListing", urlPatterns = {"/user/do.editlisting"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class EditListing extends HttpServlet {

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

        int parsedID = 0;
        HttpSession session = request.getSession(); // Get or create session
        User loggedin = (User) session.getAttribute("loggedin");
        request.setAttribute("type", "Edit Listing");
        String uri = "/WEB-INF/jsp/error.jsp";
        String finalErr = "Listing Does Not Exist!<br>";
        request.setAttribute("finalErr", finalErr);
        request.setAttribute("backLink", "../user/do.viewlisting");
        RequestDispatcher rd = request.getRequestDispatcher(uri);
        ListingIO listingIO = new ListingIO();

        if (loggedin == null) {
            session.setAttribute("loggedin", null);
            response.sendRedirect("login.jsp?ErrorMessage=USER%20NOT%20LOGGED%20IN");
            return;
            //loggedin = false;
        }

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            rd.forward(request, response);
            return;
        }
        try {
            parsedID = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            log("Error with parsing ID");
        }

        Listing listingToEdit = null;

        try {
            listingToEdit = listingIO.getListingByID(parsedID);
        } catch (SQLException e) {
            e.printStackTrace();
            log("UNABLE TO GET ALL Listings" + e);
        }

        if (listingToEdit == null) {
            rd.forward(request, response);
            return;
        }

        if (listingToEdit.getTags() == null) {
            listingToEdit.setTags("");
        }
        
        if (!listingToEdit.getTags().isEmpty()) {
            String[] tagsArray = listingToEdit.getTags().split(",");
            ArrayList<String> tagsList = new ArrayList<>(Arrays.asList(tagsArray));
            request.setAttribute("tags", tagsList);
        }
        String[] suggestedTags = {"Fiction", "Non-fiction", "Fantasy", "Science Fiction", "Mystery", "Biography", "Historical"};

        request.setAttribute("listing", listingToEdit);

        request.setAttribute("genre", suggestedTags);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/listing/editlisting.jsp");
        dispatcher.forward(request, response);
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
