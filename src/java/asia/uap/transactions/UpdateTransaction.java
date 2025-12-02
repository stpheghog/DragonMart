/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.transactions;

import asia.uap.beans.Listing;
import asia.uap.beans.Transaction;
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

/**
 *
 * @author Stephanie
 */
@WebServlet(name = "UpdateTransaction", urlPatterns = {"/user/do.updatetransaction"})
public class UpdateTransaction extends HttpServlet {

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

        String type = request.getParameter("type");
        String action = request.getParameter("action");
        TransactionIO tIO = new TransactionIO();
        ListingIO lIO = new ListingIO();
        boolean proceed = true;
        Transaction transaction = new Transaction();
        String errmsg = "";
        String[] approvedActions = {"Accept", "Paid", "Cancel", "Received", "Deny"};
        String transactionId = request.getParameter("id");
        request.setAttribute("type", "Transaction");
        String uri = "/WEB-INF/jsp/error.jsp";
        
        if (action == null || action.isEmpty()) {
            proceed = false;
            errmsg += "Action is missing!<br>";
        }
        if (transactionId == null || transactionId.isEmpty()) {
            proceed = false;
            errmsg += "Transaction does not exist!<br>";
        }
        for (String i : approvedActions) {
            proceed = false;
            if (i.equals(action)) {
                proceed = true;
                break;
            }
        }

        if (proceed == true) { //if nothing is null

            int parsedID = 0;

            try {
                parsedID = Integer.parseInt(transactionId); // Convert the string to an integer
            } catch (NumberFormatException e) {
                log("cannot parse int");
            }

            if (action.equals("Accept")) {
                action = "Pending";
            }
            if (action.equals("Deny")) {
                action = "Deleted";
            }
            if (action.equals("Cancel")) {
                action = "Canceled";
            }

            if (action.equals("Pending") || action.equals("Canceled") || action.equals("Deleted")) {
                try {
                    tIO.updateTransactionStatus(parsedID, action);
                } catch (SQLException e) {
                    e.printStackTrace();
                    log("UNABLE TO UPDATE TRANSACTION STATUS");
                }
            }
            if (action.equals("Paid")) {
                try {
                    tIO.updateTransactionPaid(parsedID);
                } catch (SQLException e) {
                    e.printStackTrace();
                    log("UNABLE TO BE PAID");
                }
            }
            if (action.equals("Received")) {
                try {
                    tIO.updateTransactionReceived(parsedID);
                } catch (SQLException e) {
                    e.printStackTrace();
                    log("UNABLE TO BE RECEIVED");
                }
            }

            try {
                transaction = tIO.getTransactionByID(parsedID);
            } catch (SQLException e) {
                e.printStackTrace();
                log("UNABLE TO BE RECEIVED");
            }

            if (transaction.isIsPaid() && transaction.isIsReceived()) {
                try {
                    String status = "Available";
                    tIO.updateTransactionStatus(parsedID, "Completed");

                    Listing listing = lIO.getListingByID(transaction.getListingId());

                    int newStock = listing.getStock() - 1;

                    if (newStock == 0) {
                        status = "Unavailable";
                    }

                    lIO.updateStock(newStock, status, transaction.getListingId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    log("UNABLE TO UPDATE TRANSACTION STATUS");
                }
            }

            response.sendRedirect("do.viewtransaction?type=" + type);

        } else {
            request.setAttribute("backLink", "../user/userprofile.jsp");  
            request.setAttribute("finalErr", errmsg);
            RequestDispatcher rd = request.getRequestDispatcher(uri);

            rd.forward(request, response);
        }
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
