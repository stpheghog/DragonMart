/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.sql;

import asia.uap.beans.Comment;
import asia.uap.beans.Listing;
import asia.uap.beans.Tag;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;

/**
 *
 * @author blonyagoncillo
 */
public class ListingIO implements Serializable {

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

    public ListingIO(String driver, String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.driver = driver;
    }

    public ListingIO() {
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


    public int addListing(Listing row) throws SQLException {
        int generatedID = 0;
        Connection conn = null;

        try {
            //This allows us to execute multiple 
            //things for a single transaction.
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);
            String preparedSQL = "insert into mart_listing (title, image, price, bio, owner_id, listing_status, isarchive,date_time, stock) values (?, ?, ?, ?, ?, ?, ?, NOW(), ?)";
            PreparedStatement ps = conn.prepareStatement(preparedSQL, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, row.getTitle());
            ps.setBytes(2, row.getPhoto());
            ps.setDouble(3, row.getPrice());
            ps.setString(4, row.getDesc());
            ps.setInt(5, row.getOwnerID());
            ps.setString(6, row.getStatus());
            ps.setBoolean(7, row.isArchived());
            ps.setInt(8, row.getStock());

            ps.executeUpdate(); // Execute the update

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedID = generatedKeys.getInt(1); // Get the generated listing_id
                }
            }
            conn.commit();

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }

            throw ex; //rethrow so caller can handle the error
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return generatedID;
    }

    public ArrayList<Listing> getAllListings() throws SQLException {
        Connection conn = null;

        ArrayList<Listing> listingList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l LEFT JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id LEFT JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id WHERE l.isarchive = 0 AND l.listing_status = ? group by listing_id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "Available");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("listing_id");
                String title = rs.getString("title");
                byte[] image = rs.getBytes("image");
                int ownerId = rs.getInt("owner_id");
                String ownername = rs.getString("username");
                double price = rs.getDouble("price");
                String bio = rs.getString("bio");
                String status = rs.getString("listing_status");
                boolean isArchived = rs.getBoolean("isarchive");
                Timestamp dateTime = rs.getTimestamp("date_time");
                int stock = rs.getInt("stock");
                String tags = rs.getString("tags");

                Listing listing = new Listing();
                listing.setId(id);
                listing.setTitle(title);
                listing.setPhoto(image);
                listing.setPrice(price);
                listing.setOwnerID(ownerId);
                listing.setOwnername(ownername);
                listing.setDesc(bio);
                listing.setStatus(status);
                listing.setIsArchived(isArchived);
                listing.setDateTime(dateTime);
                listing.setStock(stock);
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

    public Listing getListingByID(int listingid) throws SQLException {
        Connection conn = null;

        Listing listing = new Listing();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l LEFT JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id LEFT JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id where l.listing_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, listingid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
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


            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }


        return listing;
    }

    public ArrayList<Listing> getOwnerListings(int ownerid) throws SQLException {
        Connection conn = null;

        ArrayList<Listing> listingList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l LEFT JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id LEFT JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id where l.owner_id = ? AND l.isarchive = ? group by l.listing_id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ownerid);
            ps.setBoolean(2, false);
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

    public boolean editListing(Listing listing) throws SQLException, NumberFormatException {
        boolean success = false;
        Connection conn = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            String preparedSQL = "UPDATE mart_listing SET title = ?, price = ?, bio = ?, listing_status = ?, image = ?, stock = ? WHERE listing_id = ?";
            PreparedStatement ps = conn.prepareStatement(preparedSQL);

            // setting updated parameters in the prepared statement
            ps.setString(1, listing.getTitle());
            ps.setDouble(2, listing.getPrice());
            ps.setString(3, listing.getDesc());
            ps.setString(4, listing.getStatus());
            ps.setBytes(5, listing.getPhoto());
            ps.setInt(6, listing.getStock());
            ps.setInt(7, listing.getId());

            ps.executeUpdate();
            conn.commit();
            success = true; // update successful
        } catch (SQLException ex) {
            System.out.println("could not update listing");
            if (conn != null) {
                conn.rollback();
            }
            throw ex; //rethrow exception
        } finally {
            if (conn != null) {
                conn.close(); // 
            }

        }

        return success;
    }

    //removes listings based on their ids passed through the checkbox
    public boolean removeListing(ArrayList<String> listingIDs) throws SQLException, NumberFormatException {
        boolean success = false;

        Connection conn = null;

        try {
            //This allows us to execute multiple 
            //things for a single transaction.
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);
            // Updated SQL to match the MART_USER table
            String preparedSQL = "UPDATE mart_listing SET isarchive = 1 where listing_id = ?";
            PreparedStatement ps = conn.prepareStatement(preparedSQL);

            for (String i : listingIDs) {
                try {
                    ps.setInt(1, Integer.parseInt(i));
                    ps.addBatch(); // Execute the update
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    System.out.println("Invalid listing ID: " + i);
                    throw ex;
                }
            }

            ps.executeBatch();

            conn.commit();
            success = true;
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }

            throw ex; //rethrow so caller can handle the error
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }

    //adds tags of a specific listing based on listing id and tag names
    public int addTags(int listingid, ArrayList<String> tags) throws SQLException {
        int rows = 0;
        Connection conn = null;

        try {
            //This allows us to execute multiple 
            //things for a single transaction.
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);

            // insert tags if not already present
            String tagInsertSQL = "INSERT INTO listing_tag (tag_name) VALUES (?) ON DUPLICATE KEY UPDATE tag_id=LAST_INSERT_ID(tag_id)";
            PreparedStatement tagPs = conn.prepareStatement(tagInsertSQL, PreparedStatement.RETURN_GENERATED_KEYS);

            // Prepared statement to insert into junction table
            String junctionInsertSQL = "INSERT INTO listing_tag_junction (listing_id, tag_id) VALUES (?, ?)";
            PreparedStatement junctionPs = conn.prepareStatement(junctionInsertSQL);

            for (String tag : tags) {
                tagPs.setString(1, tag);
                tagPs.executeUpdate();

                // gets generated or existing tag_id
                ResultSet rs = tagPs.getGeneratedKeys();
                if (rs.next()) {
                    int tagId = rs.getInt(1);

                    // Insert into junction table
                    junctionPs.setInt(1, listingid);
                    junctionPs.setInt(2, tagId);
                    rows += junctionPs.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }

            throw ex; //rethrow so caller can handle the error
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return rows;
    }

    //gets all tag names associated with a listing
    public ArrayList<String> getTagsForListing(int listingId) throws SQLException {
        ArrayList<String> tags = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);
            String sql = "SELECT t.tag_name FROM listing_tag t "
                    + "JOIN listing_tag_junction jt ON t.tag_id = jt.tag_id "
                    + "WHERE jt.listing_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, listingId);

            rs = ps.executeQuery();
            conn.commit();
            while (rs.next()) {
                tags.add(rs.getString("tag_name"));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return tags;
    }

    //updates tags junction table using listing id and the ids of the its tags
    public boolean updateTagsJunction(int listingid, ArrayList<Integer> tagIDs) throws SQLException {
        Connection conn = null;
        boolean success = false;
        PreparedStatement psDelete = null;
        PreparedStatement psInsert = null;

        try {
            //This allows us to execute multiple 
            //things for a single transaction.
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);

            String deleteSQL = "DELETE FROM listing_tag_junction where listing_id = ?";
            psDelete = conn.prepareStatement(deleteSQL);
            psDelete.setInt(1, listingid);
            psDelete.executeUpdate();

            String insertSQL = "INSERT INTO listing_tag_junction (listing_id, tag_id) VALUES (?, ?)";
            psInsert = conn.prepareStatement(insertSQL);

            for (int tag : tagIDs) {

                psInsert.setInt(1, listingid);
                psInsert.setInt(2, tag);
                psInsert.addBatch(); //adds to batch instead of executing per tagID
            }

            psInsert.executeBatch(); //executes all together
            conn.commit();

            success = true;
        } catch (SQLException ex) {
            success = false;
            if (conn != null) {
                conn.rollback();
            }

            throw ex; //rethrow so caller can handle the error
        } finally {
            if (psDelete != null) {
                psDelete.close();
            }
            if (psInsert != null) {
                psInsert.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }

    public int checkAndInsertCustomTag(String tagName) throws SQLException {
        int tagId = -1; // Default value indicating an error or non-existence.
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            // Check if the tag already exists
            String checkSQL = "SELECT tag_id FROM listing_tag WHERE tag_name = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSQL);
            checkPs.setString(1, tagName);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // Tag already exists, get its ID
                tagId = rs.getInt("tag_id");

            } else {
                // Tag does not exist, insert it and get the generated ID
                String insertSQL = "INSERT INTO listing_tag (tag_name) VALUES (?)";
                PreparedStatement insertPs = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                insertPs.setString(1, tagName);
                insertPs.executeUpdate();

                ResultSet generatedKeys = insertPs.getGeneratedKeys();
                if (generatedKeys.next()) {
                    tagId = generatedKeys.getInt(1); // Get the new tag_id

                }
            }
        } catch (SQLException ex) {
            throw ex; // re-throws to let the caller handle it
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return tagId;
    }

    public int addComment(int listingid, String content, int userid) throws SQLException {
        int rows = 0;
        Connection conn = null;

        try {
            //This allows us to execute multiple 
            //things for a single transaction.
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);

            // insert comment 
            String commentInsertSQL = "INSERT INTO listing_comment (content, publisher, isarchive, listing_id) VALUES (?, ?, ?, ?) ";
            PreparedStatement commentPS = conn.prepareStatement(commentInsertSQL);

            //comment insert
            commentPS.setString(1, content);
            commentPS.setInt(2, userid);
            commentPS.setBoolean(3, false);
            commentPS.setInt(4, listingid);
            commentPS.executeUpdate();

            conn.commit();

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }

            throw ex; //rethrow so caller can handle the error
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return rows;
    }

    public ArrayList<Comment> getListingComments(int listingId) throws SQLException {
        ArrayList<Comment> comments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);
            String sql = "SELECT c.comment_id, c.content, c.publisher, c.listing_id, m.username " +
                     "FROM listing_comment c " +
                     "JOIN mart_user m ON c.publisher = m.user_id " +
                     "WHERE c.listing_id = ? AND c.isarchive = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, listingId);
            ps.setBoolean(2, false);
            rs = ps.executeQuery();
            conn.commit();

            while (rs.next()) {
                int commentId = rs.getInt("comment_id");
                String content = rs.getString("content");
                int publisherId = rs.getInt("publisher");
                String username = rs.getString("username");
                int listingid = rs.getInt("listing_id");

                Comment comment = new Comment();
                comment.setId(commentId);
                comment.setComment(content);
                comment.setUserid(publisherId);
                comment.setUsername(username);
                comment.setListingid(listingid);
                comments.add(comment);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return comments;
    }

    public void archiveCommentByID(int id) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pass);

        try {

            String sql = "UPDATE listing_comment SET isarchive = ? WHERE comment_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
            } else {
                System.out.println("no comment found with ID " + id + ".");
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean updateStock(int stock, String status, int id) throws SQLException, NumberFormatException {
        boolean success = false;
        Connection conn = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            String preparedSQL = "UPDATE mart_listing SET stock = ?, listing_status = ? WHERE listing_id = ?";
            PreparedStatement ps = conn.prepareStatement(preparedSQL);

            // setting updated parameters in the prepared statement
            ps.setInt(1, stock);
            ps.setString(2, status);
            ps.setInt(3, id);

            ps.executeUpdate();
            conn.commit();
            success = true; // update successful
        } catch (SQLException ex) {
            System.out.println("could not update listing");
            if (conn != null) {
                conn.rollback();
            }
            throw ex; //rethrow exception
        } finally {
            if (conn != null) {
                conn.close(); // 
            }

        }

        return success;
    }

    public ArrayList<Listing> getAllSearchedListings(String query) throws SQLException {
        Connection conn = null;
        ArrayList<Listing> listingList = new ArrayList<>();
        try {
            conn = getConnection();

            String sql = "SELECT l.listing_id, l.title, l.image, l.price, l.bio, l.owner_id, l.listing_status, l.isarchive, l.date_time, l.stock, GROUP_CONCAT(t.tag_name) AS tags, u.username FROM mart_listing l LEFT JOIN listing_tag_junction ltj ON l.listing_id = ltj.listing_id LEFT JOIN listing_tag t ON ltj.tag_id = t.tag_id LEFT JOIN mart_user u ON l.owner_id = u.user_id where l.isarchive = 0 AND (title LIKE ? OR u.username LIKE ?) group by listing_id";
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

    public ArrayList<Tag> getAllTags() throws SQLException {
        Connection conn = null;

        ArrayList<Tag> tagList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "select distinct lt.* from listing_tag lt inner join listing_tag_junction ltj on lt.tag_id = ltj.tag_id inner join mart_listing l on ltj.listing_id = l.listing_id where l.isarchive = 0";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("tag_id");
                String name = rs.getString("tag_name");

                Tag tag = new Tag();
                tag.setId(id);
                tag.setName(name);

                if (!tagList.contains(tag)){
                tagList.add(tag);
                }

            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return tagList;
    }

}
