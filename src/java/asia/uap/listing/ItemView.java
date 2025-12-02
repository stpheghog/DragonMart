/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.listing;

import asia.uap.beans.Comment;
import asia.uap.beans.Listing;
import asia.uap.beans.User;
import asia.uap.sql.ImageUtils;
import asia.uap.sql.ListingIO;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
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
 * @author Stephanie
 */
@WebServlet(name = "ItemView", urlPatterns = {"/do.itemview"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class ItemView extends HttpServlet {

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

        String listingId = request.getParameter("id");
        HttpSession session = request.getSession();
        User loggedin = (User) session.getAttribute("loggedin");
        int parsedID = 0;

        request.setAttribute("type", "View Listing");
        request.setAttribute("backLink", "do.shop");       
        String uri = "/WEB-INF/jsp/listing/itemview.jsp";

        ListingIO listingIO = new ListingIO();

        String errmsg = "";

        if (listingId == null || listingId.isEmpty()) {
            errmsg = "Sorry! Listing does not exist!";
            request.setAttribute("finalErr", errmsg);
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            if (loggedin == null) {
                session.setAttribute("loggedin", null);
            }

            try {
                parsedID = Integer.parseInt(listingId);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                log("error with parsing listingid");
            }
            
            ImageUtils imageTool = new ImageUtils();
            String directory = imageTool.getImagePath() + "/listings/";
            String fileName = listingId + "_image.jpg"; // Dynamic file naming
            File file = new File(directory, fileName);

            try {
                // Check if the image file already exists
                if (!file.exists()) {
                    // Fetch the image BLOB from the database using the DatabaseUtils class
                    Blob imageBlob = imageTool.getImageBlobByListingId(parsedID);

                    if (imageBlob != null) {
                        // Save the image from the BLOB to the file using ImageUtils
                        ImageUtils.saveImageFromBlob(imageBlob, file);
                    }
                }

                // sets image path in the request for the JSP
                request.setAttribute("imagePath", directory + fileName);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }

            Listing listing = null;
            try {
                listing = listingIO.getListingByID(parsedID);
            } catch (SQLException ex) {
                ex.printStackTrace();
                log("Error with getting listng");
            }

            ArrayList<Comment> comment = new ArrayList<>();
            try {
                comment = listingIO.getListingComments(parsedID);
            } catch (SQLException ex) {
                ex.printStackTrace();
                log("Error with getting comments of listing");
            }

            request.setAttribute("comment", comment);
            request.setAttribute("listing", listing);
        }
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
