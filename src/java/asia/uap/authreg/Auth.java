/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.authreg;

import asia.uap.beans.User;
import asia.uap.sql.ImageUtils;
import asia.uap.sql.UserIO;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
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
@WebServlet(name = "Auth", urlPatterns = {"/do.auth"})
public class Auth extends HttpServlet {

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

        String uri = "index.jsp";

        UserIO dbIO = new UserIO();

        HttpSession session = request.getSession(); //gets current session
        String username = request.getParameter("username"); //gets parameters
        String password = request.getParameter("password");
        boolean proceed = true; //checkers
        String error = "";

        if (username == null || username.isEmpty()) { //if any of the parameters are null or empty
            proceed = false;
        }
        if (password == null || password.isEmpty()) {
            proceed = false;
        }

        if (proceed == false) { //if proceed is false
            error = "Wrong username or password";
            response.sendRedirect("login.jsp?error=" + error); //send back to login page
            return;
        } else { //if proceed is true

            User user = new User();

            try {
                user = dbIO.getUserByUsername(username);
            } catch (SQLException e) {
                e.printStackTrace();
                log("UNABLE TO GET USER BY USERNAME");
            }

            boolean validUser = false;
            boolean approvedUser = false;

            if (user.getUsername() != null) {
                validUser = true;
                if (user.isApproved()) {
                    approvedUser = true;
                }
            }

            if (!validUser || !approvedUser) { //if the username isnt found
                if (!validUser) {
                    error = "Wrong username or password"; //send error
                }
                if (validUser && !approvedUser) {
                    error = "Waiting for account approval from administrator";
                }
                response.sendRedirect("login.jsp?error=" + error);
                return;
            } else { //if username is found

                Encryption encryptionTool = new Encryption();
                String inputtedpass = encryptionTool.encrypt(password);
                String encryptedPass = user.getPassword();

                if (inputtedpass.equals(encryptedPass)) { //checks if this specific users password equals this password
                    session.setAttribute("loggedin", user);

                } else {
                    session.setAttribute("loggedin", null);
                    error = "Wrong username or password";
                    response.sendRedirect("login.jsp?error=" + error);
                    return;

                }

            }

            if(error.isEmpty()){
                ImageUtils imageTool = new ImageUtils();
                // Directory where the images are saved
                String directory = imageTool.getImagePath() + "/user/";
                String fileName = user.getId() + "_image.jpg"; // Dynamic file naming
                File file = new File(directory, fileName);

                if (!file.exists()){
                    try {
                        // fetch the image BLOB from the database if no photo was uploaded
                        Blob imageBlob = imageTool.getImageBlobByUserId(user.getId());
                        if (imageBlob != null) {
                            // Save the image from the BLOB to the file, overwriting if necessary
                            ImageUtils.saveImageFromBlob(imageBlob, file);
                        }

                        // Set image path in the request for the JSP
                        request.setAttribute("imagePath", directory + fileName);
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                        error += "Invalid photo!<br>";
                        uri = "/WEB-INF/jsp/error.jsp";
                    }
                }
            }
            
            request.setAttribute("error", error);
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
