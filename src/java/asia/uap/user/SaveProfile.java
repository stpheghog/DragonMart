/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.user;

import asia.uap.beans.User;
import asia.uap.sql.ImageUtils;
import asia.uap.sql.UserIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
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
 * @author Stephanie
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max file size
@WebServlet(name = "SaveProfile", urlPatterns = {"/user/do.saveprofile"})
public class SaveProfile extends HttpServlet {

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
        UserIO dbIO = new UserIO();
        request.setAttribute("type", "Edit Profile");
        String uri = "/WEB-INF/jsp/success.jsp";
        String finalErr = ""; //final string of errors

        User currentUser = (User) session.getAttribute("loggedin");
        if (currentUser == null) {
            response.sendRedirect("../login.jsp?error=You%20are%20not%20logged%20in!");
            return;
        }

        String editedUsername = request.getParameter("username");
        String editedEmail = request.getParameter("email");
        String editedFirstname = request.getParameter("firstname");
        String editedLastname = request.getParameter("lastname");
        String editedContact = request.getParameter("contact");
        String editedAbtme = request.getParameter("aboutme");
        Part photoPart = request.getPart("photo");

        if (editedUsername == null || editedUsername.isEmpty()
                || editedEmail == null || editedEmail.isEmpty()
                || editedFirstname == null || editedFirstname.isEmpty()
                || editedLastname == null || editedLastname.isEmpty()
                || editedContact == null || editedContact.isEmpty()
                || photoPart == null || photoPart.getSize() == 0
                || editedAbtme == null || editedAbtme.isEmpty()) {

            finalErr += "Missing fields!<br>";
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            InputStream photoInputStream = photoPart.getInputStream();
            byte[] photoBytes = null;

            String formattedContact;
            if (editedContact.startsWith("http://") || editedContact.startsWith("https://")) {
                formattedContact = editedContact;
            } else if (editedContact.matches("^[+\\d\\s\\-()]*$")) {
                formattedContact = "tel:" + editedContact;
            } else {
                formattedContact = "https://" + editedContact;
            }

            if (photoInputStream != null) {
                photoBytes = new byte[(int) photoPart.getSize()];
                try {
                    photoInputStream.read(photoBytes);
                } catch (IOException e) {
                    log("PHOTO: Error reading photo");
                    e.printStackTrace();
                    finalErr += "Invalid photo!<br>";
                    uri = "/WEB-INF/jsp/error.jsp";
                }
            }

            User userToEdit = new User();
            userToEdit.setId(currentUser.getId());
            userToEdit.setPassword(currentUser.getPassword());
            userToEdit.setUsername(editedUsername);
            userToEdit.setEmail(editedEmail);
            userToEdit.setFirstname(editedFirstname);
            userToEdit.setPhoto(photoBytes);
            userToEdit.setDob(currentUser.getDob());
            userToEdit.setLastname(editedLastname);
            userToEdit.setContact(formattedContact);
            userToEdit.setAboutme(editedAbtme);
            userToEdit.setApproved(currentUser.isApproved());
            userToEdit.setArchived(currentUser.isArchived());

            try {
                dbIO.updateUser(currentUser.getId(), userToEdit);
            } catch (SQLException e) {
                e.printStackTrace();
                log("Error updating user information: " + e.getMessage());
                finalErr += "Unable to update user information!<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            }

            ImageUtils imageTool = new ImageUtils();
            // Directory where the images are saved
            String directory = imageTool.getImagePath() + "/user/";
            String fileName = userToEdit.getId() + "_image.jpg"; // Dynamic file naming
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
                    Blob imageBlob = imageTool.getImageBlobByUserId(userToEdit.getId());
                    if (imageBlob != null) {
                        // Save the image from the BLOB to the file, overwriting if necessary
                        ImageUtils.saveImageFromBlob(imageBlob, file);
                    }
                }

                // Set image path in the request for the JSP
                request.setAttribute("imagePath", directory + fileName);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                finalErr += "Invalid photo!<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            }

            session.setAttribute("loggedin", userToEdit);
        }
        request.setAttribute("finalErr", finalErr);
        request.setAttribute("frontLink", "../user/userprofile.jsp");
        request.setAttribute("frontName", "Back to Profile");
        request.setAttribute("backLink", "../user/editprofile.jsp");
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
