/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.transactions;

import asia.uap.beans.Transaction;
import asia.uap.beans.User;
import asia.uap.sql.TransactionIO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
@WebServlet(name = "ViewTransaction", urlPatterns = {"/user/do.viewtransaction"})
public class ViewTransaction extends HttpServlet {

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
        String type = request.getParameter("type");
        String action = request.getParameter("action");
        User loggedin = (User) session.getAttribute("loggedin");
        TransactionIO tIO = new TransactionIO();
        String errmsg = "";
        String uri = "";

        request.setAttribute("type", "Transaction");

        if (type == null || type.isEmpty() || (!type.equals("Sale") && !type.equals("Purchase"))) {
            errmsg = "Unable to view transactions!<br>";
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            if(action == null){
                action = "";
            }
            if (!action.equals("Canceled")) {
                ArrayList<Transaction> transList = new ArrayList<>();
                ArrayList<Transaction> pendList = new ArrayList<>();
                ArrayList<Transaction> compList = new ArrayList<>();
                ArrayList<Transaction> reqList = new ArrayList<>();

                if (type.equals("Sale")) {

                    uri = "/WEB-INF/jsp/transactions/owntransaction.jsp";

                    try {
                        transList = tIO.getUserSales(loggedin.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        log("UNABLE TO GET ALL USERS");
                    }
                }
                if (type.equals("Purchase")) {

                    uri = "/WEB-INF/jsp/transactions/othertransaction.jsp";

                    try {
                        transList = tIO.getUserPurchases(loggedin.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        log("UNABLE TO GET ALL USERS");
                    }
                }
                for (Transaction i : transList) {
                    if (i.getStatus().equals("Requested")) {
                        reqList.add(i);
                    }
                    if (i.getStatus().equals("Pending")) {
                        pendList.add(i);
                    }
                    if (i.getStatus().equals("Completed")) {
                        compList.add(i);
                    }
                }

                request.setAttribute("reqList", reqList);
                request.setAttribute("pendList", pendList);
                request.setAttribute("compList", compList);
            } else {

                ArrayList<Transaction> cancList = new ArrayList<>();
                if (type.equals("Sale")) {

                    uri = "/WEB-INF/jsp/transactions/canceledsale.jsp";

                    try {
                        cancList = tIO.getCanceledUserSales(loggedin.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        log("UNABLE TO GET CANCELED SALES");
                    }
                }
                if (type.equals("Purchase")) {

                    uri = "/WEB-INF/jsp/transactions/canceledpurchase.jsp";

                    try {
                        cancList = tIO.getCanceledUserPurchases(loggedin.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        log("UNABLE TO GET ALL CANCELED PURCHASES");
                    }
                }
                request.setAttribute("cancList", cancList);
            }
        }

        request.setAttribute("backLink", "../user/userprofile.jsp"); 
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
