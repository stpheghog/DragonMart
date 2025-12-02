/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package asia.uap.authreg;

import asia.uap.beans.User;
import asia.uap.sql.UserIO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
 * @author lucyallysongarcia
 */
@WebServlet(name = "Register", urlPatterns = {"/do.register"})
public class Register extends HttpServlet {

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

        UserIO dbIO = new UserIO();

        HttpSession session = request.getSession();
        String firstname = request.getParameter("fn"); //gets the parameters
        String lastname = request.getParameter("ln");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String check = request.getParameter("check");
        String dob = request.getParameter("dob");

        request.setAttribute("type", "Registration");
        String uri = "/WEB-INF/jsp/success.jsp";

        User loggedin = (User) session.getAttribute("loggedin");

        if (loggedin == null) {
            session.setAttribute("loggedin", null);
        }

        boolean proceed = true; //checkers
        String finalErr = ""; //final string of errors

        ArrayList<String> errorMsg = new ArrayList<>(); //arraylist of messages

        if (username == null || username.isEmpty()) { //checking if anything is null or if fields were left empty
            errorMsg.add("username"); //username
            proceed = false;
        }
        if (password == null || password.isEmpty()) {
            errorMsg.add("password"); //password
            proceed = false;
        }
        if (firstname == null || firstname.isEmpty()) {
            errorMsg.add("firstname"); //firstname
            proceed = false;
        }
        if (lastname == null || lastname.isEmpty()) {
            errorMsg.add("lastname"); //lastname
            proceed = false;
        }
        if (email == null || email.isEmpty()) {
            errorMsg.add("email"); //email
            proceed = false;
        }
        if (dob == null || dob.isEmpty()) {
            errorMsg.add("date of birth"); //date of birth
            proceed = false;
        } else {
            try {
                LocalDate parsedob = LocalDate.parse(dob);
            } catch (DateTimeParseException e) {
                errorMsg.add("<br><br>Invalid date of birth format. Please use YYYY-MM-DD");
                proceed = false;
            }
        }
        if (check == null || check.isEmpty()) {
            errorMsg.add("confirm password"); //confirm password
            proceed = false;
        }

        if (proceed == false) { //if any of the fields are null/empty
            uri = "/WEB-INF/jsp/error.jsp";
            String error = String.join(", ", errorMsg);
            finalErr = "You have missed field/s: " + error;
        } else { //if not

            if (check != null && password != null && !password.equals(check)) { //null check and if password = confirm password
                finalErr += "Passwords do not match <br>";
                proceed = false;
            }

            ArrayList<User> userList = new ArrayList<>();

            try {
                userList = dbIO.getAllUsers();
            } catch (SQLException e) {
                e.printStackTrace();
                log("UNABLE TO GET ALL USERS");
            }

            for (User i : userList) { //goes through all users and checks if username is unique
                if (i.getUsername().equals(username)) {
                    finalErr += "Username already in use <br>";
                    proceed = false;
                    break;
                }
            }

            for (User i : userList) { //goes through all users and checks if username is unique
                if (i.getEmail().equals(email)) {
                    finalErr += "This email has already been registered <br>";
                    proceed = false;
                    break;
                }
            }

            if (proceed == true) {

                Encryption encryptionTool = new Encryption();
                String encryptedPass = encryptionTool.encrypt(password);

                User user = new User(); //adds newly registered user into allusers list
                user.setUsername(username);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setEmail(email);
                user.setDob(dob); // not the parsed ob
                user.setAboutme("");
                user.setPassword(String.valueOf(encryptedPass));
                user.setContact("");
                user.setPhoto(new byte[0]);
                user.setApproved(false);

                try {
                    dbIO.addUser(user);
                } catch (SQLException e) {
                    e.printStackTrace();
                    log("UNABLE TO ADD USER");
                }

            } else {
                uri = "/WEB-INF/jsp/error.jsp";
            }
        }

        request.setAttribute("finalErr", finalErr);
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
