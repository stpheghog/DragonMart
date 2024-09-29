/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String username = request.getParameter("username"); 
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String dob = request.getParameter("dob");

        if (username == null || password == null || firstname == null || lastname == null || email == null || username.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || email.isEmpty()) {
            response.sendRedirect("signup.jsp?error=ERROR: MISSING FIELDS"); //checks for any missing fields
            return;
        }
        
        if (dob == null){
            dob = "";
        }

        ArrayList<User> Users = null; //creates new array list that can hold user objects
        HttpSession session = request.getSession(); //gets the current session
        Users = (ArrayList) session.getAttribute("alluser"); //gets the current session's all user attribute

        if (Users == null || Users.isEmpty()) { //checks if there is an arraylist
            Users = new ArrayList<User>(); //if not make a new arraylist
        }

        for (User i : Users) { //goes through all exisiting users
            if (i.getUsername().equals(username)) { //if there are any usernames that are the same as the registering username
                response.sendRedirect("add.jsp?error=ERROR: USERNAME ALREADY IN USE"); //invalid
                return;
            }
        }

        User user = new User(); //creates a new user instance
        user.username = username; //adds all attributes
        user.firstname = firstname;
        user.lastname = lastname;
        user.dob = dob;
        user.password = password;
        user.email = email;


        Users.add(user); //adds current sessions user informations to the Users arraylist

        session.setAttribute("alluser", Users); //sets the attribute of Users to have all the past user's information and the newly registered user
        //basically adds the new user to the list

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");  
            out.println("<title> Registering... </title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Register at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
