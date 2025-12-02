/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.sql;

import asia.uap.beans.Listing;
import asia.uap.beans.Transaction;
import asia.uap.beans.User;
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
 * @author Stephanie
 */
public class TransactionIO implements Serializable {

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

    public TransactionIO(String driver, String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.driver = driver;
    }

    public TransactionIO() {
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

    //INSERTS
    public int addTransaction(int listing, int buyer) throws SQLException {
        int generatedID = 0;
        Connection conn = null;

        try {
            //This allows us to execute multiple 
            //things for a single transaction.
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);
            String preparedSQL = "INSERT INTO transactions (listing_id, buyer_id, transaction_status, ispaid, isreceived) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(preparedSQL, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setInt(1, listing);
            ps.setInt(2, buyer);
            ps.setString(3, "Requested");
            ps.setBoolean(4, false);
            ps.setBoolean(5, false);

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

    //SELECTS
    public ArrayList<Transaction> getUserPurchases(int currentUserId) throws SQLException {
        Connection conn = null;

        ArrayList<Transaction> transList = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT t.transaction_id, t.listing_id, t.buyer_id, t.transaction_status, t.ispaid, t.isreceived, buyer.username AS buyer_username, l.owner_id AS owner_id, l.title AS listing_name, owner.username AS owner_username FROM transactions t JOIN mart_user buyer ON t.buyer_id = buyer.user_id JOIN mart_listing l ON t.listing_id = l.listing_id JOIN mart_user owner ON l.owner_id = owner.user_id WHERE t.buyer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("transaction_id");
                int listing_id = rs.getInt("listing_id");
                int buyer_id = rs.getInt("buyer_id");
                int owner_id = rs.getInt("owner_id");
                boolean isPaid = rs.getBoolean("ispaid");
                boolean isReceived = rs.getBoolean("isreceived");
                String buyer_name = rs.getString("buyer_username");
                String owner_name = rs.getString("owner_username");
                String listing_name = rs.getString("listing_name");
                String transaction_status = rs.getString("transaction_status");
                Transaction transaction = new Transaction();
                transaction.setId(id);
                transaction.setListingId(listing_id);
                transaction.setBuyerId(buyer_id);
                transaction.setOwnerId(owner_id);
                transaction.setBuyerName(buyer_name);
                transaction.setOwnerName(owner_name);
                transaction.setIsReceived(isReceived);
                transaction.setIsPaid(isPaid);
                transaction.setStatus(transaction_status);
                transaction.setListingName(listing_name);

                transList.add(transaction);


            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }


        return transList;
    }
    
    public ArrayList<Transaction> getUserSales(int currentUserId) throws SQLException {
        Connection conn = null;

        ArrayList<Transaction> transList = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT t.transaction_id, t.listing_id, t.buyer_id, t.transaction_status, t.ispaid, t.isreceived, buyer.username AS buyer_username, l.owner_id AS owner_id, l.title AS listing_name, owner.username AS owner_username FROM transactions t JOIN mart_user buyer ON t.buyer_id = buyer.user_id JOIN mart_listing l ON t.listing_id = l.listing_id JOIN mart_user owner ON l.owner_id = owner.user_id WHERE l.owner_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("transaction_id");
                int listing_id = rs.getInt("listing_id");
                int buyer_id = rs.getInt("buyer_id");
                int owner_id = rs.getInt("owner_id");
                boolean isPaid = rs.getBoolean("ispaid");
                boolean isReceived = rs.getBoolean("isreceived");
                String buyer_name = rs.getString("buyer_username");
                String owner_name = rs.getString("owner_username");
                String listing_name = rs.getString("listing_name");
                String transaction_status = rs.getString("transaction_status");
                Transaction transaction = new Transaction();
                transaction.setId(id);
                transaction.setListingId(listing_id);
                transaction.setBuyerId(buyer_id);
                transaction.setOwnerId(owner_id);
                transaction.setBuyerName(buyer_name);
                transaction.setOwnerName(owner_name);
                transaction.setIsReceived(isReceived);
                transaction.setIsPaid(isPaid);
                transaction.setStatus(transaction_status);
                transaction.setListingName(listing_name);

                transList.add(transaction);

 
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }


        return transList;
    }
    
    public ArrayList<Transaction> getCanceledUserPurchases(int currentUserId) throws SQLException {
        Connection conn = null;

        ArrayList<Transaction> transList = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT t.transaction_id, t.listing_id, t.buyer_id, t.transaction_status, t.ispaid, t.isreceived, buyer.username AS buyer_username, l.owner_id AS owner_id, l.title AS listing_name, owner.username AS owner_username FROM transactions t JOIN mart_user buyer ON t.buyer_id = buyer.user_id JOIN mart_listing l ON t.listing_id = l.listing_id JOIN mart_user owner ON l.owner_id = owner.user_id WHERE t.buyer_id = ? AND t.transaction_status = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ps.setString(2, "Canceled");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("transaction_id");
                int listing_id = rs.getInt("listing_id");
                int buyer_id = rs.getInt("buyer_id");
                int owner_id = rs.getInt("owner_id");
                boolean isPaid = rs.getBoolean("ispaid");
                boolean isReceived = rs.getBoolean("isreceived");
                String buyer_name = rs.getString("buyer_username");
                String owner_name = rs.getString("owner_username");
                String listing_name = rs.getString("listing_name");
                String transaction_status = rs.getString("transaction_status");
                Transaction transaction = new Transaction();
                transaction.setId(id);
                transaction.setListingId(listing_id);
                transaction.setBuyerId(buyer_id);
                transaction.setOwnerId(owner_id);
                transaction.setBuyerName(buyer_name);
                transaction.setOwnerName(owner_name);
                transaction.setIsReceived(isReceived);
                transaction.setIsPaid(isPaid);
                transaction.setStatus(transaction_status);
                transaction.setListingName(listing_name);

                transList.add(transaction);

   
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }


        return transList;
    }
    
    public ArrayList<Transaction> getCanceledUserSales(int currentUserId) throws SQLException {
        Connection conn = null;

        ArrayList<Transaction> transList = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT t.transaction_id, t.listing_id, t.buyer_id, t.transaction_status, t.ispaid, t.isreceived, buyer.username AS buyer_username, l.owner_id AS owner_id, l.title AS listing_name, owner.username AS owner_username FROM transactions t JOIN mart_user buyer ON t.buyer_id = buyer.user_id JOIN mart_listing l ON t.listing_id = l.listing_id JOIN mart_user owner ON l.owner_id = owner.user_id WHERE l.owner_id = ? AND t.transaction_status = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ps.setString(2, "Canceled");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("transaction_id");
                int listing_id = rs.getInt("listing_id");
                int buyer_id = rs.getInt("buyer_id");
                int owner_id = rs.getInt("owner_id");
                boolean isPaid = rs.getBoolean("ispaid");
                boolean isReceived = rs.getBoolean("isreceived");
                String buyer_name = rs.getString("buyer_username");
                String owner_name = rs.getString("owner_username");
                String listing_name = rs.getString("listing_name");
                String transaction_status = rs.getString("transaction_status");
                Transaction transaction = new Transaction();
                transaction.setId(id);
                transaction.setListingId(listing_id);
                transaction.setBuyerId(buyer_id);
                transaction.setOwnerId(owner_id);
                transaction.setBuyerName(buyer_name);
                transaction.setOwnerName(owner_name);
                transaction.setIsReceived(isReceived);
                transaction.setIsPaid(isPaid);
                transaction.setStatus(transaction_status);
                transaction.setListingName(listing_name);

                transList.add(transaction);

              
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        

        return transList;
    }

    public Transaction getTransactionByID(int transactionId) throws SQLException {
        Connection conn = null;

        Transaction transaction = new Transaction();
        try {
            conn = DriverManager.getConnection(url, user, pass);
            
            String sql = "SELECT t.transaction_id, t.listing_id, t.buyer_id, t.transaction_status, t.ispaid, t.isreceived, buyer.username AS buyer_username, l.owner_id AS owner_id, l.title AS listing_name, owner.username AS owner_username FROM transactions t JOIN mart_user buyer ON t.buyer_id = buyer.user_id JOIN mart_listing l ON t.listing_id = l.listing_id JOIN mart_user owner ON l.owner_id = owner.user_id WHERE t.transaction_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("transaction_id");
                int listing_id = rs.getInt("listing_id");
                int buyer_id = rs.getInt("buyer_id");
                int owner_id = rs.getInt("owner_id");
                boolean isPaid = rs.getBoolean("ispaid");
                boolean isReceived = rs.getBoolean("isreceived");
                String buyer_name = rs.getString("buyer_username");
                String owner_name = rs.getString("owner_username");
                String listing_name = rs.getString("listing_name");
                String transaction_status = rs.getString("transaction_status");

                transaction.setId(id);
                transaction.setListingId(listing_id);
                transaction.setBuyerId(buyer_id);
                transaction.setOwnerId(owner_id);
                transaction.setBuyerName(buyer_name);
                transaction.setOwnerName(owner_name);
                transaction.setIsReceived(isReceived);
                transaction.setIsPaid(isPaid);
                transaction.setStatus(transaction_status);
                transaction.setListingName(listing_name);

            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }


        return transaction;
    }
    
    //UPDATE TRANSACTION
    public void updateTransactionStatus(int id, String action) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "UPDATE transactions SET transaction_status = ? WHERE transaction_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, action);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {

            } else {
                System.out.println("no transaction found with ID " + id + ".");
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void updateTransactionPaid(int id) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "UPDATE transactions SET ispaid = ? WHERE transaction_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {

            } else {
                System.out.println("no transaction found with ID " + id + ".");
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void updateTransactionReceived(int id) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "UPDATE transactions SET isreceived = ? WHERE transaction_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {

            } else {
                System.out.println("no transaction found with ID " + id + ".");
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
