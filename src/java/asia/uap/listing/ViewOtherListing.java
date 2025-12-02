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
@WebServlet(name = "ViewOtherListing", urlPatterns = {"/user/do.viewotherlisting"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class ViewOtherListing extends HttpServlet {

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
        //fixed 107pm

        response.setContentType("text/html;charset=UTF-8");

        ListingIO listingIO = new ListingIO();
        String id = request.getParameter("id");
        HttpSession session = request.getSession(); // Get or create session
        String errmsg = "";
        request.setAttribute("type", "View Other's Listings");
        String uri = "/WEB-INF/jsp/listing/viewotherlisting.jsp";

        User loggedin = (User) session.getAttribute("loggedin");

        if (loggedin == null) {
            session.setAttribute("loggedin", null);
            errmsg = "You must login to view other user's listings!";
            uri = "/WEB-INF/jsp/error.jsp";
        }
        if (id == null) {
            errmsg = "Sorry! You can not view these listings!";
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            ArrayList<Listing> otherListings = new ArrayList<>();

            int parsedId = 0;
            try {
                parsedId = Integer.parseInt(id);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                errmsg += "Sorry! You can not view these listings!";
                uri = "/WEB-INF/jsp/error.jsp";
                request.setAttribute("finalErr", errmsg);
                RequestDispatcher rd = request.getRequestDispatcher(uri);
                rd.forward(request, response);
            }

            try {
                otherListings = listingIO.getOwnerListings(parsedId);
            } catch (SQLException e) {
                e.printStackTrace();
                log("error with getting ownerlistings");
            }
            request.setAttribute("otherListings", otherListings);
        }

        request.setAttribute("finalErr", errmsg);
        request.setAttribute("backLink", "do.shop");
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
