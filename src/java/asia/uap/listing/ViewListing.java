/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.listing;

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
 * @author blonyagoncillo
 */
@WebServlet(name = "ViewListing", urlPatterns = {"/user/do.viewlisting"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class ViewListing extends HttpServlet {

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
        HttpSession session = request.getSession(); // Get or create session
        User loggedin = (User) session.getAttribute("loggedin");
        String errmsg = "";
        request.setAttribute("type", "View Own Listings");
        String uri = "/WEB-INF/jsp/listing/viewlisting.jsp";

        if (loggedin == null) {
            errmsg = "Sorry! Listing does not exist!";
            request.setAttribute("finalErr", errmsg);
            session.setAttribute("loggedin", null);
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            ArrayList<Listing> ownerListings = new ArrayList<>();

            try {
                ownerListings = listingIO.getOwnerListings(loggedin.getId());
            } catch (SQLException e) {
                e.printStackTrace();
                log("error with getting ownerlistings");
            }
            request.setAttribute("ownerListings", ownerListings);

            
            ImageUtils imageTool = new ImageUtils();
            String directory = imageTool.getImagePath() + "/listings/";

            for (Listing listing : ownerListings) {
                String fileName = listing.getId() + "_image.jpg"; // Dynamic file naming
                File file = new File(directory, fileName);

                try {
                    // Always overwrite the image file (removes the file existence check)
                    if (file.exists()) {
                        file.delete();  // Delete the existing file before saving the new one
                    }

                    // Get the image BLOB from the DB
                    Blob imageBlob = imageTool.getImageBlobByListingId(listing.getId());

                    if (imageBlob != null) {
                        // Save the image from the BLOB to the file, overwriting if necessary
                        ImageUtils.saveImageFromBlob(imageBlob, file);
                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                    log("Error with writing photo to file");
                    uri = "/WEB-INF/jsp/error.jsp";
                }
            }

        }
        request.setAttribute("error", errmsg);
        request.setAttribute("backLink", "../user/userprofile.jsp");  
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
