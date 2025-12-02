/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.listing;

import asia.uap.sql.RequestIO;
import asia.uap.beans.User;
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
@WebServlet(name = "ReportAbuse", urlPatterns = {"/user/do.reportabuse"})
public class ReportAbuse extends HttpServlet {

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
// Getting the session and logged-in user
        HttpSession session = request.getSession();
        User loggedin = (User) session.getAttribute("loggedin");
        request.setAttribute("type", "Report Abuse");
        String uri = "/WEB-INF/jsp/success.jsp";
        String finalErr = ""; //final string of errors

        // If no user is logged in, redirect to login
        if (loggedin == null) {
            response.sendRedirect("do.login?Error%20NO%20LOGGEDINUSER");
            return;
        }

        // Parsing parameters
        String commentIdParam = request.getParameter("id");
        String listingIdParam = request.getParameter("listingid");

        if (commentIdParam == null || commentIdParam.isEmpty() || listingIdParam == null || listingIdParam.isEmpty()) {
            finalErr += "Invalid Content<br>";
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            int commentId = 0;
            int listingId = 0;

            try {
                commentId = Integer.parseInt(commentIdParam);
                listingId = Integer.parseInt(listingIdParam);
            } catch (NumberFormatException e) {
                finalErr += "Invalid Listing<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            }

            // Proceed to report abuse
            RequestIO reqIO = new RequestIO();

            // Add the comment delete request, assuming 0 is the admin%20id placeholder for the report
            try {
                reqIO.addUserReport(commentId, loggedin.getId());  // Assuming 0 means a normal user or placeholder for admin
            } catch (SQLException ex) {
                ex.printStackTrace();
                log("Error adding userreport req by user");
            }
        }
            // Redirect to item view page after processing
            request.setAttribute("frontLink", "../do.shop");
            request.setAttribute("frontName", "Back to Shop");
            request.setAttribute("backLink", "../do.shop");
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
