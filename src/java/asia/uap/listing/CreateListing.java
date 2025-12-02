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
import java.io.InputStream;
import java.sql.Blob;
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
import javax.servlet.http.Part;

/**
 *
 * @author blonyagoncillo
 */
@WebServlet(name = "CreateListing", urlPatterns = {"/user/do.createlisting"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class CreateListing extends HttpServlet {

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

        int listingid = 0;
        ListingIO listing = new ListingIO();

        HttpSession session = request.getSession(); // Get or create session
        User loggedin = (User) session.getAttribute("loggedin");

        request.setAttribute("type", "Create Listing");
        String uri = "/WEB-INF/jsp/success.jsp";
        String finalErr = "";

        boolean proceed = true;

        if (loggedin == null) {
            session.setAttribute("loggedin", null);
            proceed = false;
        }

        if (proceed == true) {

            // get parameters
            String title = request.getParameter("title");
            String owner = request.getParameter("ownerid");
            String priceStr = request.getParameter("price");
            String description = request.getParameter("description");
            String stock = request.getParameter("stock");
            Part photoPart = null;

            try {
                photoPart = request.getPart("photo");
            } catch (ServletException | IOException e) {
                finalErr = "Invalid Photo!<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            }

            // gets checkboxes values
            String[] selectedGenres = request.getParameterValues("genre");

            ArrayList<String> allTags = new ArrayList<>();

            if (photoPart == null || photoPart.getSize() == 0) {
                finalErr = "No Photo Uploaded!<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            } else {
                // value validation
                if (title == null || title.isEmpty()
                        || owner == null || owner.isEmpty()
                        || priceStr == null || priceStr.isEmpty()
                        || description == null || description.isEmpty()
                        || stock == null || stock.isEmpty()) { // Validate selected genres
                    finalErr = "All fields must be filled!<br>";
                    uri = "/WEB-INF/jsp/error.jsp";
                } else {

                    InputStream photoInputStream = photoPart.getInputStream();
                    byte[] photoBytes = null;

                    if (selectedGenres == null || selectedGenres.length == 0) {
                        selectedGenres = new String[0];
                    }

                    allTags.addAll(Arrays.asList(selectedGenres));

                    String customTags = request.getParameter("customTags");

                    if (customTags != null && !customTags.isEmpty()) {
                        String[] newTags = customTags.split(",");
                        for (String i : newTags) {
                            allTags.add(i.trim());
                        }
                    }

                    int parsedID = 0;

                    try {
                        parsedID = Integer.parseInt(owner.trim());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        finalErr = "Invalid Owner!";
                        uri = "/WEB-INF/jsp/error.jsp";
                    }

                    //check if stock is a number
                    int parsedStock = 0;

                    try {
                        parsedStock = Integer.parseInt(stock);
                    } catch (NumberFormatException e) {
                        finalErr = "Invalid Stock!";
                        uri = "/WEB-INF/jsp/error.jsp";
                    }

                    if (parsedStock <= 0) {
                        finalErr = "Invalid Stock!";
                        uri = "/WEB-INF/jsp/error.jsp";
                    }

                    // checks if price is number
                    double price = 0;
                    try {
                        price = Double.parseDouble(priceStr);
                        if (price < 0 || price > 999.99) {
                            finalErr = "Invalid Price! Must be between 0.00 and 999.99.<br>";
                            uri = "/WEB-INF/jsp/error.jsp";
                        } else if (priceStr.contains(".") && priceStr.split("\\.")[1].length() > 2) {
                            finalErr = "Invalid Price! Only two decimal places allowed.<br>";
                            uri = "/WEB-INF/jsp/error.jsp";
                        }
                    } catch (NumberFormatException e) {
                        finalErr = "Invalid Price!<br>";
                        uri = "/WEB-INF/jsp/error.jsp";
                    }
                    if (price < 0) {
                        finalErr = "Invalid Price!<br>";
                        uri = "/WEB-INF/jsp/error.jsp";
                    }

                    if (photoInputStream != null) {
                        photoBytes = new byte[(int) photoPart.getSize()];
                        try {
                            photoInputStream.read(photoBytes); //Convert photo to byte array
                        } catch (IOException e) {
                            log("PHOTO: Error reading photo");
                            e.printStackTrace();
                        }
                    }

                    if (finalErr.isEmpty()) {
                        // creates new listing with initial status automatically set to "Available"
                        Listing newListing = new Listing();

                        newListing.setDesc(description);
                        newListing.setTitle(title);
                        newListing.setIsArchived(false);
                        newListing.setOwnerID(parsedID);
                        newListing.setPhoto(photoBytes);
                        newListing.setPrice(price);
                        newListing.setStatus("Available");
                        newListing.setStock(parsedStock);

                        try {
                            listingid = listing.addListing(newListing);

                        } catch (SQLException e) {
                            e.printStackTrace();
                            log("Error with creating new listing");
                        }

                        try {
                            listing.addTags(listingid, allTags);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            log("Error with adding tags");
                        }

                        ImageUtils imageTool = new ImageUtils();
                        String directory = imageTool.getImagePath() + "/listings/";
                        String fileName = newListing.getId() + "_image.jpg"; // Dynamic file naming
                        File file = new File(directory, fileName);

                        try {
                            // Always overwrite the image file (removes the file existence check)
                            if (photoBytes != null && photoBytes.length > 0) {
                                if (file.exists()) {
                                    file.delete();  // Delete the existing file before saving the new one
                                }
                                // Save the uploaded photo as a file (overwrites any existing file)
                                ImageUtils.saveImageFromBytes(photoBytes, file);
                            } else {
                                // Optionally, fetch the image BLOB from the database if no photo was uploaded
                                Blob imageBlob = imageTool.getImageBlobByListingId(newListing.getId());
                                if (imageBlob != null) {
                                    // Save the image from the BLOB to the file, overwriting if necessary
                                    ImageUtils.saveImageFromBlob(imageBlob, file);
                                }
                            }

                            // sets image path in the request for the JSP
                            request.setAttribute("imagePath", directory + fileName);
                        } catch (SQLException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            request.setAttribute("finalErr", finalErr);
            request.setAttribute("frontLink", "do.viewlisting");
            request.setAttribute("frontName", "View Listings");
            request.setAttribute("backLink", "../user/userprofile.jsp");
            RequestDispatcher rd = request.getRequestDispatcher(uri);
            rd.forward(request, response);

        }  //if not

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
