/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.listing;

import asia.uap.beans.Listing;
import asia.uap.beans.Tag;
import asia.uap.sql.CategoryIO;
import asia.uap.sql.ListingIO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 225201
 */
@WebServlet(name = "CategoryView", urlPatterns = {"/do.category"})
public class CategoryView extends HttpServlet {

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

        String type = request.getParameter("type");
        String order = request.getParameter("order");
        String query = request.getParameter("query");
        CategoryIO cIO = new CategoryIO();
        ListingIO listingIO = new ListingIO();
        String errmsg = "";
        String result = "";
        ArrayList<Listing> listingList = new ArrayList<>();
        String[] tag = request.getParameterValues("tag");
        ArrayList<Tag> tagList = new ArrayList<>();
        request.setAttribute("type", "Categorization");

        String uri = "/WEB-INF/jsp/listing/categoryview.jsp";

        if (((type == null || type.isEmpty() || order == null || order.isEmpty()) && tag == null) && (query == null || query.isEmpty())) {
            errmsg = "Unable to categorize listings...<br>";
            uri = "/WEB-INF/jsp/error.jsp";
        } else {
            if (query == null) {
                query = "";
            }
            if (type == null) {
                type = "";
            }
            if (order == null) {
                order = "";
            }
            if (tag == null) {
                tag = new String[0];
            }

            if (!query.isEmpty() && type.isEmpty() && order.isEmpty() && tag.length == 0) {
                try {
                    listingList = listingIO.getAllSearchedListings(query);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    log("Error with fetching all listings");
                }
                result += query;
            } else {
                if (tag.length > 0) {
                    if (query.isEmpty()) {
                        try {
                            listingList = cIO.getListingsByTag(tag);
                            result += "Listings with tags: ";
                            String tags = String.join(", ", tag);
                            result += tags;
                        } catch (SQLException e) {
                            e.printStackTrace();
                            log("UNABLE TO GET ALL Listings");
                        }
                    } else {
                        try {
                            listingList = cIO.getSearchedListingsByTag(query, tag);
                            result += query + " with tags: ";
                            String tags = String.join(", ", tag);
                            result += tags;
                        } catch (SQLException e) {
                            e.printStackTrace();
                            log("UNABLE TO GET ALL Listings");
                        }
                    }
                } else {
                    if (query.isEmpty()) {
                        result += "Listings in ";
                        if (type.equals("price") || type.equals("date_time")) {
                            try {
                                listingList = cIO.orderListing(order, type);
                                if (type.equals("price")) {
                                    if (order.equals("ASC")) {
                                        result += "Cheap → Expensive";
                                    } else {
                                        result += "Expensive → Cheap";
                                    }
                                } else {
                                    if (order.equals("ASC")) {
                                        result += "Newest → Oldest";
                                    } else {
                                        result += "Oldest → Newest";
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                log("UNABLE TO GET ALL Listings");
                            }
                        }

                        if (type.equals("alphabet")) {
                            try {
                                listingList = cIO.orderListingByAlphabetical(order);
                                if (order.equals("ASC")) {
                                    result += "A → Z";
                                } else {
                                    result += "Z → A";
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                log("UNABLE TO GET ALL Listings");
                            }
                        }

                    } else {
                        result += query + " ";
                        if (type.equals("price") || type.equals("date_time")) {
                            try {
                                listingList = cIO.orderSearchedListing(order, query, type);
                                if (type.equals("price")) {
                                    if (order.equals("ASC")) {
                                        result += "Cheap → Expensive";
                                    } else {
                                        result += "Expensive → Cheap";
                                    }
                                } else {
                                    if (order.equals("ASC")) {
                                        result += "Newest → Oldest";
                                    } else {
                                        result += "Oldest → Newest";
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                log("UNABLE TO GET ALL Listings");
                            }
                        }

                        if (type.equals("alphabet")) {
                            try {
                                listingList = cIO.orderSearchedListingByAlphabetical(query, order);
                                if (order.equals("ASC")) {
                                    result += "A → Z";
                                } else {
                                    result += "Z → A";
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                log("UNABLE TO GET ALL Listings");
                            }
                        }
                    }
                }
            }

            if (listingList.isEmpty()) {
                errmsg = "Sorry! No items with this filter available!<br>";
                uri = "/WEB-INF/jsp/error.jsp";
            }

            try {
                tagList = listingIO.getAllTags();
            } catch (SQLException e) {
                e.printStackTrace();
                log("UNABLE TO GET ALL TAGS");
            }
        }

        request.setAttribute("backLink", "do.shop");       
        request.setAttribute("result", result);
        request.setAttribute("checkedTag", tag);
        request.setAttribute("query", query);
        request.setAttribute("tagList", tagList);
        request.setAttribute("availableListings", listingList);
        request.setAttribute("finalErr", errmsg);
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
