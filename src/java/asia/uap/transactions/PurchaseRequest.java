/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.transactions;

import asia.uap.beans.Listing;
import asia.uap.beans.User;
import asia.uap.sql.ListingIO;
import asia.uap.sql.TransactionIO;
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
@WebServlet(name = "PurchaseRequest", urlPatterns = {"/user/do.purchaserequest"})
public class PurchaseRequest extends HttpServlet {

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

        String listingId = request.getParameter("listingid");
        int parsedID = 0;
        boolean proceed = true;
        String errmsg = "";
        TransactionIO tIO = new TransactionIO();
        ListingIO lIO = new ListingIO();
        HttpSession session = request.getSession();
        User loggedin = (User) session.getAttribute("loggedin");
        request.setAttribute("type", "Purchase Request");
        String uri = "/WEB-INF/jsp/success.jsp";

        if (loggedin == null) {
            proceed = false;
            errmsg += "You must be logged into purchase items!<br>";
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            if (listingId == null || listingId.isEmpty()) {
                proceed = false;
                errmsg += "Listing does not exist!<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            } else {

                try {
                    parsedID = Integer.parseInt(listingId);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    log("error with parsing listingid");
                }

                Listing listing = new Listing();
                try {
                    listing = lIO.getListingByID(parsedID);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    log("Error with getting listng");
                }

                if (listing.getOwnerID() == loggedin.getId()) {
                    proceed = false;
                    errmsg += "You cannot purchase your own items!<br>";
                }

                if (proceed == true) {

                    try {
                        tIO.addTransaction(parsedID, loggedin.getId());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        log("Error with getting listng");
                    }
                } else {
                    uri = "/WEB-INF/jsp/error.jsp";
                }
            }
        }

        request.setAttribute("frontLink", "../user/do.viewtransaction?type=Purchase");
        request.setAttribute("frontName", "View Purchases");
        request.setAttribute("backLink", "../do.shop");  
        request.setAttribute("finalErr", errmsg);
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
