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
@WebServlet(name = "SaveEdit", urlPatterns = {"/user/do.savelisting"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
public class SaveEdit extends HttpServlet {

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

        HttpSession session = request.getSession(); //gets or creates session
        // value validation

        ListingIO listingIO = new ListingIO();

        //checks for and gets logged in user object
        User loggedin = (User) session.getAttribute("loggedin");
        request.setAttribute("type", "Edit Listing");
        String uri = "/WEB-INF/jsp/success.jsp";
        String finalErr = ""; //final string of errors

        if (loggedin == null) {
            session.setAttribute("loggedin", null);

            response.sendRedirect("../do.login?errmsg=USER%20NOT%20LOGGED%20IN");
            return;
        }

        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            finalErr = "Invalid Listing!<br>";
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
            finalErr = "Listing not Found!<br>";
            uri = "/WEB-INF/jsp/error.jsp";
        }

        //new title
        String editedTitle = request.getParameter("title");
        if (editedTitle == null || editedTitle.isEmpty()) {
            finalErr = "Invalid Title!<br>";
            uri = "/WEB-INF/jsp/error.jsp";
        }

        //new photo
        Part photo = request.getPart("photo");
        if (photo == null || photo.getSize() == 0) {
            finalErr = "Invalid Photo!<br>";
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            InputStream photoInputStream = photo.getInputStream();
            byte[] photoBytes = null;

            if (photoInputStream != null) {
                photoBytes = new byte[(int) photo.getSize()];
                try {
                    photoInputStream.read(photoBytes); // Convert photo to byte array
                } catch (IOException e) {
                    log("PHOTO: Error reading photo");
                    e.printStackTrace();
                }
            }

            listingToEdit.setPhoto(photoBytes); // update photo

            //price
            String priceStr = request.getParameter("price");
            double price = 0;
            if (priceStr == null || priceStr.isEmpty()) {
                finalErr = "Invalid Price!<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            }
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0 || price > 999.99) {
                    finalErr = "Invalid Price! Must be between 0.00 and 999.99.<br>";
                    uri = "/WEB-INF/jsp/error.jsp";
                } else if (priceStr.contains(".") && priceStr.split("\\.")[1].length() > 2) {
                    finalErr = "Invalid Price! Only two decimal places allowed.<br>";
                    uri = "/WEB-INF/jsp/error.jsp";
                }
                if (price < 0) {
                    finalErr = "Invalid Price!<br>";
                    uri = "/WEB-INF/jsp/error.jsp";
                }
            } catch (NumberFormatException e) {
                finalErr = "Invalid Price!<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            }

            //desc
            String desc = request.getParameter("desc");
            if (desc == null || desc.isEmpty()) {
                finalErr = "Invalid Description!<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            }

            // genre + customtags
            ArrayList<Integer> allTagIDs = new ArrayList<>();
            int tagID = 0;

            String[] selectedGenres = request.getParameterValues("genre");
            if (selectedGenres == null || selectedGenres.length == 0) {
                selectedGenres = new String[0];
            }

            for (String genre : selectedGenres) {
                try {
                    tagID = listingIO.checkAndInsertCustomTag(genre);
                    if (tagID >= 0) {
                        allTagIDs.add(tagID);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    log("Cannot check and insert tag: " + genre);
                }
            }

            String customTagsInput = request.getParameter("customTags");

            if (customTagsInput != null && !customTagsInput.trim().isEmpty()) {
                String[] customTags = customTagsInput.split(",");
                for (String tag : customTags) {
                    tag = tag.trim();
                    // checks if tag exists in the listing_tags table and inserts if not
                    try {
                        tagID = listingIO.checkAndInsertCustomTag(tag); // returns tagID of existing or new tag
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        log("Cannot check and insert custom tag");
                    }
                    if (tagID > 0) { // if tagid exists
                        allTagIDs.add(tagID); //adds to all tags arraylist
                    }
                }

            }

            if (finalErr.isEmpty()) {
                try {
                    listingIO.updateTagsJunction(listingToEdit.getId(), allTagIDs);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    log("Error with adding tags");
                }

                listingToEdit.setPrice(price); // Update price
                listingToEdit.setDesc(desc); // Update description
                listingToEdit.setTitle(editedTitle); // update title

                ImageUtils imageTool = new ImageUtils();
                String directory = imageTool.getImagePath() + "/listings/";
                String fileName = listingToEdit.getId() + "_image.jpg"; // Dynamic file naming
                File file = new File(directory, fileName);

                try {
                    listingIO.editListing(listingToEdit);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    log("Error with updating listing");
                }

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
                        Blob imageBlob = imageTool.getImageBlobByListingId(listingToEdit.getId());
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
