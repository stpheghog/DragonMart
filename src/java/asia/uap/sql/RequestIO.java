/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.sql;

import asia.uap.beans.DeleteCommentRequest;
import asia.uap.beans.DeleteListingRequest;

import asia.uap.beans.Listing;

import asia.uap.beans.UserDeleteRequest;
import asia.uap.beans.UserReport;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 *
 * @author blonyagoncillo
 */
public class RequestIO implements Serializable {

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

    public RequestIO(String driver, String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.driver = driver;
    }

    public RequestIO() {
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


    // Approve a request
    public void approveRequest(int requestId) throws SQLException {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            String sql = "UPDATE profile_update_requests SET status = 'APPROVED' WHERE request_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, requestId);
            ps.executeUpdate();
            conn.commit(); // Commit the transaction

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex; //rethrow exception
        } finally {
            if (conn != null) {
                conn.close(); // 
            }
        }
    }

    public void denyRequest(int requestId) throws SQLException {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            String sql = "UPDATE profile_update_requests SET status = 'DENIED' WHERE request_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, requestId);
            ps.executeUpdate();
            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex; //rethrow exception
        } finally {
            if (conn != null) {
                conn.close(); // 
            }
        }
    }


    public void addDeleteRequest(int userId, int adminId) throws SQLException {
        Connection conn = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            String sql = "INSERT INTO delete_user_requests (user_id, admin_id, status) VALUES (?, ?, 'PENDING')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, adminId);

            ps.executeUpdate();
            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback(); // Rollback if there was an error
            }
            ex.printStackTrace();
            throw ex; // Rethrow exception
        } finally {
            if (conn != null) {
                conn.close(); // Close the connection
            }
        }
    }

    public void approveDeleteRequest(int requestId, int userId) throws SQLException {
        Connection conn = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            // Approve the delete request
            String approveSql = "UPDATE delete_user_requests SET status = 'APPROVED' WHERE request_id = ?";
            PreparedStatement approvePs = conn.prepareStatement(approveSql);
            approvePs.setInt(1, requestId);
            approvePs.executeUpdate();

            // Archive the user
            String archiveSql = "UPDATE mart_user SET isarchive = 1 WHERE user_id = ?";
            PreparedStatement archivePs = conn.prepareStatement(archiveSql);
            archivePs.setInt(1, userId);
            archivePs.executeUpdate();

            // 3. Archive all listings by setting isarchive to 1 for the user's listings
            String archiveListingsSql = "UPDATE mart_listing SET isarchive = 1 WHERE owner_id = ?";
            PreparedStatement psArchiveListings = conn.prepareStatement(archiveListingsSql);
            psArchiveListings.setInt(1, userId);
            psArchiveListings.executeUpdate();

            conn.commit(); // Commit the transaction

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback(); // Rollback if there was an error
            }
            ex.printStackTrace();
            throw ex; // Rethrow exception
        } finally {
            if (conn != null) {
                conn.close(); // Close the connection
            }
        }
    }

    public void denyDeleteRequest(int requestId) throws SQLException {
        Connection conn = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            // Deny the delete request
            String sql = "UPDATE delete_user_requests SET status = 'DENIED' WHERE request_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, requestId);
            ps.executeUpdate();

            conn.commit(); // Commit the transaction

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback(); // Rollback if there was an error
            }
            ex.printStackTrace();
            throw ex; // Rethrow exception
        } finally {
            if (conn != null) {
                conn.close(); // Close the connection
            }
        }
    }

    public ArrayList<UserDeleteRequest> getPendingDeleteRequests(int currentAdminId) throws SQLException {
        ArrayList<UserDeleteRequest> requests = new ArrayList<>();

        Connection conn = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction
            String sql = "SELECT * FROM delete_user_requests WHERE status = 'PENDING' AND admin_id != ? ORDER BY created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, currentAdminId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserDeleteRequest request = new UserDeleteRequest();
                request.setRequest_id(rs.getInt("request_id"));
                request.setAdmin_id(rs.getInt("admin_id"));
                request.setUser_id(rs.getInt("user_id"));

                requests.add(request);
            }

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex; //rethrow exception
        } finally {
            if (conn != null) {
                conn.close(); // 
            }
        }
        return requests;
    }

    // Approve delete requests based on listingIds from DeleteListingRequest bean
    // Add delete listing request
    public boolean addDeleteListingRequest(DeleteListingRequest request) throws SQLException {
        Connection conn = null;
        boolean success = false;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            String sql = "INSERT INTO delete_listing_requests (listing_id, admin_id, status) VALUES (?, ?, 'PENDING')";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (String listingId : request.getListingIds()) {
                stmt.setInt(1, Integer.parseInt(listingId));
                stmt.setInt(2, request.getAdminId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                } else {
                    success = false;
                    break; // If any insertion fails, exit early
                }
            }

            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback(); // Rollback in case of failure
            }
            throw ex; // Rethrow exception to be handled at the caller level
        } finally {
            if (conn != null) {
                conn.close(); // Close the connection
            }
        }

        return success;
    }

    // Approve delete listing request
    public boolean approveDeleteListingRequest(int requestID, int listingID) throws SQLException {
        Connection conn = null;
        boolean success = false;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            String deleteRequestSql = "UPDATE mart_listing SET isarchive = TRUE WHERE listing_id = ?";
            String approveSql = "UPDATE delete_listing_requests SET status = 'APPROVED' WHERE request_id = ?";

            PreparedStatement approveStmt = conn.prepareStatement(approveSql);
            PreparedStatement deleteRequestStmt = conn.prepareStatement(deleteRequestSql);

            // Approve the request
            approveStmt.setInt(1, requestID);
            int rowsAffected = approveStmt.executeUpdate();

            if (rowsAffected > 0) {
                // If approved, remove
                deleteRequestStmt.setInt(1, listingID);
                deleteRequestStmt.executeUpdate();
                success = true;
            } else {
                success = false;
            }
            

            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback(); // Rollback in case of failure
            }
            throw ex; // Rethrow exception to be handled at the caller level
        } finally {
            if (conn != null) {
                conn.close(); // Close the connection
            }
        }

        return success;
    }

    // Deny delete listing request
    public boolean denyDeleteListingRequest(int requestID) throws SQLException {
        Connection conn = null;
        boolean success = false;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            String sql = "UPDATE delete_listing_requests SET status = 'DENIED' WHERE request_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, requestID);
            stmt.executeUpdate();

            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback(); // Rollback in case of failure
            }
            throw ex; // Rethrow exception to be handled at the caller level
        } finally {
            if (conn != null) {
                conn.close(); // Close the connection
            }
        }

        return success;
    }

    // Get pending delete requests, excluding the current admin
    public ArrayList<DeleteListingRequest> getDeleteListingRequests(int currentAdminId) throws SQLException {
        ArrayList<DeleteListingRequest> requests = new ArrayList<>();
        Connection conn = null;

        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            String sql = "SELECT * FROM delete_listing_requests WHERE status = 'PENDING' AND admin_id != ? ORDER BY created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, currentAdminId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ArrayList<String> listingIds = new ArrayList<>();
                listingIds.add(rs.getString("listing_id")); // Add listingId to the list
                DeleteListingRequest request = new DeleteListingRequest();
                request.setRequestId(rs.getInt("request_id"));
                request.setAdminId(rs.getInt("admin_id"));
                request.setStatus(rs.getString("status"));
                request.setListingIds(listingIds); // Set the listing IDs

                requests.add(request);
            }

            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback(); // Rollback in case of failure
            }
            throw ex; // Rethrow exception to be handled at the caller level
        } finally {
            if (conn != null) {
                conn.close(); // Close the connection
            }
        }

        return requests;
    }

    public boolean addCommentDeleteRequest(int commentId, int adminId) throws SQLException {
        Connection conn = null;
        boolean success = false;

        try {
            // Establish connection to the database
            conn = DriverManager.getConnection(url, user, pass);

            String insertSql = "INSERT INTO delete_comment_requests (comment_id, admin_id, status) VALUES (?, ?, 'PENDING')";

            PreparedStatement stmt = conn.prepareStatement(insertSql);
            stmt.setInt(1, commentId);
            stmt.setInt(2, adminId);

            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0; // If insertion was successful

        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }

    public boolean approveDeleteCommentRequest(int requestId, int commentId) throws SQLException {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start a transaction

            String approveSql = "UPDATE delete_comment_requests SET status = 'APPROVED' WHERE request_id = ?";
            PreparedStatement approveStmt = conn.prepareStatement(approveSql);
            approveStmt.setInt(1, requestId);

            int rowsDeleted = approveStmt.executeUpdate();

            if (rowsDeleted > 0) {
                String deleteCommentSql = "UPDATE listing_comment SET isarchive=1 WHERE comment_id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteCommentSql);
                deleteStmt.setInt(1, commentId);
                deleteStmt.executeUpdate();
                conn.commit(); // Commit the transaction
                success = true;
            } else {
                conn.rollback(); // Rollback if comment archiving failed
            }

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback(); // Rollback in case of failure
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }

    public boolean denyDeleteCommentRequest(int requestId) throws SQLException {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String denySql = "UPDATE delete_comment_requests SET status = 'DENIED' WHERE request_id = ?";
            PreparedStatement stmt = conn.prepareStatement(denySql);
            stmt.setInt(1, requestId);

            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0; // If update was successful

        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }

    public ArrayList<DeleteCommentRequest> getAllCommentDeleteRequests(int adminID) throws SQLException {
        Connection conn = null;
        ArrayList<DeleteCommentRequest> requests = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT *, lc.content AS comment_content FROM delete_comment_requests dcr LEFT JOIN listing_comment lc ON dcr.comment_id = lc.comment_id WHERE dcr.status = 'PENDING' AND dcr.admin_id != ? ORDER BY dcr.created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adminID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                int commentId = rs.getInt("comment_id");
                String commentContent = rs.getString("comment_content");
                int adminId = rs.getInt("admin_id");
                String status = rs.getString("status");

                DeleteCommentRequest request = new DeleteCommentRequest();
                request.setAdmin_id(adminId);
                request.setContent(commentContent);
                request.setRequest_id(requestId);
                request.setComment_id(commentId);
                request.setStatus(status);
                requests.add(request);
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return requests;
    }


    
    public void updateListing(Listing editedListing) throws SQLException {
        Connection conn = null;

        try{
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

                // Update the listing in MART_LISTING table
                String updateSql = "UPDATE mart_listing SET title = ?, bio = ? WHERE listing_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, editedListing.getTitle());
                updateStmt.setString(2, editedListing.getDesc());
                updateStmt.setInt(3, editedListing.getId());
                updateStmt.executeUpdate();

            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
    
    public void approveEditListingRequest(int requestId) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            // Update the status of the request to "APPROVED"
            String sql = "UPDATE edit_listing_requests SET status = 'APPROVED' WHERE request_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, requestId);
            stmt.executeUpdate();

            // Fetch the details of the edit request (listing details)
            String selectSql = "SELECT * FROM edit_listing_requests WHERE request_id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, requestId);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int listingId = rs.getInt("listing_id");
                String newTitle = rs.getString("title");
                String newDescription = rs.getString("bio");

                // Update the listing in MART_LISTING table
                String updateSql = "UPDATE mart_listing SET title = ?, bio = ? WHERE listing_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, newTitle);
                updateStmt.setString(2, newDescription);

                updateStmt.setInt(3, listingId);
                updateStmt.executeUpdate();
            }

            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void denyEditListingRequest(int requestId) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start transaction

            // Deny the request by updating its status
            String sql = "UPDATE edit_listing_requests SET status = 'DENIED' WHERE request_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, requestId);
            stmt.executeUpdate();

            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean addUserReport(int commentId, int userId) throws SQLException {
        Connection conn = null;
        boolean success = false;

        try {
            // Establish connection to the database
            conn = DriverManager.getConnection(url, user, pass);

            String insertSql = "INSERT INTO user_report (comment_id, user_id, status) VALUES (?, ?, 'PENDING')";

            PreparedStatement stmt = conn.prepareStatement(insertSql);
            stmt.setInt(1, commentId);
            stmt.setInt(2, userId);

            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0; // If insertion was successful

        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }

    public boolean approveUserReport(int requestId, int commentId) throws SQLException {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false); // Start a transaction

            String approveSql = "UPDATE user_report SET status = 'APPROVED' WHERE request_id = ?";
            PreparedStatement approveStmt = conn.prepareStatement(approveSql);
            approveStmt.setInt(1, requestId);

            int rowsDeleted = approveStmt.executeUpdate();

            if (rowsDeleted > 0) {
                String deleteCommentSql = "UPDATE listing_comment SET isarchive=1 WHERE comment_id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteCommentSql);
                deleteStmt.setInt(1, commentId);
                deleteStmt.executeUpdate();
                conn.commit(); // Commit the transaction
                success = true;
            } else {
                conn.rollback(); // Rollback if comment archiving failed
            }

        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback(); // Rollback in case of failure
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }

    public boolean denyUserReport(int requestId) throws SQLException {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String denySql = "UPDATE user_report SET status = 'DENIED' WHERE request_id = ?";
            PreparedStatement stmt = conn.prepareStatement(denySql);
            stmt.setInt(1, requestId);

            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0; // If update was successful

        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return success;
    }

    public ArrayList<UserReport> getAllUserReports() throws SQLException {
        Connection conn = null;
        ArrayList<UserReport> requests = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT *, lc.content AS comment_content FROM user_report ur LEFT JOIN listing_comment lc ON ur.comment_id = lc.comment_id WHERE ur.status = 'PENDING' ORDER BY ur.created_at DESC;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                int commentId = rs.getInt("comment_id");
                int userId = rs.getInt("user_id");
                String status = rs.getString("status");
                String content = rs.getString("comment_content");

                UserReport report = new UserReport();
                report.setUser_id(userId);
                report.setRequest_id(requestId);
                report.setComment_id(commentId);
                report.setStatus(status);
                report.setContent(content);
                requests.add(report);
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return requests;
    }

}
