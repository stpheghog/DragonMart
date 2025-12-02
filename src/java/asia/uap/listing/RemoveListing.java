/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.listing;

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
@WebServlet(name = "RemoveListing", urlPatterns = {"/user/do.removelisting"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class RemoveListing extends HttpServlet {

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

        //fixed this 1:07pm
        // get params/attributes and value validation
        HttpSession session = request.getSession();
        ListingIO listingIO = new ListingIO();
        request.setAttribute("type", "Remove Listing");
        String uri = "/WEB-INF/jsp/success.jsp";
        String finalErr = ""; //final string of errors

        User loggedin = (User) session.getAttribute("loggedin");

        if (loggedin == null) {
            session.setAttribute("loggedin", null);
            response.sendRedirect("../do.login?ErrorMessage=USER_NOT_LOGGED_IN");
            return;
        }

        //checkbox values
        String[] listingsToRemove = request.getParameterValues("removeListing");

        if (listingsToRemove == null || listingsToRemove.length <= 0) {
            finalErr = "Invalid Listings";
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            ArrayList<String> listingIDs = new ArrayList<>();

            listingIDs.addAll(Arrays.asList(listingsToRemove));

            try {
                listingIO.removeListing(listingIDs);
            } catch (SQLException ex) {
                ex.printStackTrace();
                log("error with archiving listings");
            }
        }

        request.setAttribute("finalErr", finalErr);
        request.setAttribute("frontLink", "../user/do.viewlisting");
        request.setAttribute("frontName", "Back to Your Listings");
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
