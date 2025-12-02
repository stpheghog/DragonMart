/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.sql;

import asia.uap.beans.Listing;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author Stephanie
 */
public class CategoryIO implements Serializable {

    private String driver;
    private String url;
    private String user;
    private String pass;

    private boolean loadClass() {
        //Load whatever preloaded driver from SQL properties file.
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void loadSQL() {
        ResourceBundle sql = ResourceBundle.getBundle("asia.uap.sql.SQL");
        this.url = sql.getString("url");
        this.user = sql.getString("user");
        this.pass = sql.getString("password");
        this.driver = sql.getString("driver");
    }

    public CategoryIO(String driver, String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.driver = driver;
    }

    public CategoryIO() {
        loadSQL();
        loadClass();
    }

    public Connection getConnection() {
        Connection conn = null; // Initialize the Connection variable

        try {
            conn = DriverManager.getConnection(url, user, pass); // Establish the connection
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("CANNOT CONNECT");
            // Optionally handle the error or set conn to null
        }

        return conn; // Return the connection object (could be null if failed)
    }

    public ArrayList<Listing> orderListing(String order, String column) throws SQLException {
        Connection conn = null;
        ArrayList<Listing> listingList = new ArrayList<>();
        try {
            conn = getConnection();
            String sql = "SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l LEFT JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id LEFT JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id where l.isarchive = 0 group by l.listing_id order by " + column + " " + order;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("listing_id");
                String title = rs.getString("title");
                byte[] image = rs.getBytes("image");
                int ownerId = rs.getInt("owner_id");
                double price = rs.getDouble("price");
                String bio = rs.getString("bio");
                String status = rs.getString("listing_status");
                Timestamp dateTime = rs.getTimestamp("date_time");
                int stock = rs.getInt("stock");
                String ownername = rs.getString("username");
                String tags = rs.getString("tags");

                Listing listing = new Listing();
                listing.setId(id);
                listing.setTitle(title);
                listing.setPhoto(image);
                listing.setPrice(price);
                listing.setOwnerID(ownerId);
                listing.setDesc(bio);
                listing.setStatus(status);
                listing.setDateTime(dateTime);
                listing.setStock(stock);
                listing.setOwnername(ownername);
                listing.setTags(tags);

                listingList.add(listing);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return listingList;
    }

    public ArrayList<Listing> orderSearchedListing(String order, String query, String column) throws SQLException {
        Connection conn = null;
        ArrayList<Listing> listingList = new ArrayList<>();
        try {
            conn = getConnection();
            String sql = "SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l LEFT JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id LEFT JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id where (l.title LIKE ? OR u.username LIKE ?) and l.isarchive = 0 group by l.listing_id order by " + column + " " + order;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("listing_id");
                String title = rs.getString("title");
                byte[] image = rs.getBytes("image");
                int ownerId = rs.getInt("owner_id");
                double price = rs.getDouble("price");
                String bio = rs.getString("bio");
                String status = rs.getString("listing_status");
                Timestamp dateTime = rs.getTimestamp("date_time");
                int stock = rs.getInt("stock");
                String ownername = rs.getString("username");
                String tags = rs.getString("tags");

                Listing listing = new Listing();
                listing.setId(id);
                listing.setTitle(title);
                listing.setPhoto(image);
                listing.setPrice(price);
                listing.setOwnerID(ownerId);
                listing.setDesc(bio);
                listing.setStatus(status);
                listing.setDateTime(dateTime);
                listing.setStock(stock);
                listing.setOwnername(ownername);
                listing.setTags(tags);

                listingList.add(listing);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return listingList;
    }

    public ArrayList<Listing> orderListingByAlphabetical(String order) throws SQLException {
        Connection conn = null;
        ArrayList<Listing> listingList = new ArrayList<>();
        try {
            conn = getConnection();
            String sql = "SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l LEFT JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id LEFT JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id where l.isarchive = 0 group by l.listing_id order by l.title " + order;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("listing_id");
                String title = rs.getString("title");
                byte[] image = rs.getBytes("image");
                int ownerId = rs.getInt("owner_id");
                double price = rs.getDouble("price");
                String bio = rs.getString("bio");
                String status = rs.getString("listing_status");
                Timestamp dateTime = rs.getTimestamp("date_time");
                int stock = rs.getInt("stock");
                String ownername = rs.getString("username");
                String tags = rs.getString("tags");

                Listing listing = new Listing();
                listing.setId(id);
                listing.setTitle(title);
                listing.setPhoto(image);
                listing.setPrice(price);
                listing.setOwnerID(ownerId);
                listing.setDesc(bio);
                listing.setStatus(status);
                listing.setDateTime(dateTime);
                listing.setStock(stock);
                listingList.add(listing);
                listing.setOwnername(ownername);
                listing.setTags(tags);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return listingList;
    }

    public ArrayList<Listing> orderSearchedListingByAlphabetical(String query, String order) throws SQLException {
        Connection conn = null;
        ArrayList<Listing> listingList = new ArrayList<>();
        try {
            conn = getConnection();
            String sql = "SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l LEFT JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id LEFT JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id where (l.title LIKE ? OR u.username LIKE ?) and l.isarchive = 0 group by l.listing_id order by l.title " + order;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("listing_id");
                String title = rs.getString("title");
                byte[] image = rs.getBytes("image");
                int ownerId = rs.getInt("owner_id");
                double price = rs.getDouble("price");
                String bio = rs.getString("bio");
                String status = rs.getString("listing_status");
                Timestamp dateTime = rs.getTimestamp("date_time");
                int stock = rs.getInt("stock");
                String ownername = rs.getString("username");
                String tags = rs.getString("tags");

                Listing listing = new Listing();
                listing.setId(id);
                listing.setTitle(title);
                listing.setPhoto(image);
                listing.setPrice(price);
                listing.setOwnerID(ownerId);
                listing.setDesc(bio);
                listing.setStatus(status);
                listing.setDateTime(dateTime);
                listing.setStock(stock);
                listing.setOwnername(ownername);
                listing.setTags(tags);

                listingList.add(listing);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return listingList;
    }

    public ArrayList<Listing> getListingsByTag(String[] tagname) throws SQLException {
        Connection conn = null;
        ArrayList<Listing> listingList = new ArrayList<>();
        Set<Integer> uniqueListingIds = new HashSet<>();
        try {
            conn = getConnection();
            StringBuilder sql = new StringBuilder("SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l INNER JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id INNER JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id where l.isarchive = 0 group by l.listing_id HAVING SUM(t.tag_name in (");

            for (int i = 0; i < tagname.length; i++) {
                sql.append("?");
                if (i < tagname.length - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")) > 0");

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < tagname.length; i++) {
                ps.setString(i + 1, tagname[i]);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("listing_id");
                if (uniqueListingIds.contains(id)) {
                    continue;
                }
                uniqueListingIds.add(id);
                String title = rs.getString("title");
                byte[] image = rs.getBytes("image");
                int ownerId = rs.getInt("owner_id");
                double price = rs.getDouble("price");
                String bio = rs.getString("bio");
                String status = rs.getString("listing_status");
                Timestamp dateTime = rs.getTimestamp("date_time");
                int stock = rs.getInt("stock");
                String ownername = rs.getString("username");
                String tags = rs.getString("tags");

                Listing listing = new Listing();
                listing.setId(id);
                listing.setTitle(title);
                listing.setPhoto(image);
                listing.setPrice(price);
                listing.setOwnerID(ownerId);
                listing.setDesc(bio);
                listing.setStatus(status);
                listing.setDateTime(dateTime);
                listing.setStock(stock);
                listing.setOwnername(ownername);
                listing.setTags(tags);

                listingList.add(listing);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return listingList;
    }

    public ArrayList<Listing> getSearchedListingsByTag(String query, String[] tagname) throws SQLException {
        Connection conn = null;

        ArrayList<Listing> listingList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            StringBuilder sql = new StringBuilder("SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l INNER JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id INNER JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id where l.isarchive = 0 and (l.title LIKE ? OR u.username LIKE ?) group by l.listing_id HAVING SUM(t.tag_name in (");

            for (int i = 0; i < tagname.length; i++) {
                sql.append("?");
                if (i < tagname.length - 1) {
                    sql.append(", ");
                }
            }

            sql.append(")) > 0");

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            for (int i = 0; i < tagname.length; i++) {
                ps.setString(i + 3, tagname[i]);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("listing_id");
                String title = rs.getString("title");
                byte[] image = rs.getBytes("image");
                int ownerId = rs.getInt("owner_id");
                double price = rs.getDouble("price");
                String bio = rs.getString("bio");
                String status = rs.getString("listing_status");
                Timestamp dateTime = rs.getTimestamp("date_time");
                int stock = rs.getInt("stock");
                String ownername = rs.getString("username");
                String tags = rs.getString("tags");

                Listing listing = new Listing();
                listing.setId(id);
                listing.setTitle(title);
                listing.setPhoto(image);
                listing.setPrice(price);
                listing.setOwnerID(ownerId);
                listing.setDesc(bio);
                listing.setStatus(status);
                listing.setDateTime(dateTime);
                listing.setStock(stock);
                listing.setOwnername(ownername);
                listing.setTags(tags);

                listingList.add(listing);


            }
        } catch(SQLException ex){
            ex.printStackTrace();
            System.out.println("SQL EXCEPTION with getting searched listings");
        }
            finally {
            if (conn != null) {
                conn.close();
            }
        }


        return listingList;
    }
}
