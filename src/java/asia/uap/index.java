/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stephanie
 */
@WebServlet(name = "index", urlPatterns = {"/index.jsp"})
public class index extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>DragonMart</title>");
            out.println("<link rel=\"stylesheet\" href=\"lightmode.css\">");
            out.println("</head>");
            out.println("<body>");

            out.println("<nav class=\"topnav\">"); //COPY PASTE THIS FOR NAV BAR
            out.println("<div class=\"logo\">");
            out.println("<img class=\"logo-img\" src=\"images/logo.png\" alt=\"logo\">");
            out.println("<p class=\"logo-text\"> DragonMart </p>");
            out.println("</div>");
            out.println("<ul class=\"nav-list\">");
            out.println("<li><a href=\"#home\" class=\"home\">Home</a></li>");
            out.println("<li><a href=\"#shop\">Shop</a></li>");
            out.println("<li><a href=\"#contact\">Contact</a></li>");
            out.println("<li><a href=\"#about\">About</a></li>");
            out.println("</ul>");
            out.println("<div class=\"auth-links\">");
            
            if (loggedin == true){
                out.println("<a href=\"#login\" class=\"registration\">Login</a>");
                out.println("<a href=\"#signup\" class=\"registration\">Sign up</a>");
            }
            else{
                out.println("<a href=\"#login\" class=\"registration\">Login</a>");
                out.println("<a href=\"#signup\" class=\"registration\">Sign up</a>");
            }
            
            out.println("</div>");
            out.println("</nav>"); //NAV BAR ENDS HERE

            out.println("<h1>Servlet index at " + request.getContextPath() + "</h1>");
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
