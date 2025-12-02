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
@WebServlet(name = "SaveStock", urlPatterns = {"/user/do.savestock"})
public class SaveStock extends HttpServlet {

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

        String stockStr = request.getParameter("stock");
        String id = request.getParameter("id");
        HttpSession session = request.getSession(); //gets or creates session
        // value validation
        request.setAttribute("type", "Save Stock");
        String uri = "/WEB-INF/jsp/success.jsp";
        String finalErr = ""; //final string of errors

        ListingIO listingIO = new ListingIO();

        //checks for and gets logged in user object
        User loggedin = (User) session.getAttribute("loggedin");

        if (loggedin == null) {
            session.setAttribute("loggedin", null);

            response.sendRedirect("../do.login?errmsg=USER%20NOT%20LOGGED%20IN");
            return;
        }

        if (id == null || id.isEmpty()) {
            finalErr = "Listing not Found!<br>"; //final string of errors
            uri = "/WEB-INF/jsp/error.jsp";
        }

        int parsedID = 0;

        try {
            parsedID = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            log("Error with parsing ID");
        }

        Listing listingToEdit = new Listing();

        try {
            listingToEdit = listingIO.getListingByID(parsedID);
        } catch (SQLException e) {
            e.printStackTrace();
            log("UNABLE TO GET ALL Listings");
        }

        if (listingToEdit == null) {
            finalErr = "Listing not Found!<br>"; //final string of errors
            uri = "/WEB-INF/jsp/error.jsp";
        }

        int stock = 0;
        
        if (stockStr == null || stockStr.isEmpty()) {
            finalErr = "Invalid Stock Amount!<br>"; //final string of errors
            uri = "/WEB-INF/jsp/error.jsp";
        }
        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                finalErr = "Invalid Stock Amount!<br>"; //final string of errors
                uri = "/WEB-INF/jsp/error.jsp";
            }
        } catch (NumberFormatException e) {
            finalErr = "Invalid Stock Amount!<br>"; //final string of errors
            uri = "/WEB-INF/jsp/error.jsp";

        }

        String status = "";

        if (stock == 0) {
            status = "Unavailable";
        }

        if (stock > 0) {
            status = "Available";
        }

        if(finalErr.isEmpty()){
            try {
                listingIO.updateStock(stock, status, parsedID);
            } catch (SQLException ex) {
                ex.printStackTrace();
                log("Error with updating listing");
            }
        }

        request.setAttribute("finalErr", finalErr);
        request.setAttribute("frontLink", "../user/do.viewlisting");
        request.setAttribute("frontName", "Back to Listings");
        request.setAttribute("backLink", "../user/do.viewlisting");
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
