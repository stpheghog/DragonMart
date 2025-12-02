/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.sql;

import asia.uap.beans.Admin;
import asia.uap.beans.User;
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
public class AdminIO {

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

    public AdminIO(String driver, String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.driver = driver;
    }

    public AdminIO() {
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

        }

        return conn;
    }

    public ArrayList<Admin> getAllAdmins() throws SQLException {
        Connection conn = null;
        ArrayList<Admin> adminList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "select * from admin";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int adminId = rs.getInt("admin_id");
                String email = rs.getString("email");
                String password = rs.getString("pass");

                Admin admin = new Admin();
                admin.setAdmin_id(adminId);
                admin.setEmail(email);
                admin.setPassword(password);
                adminList.add(admin);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return adminList;
    }

    public Admin getAdminByID(int id) throws SQLException {
        Connection conn = null;
        Admin currentAdmin = new Admin();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "select * from admin where admin_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("pass");
                currentAdmin.setAdmin_id(id);
                currentAdmin.setEmail(email);
                currentAdmin.setPassword(password);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return currentAdmin;
    }

    public ArrayList<User> getAllUsers() throws SQLException {
        Connection conn = null;

        ArrayList<User> userList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "select * from mart_user where isarchive = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String firstname = rs.getString("first_name");
                String lastname = rs.getString("last_name");
                String email = rs.getString("email");
                String date = rs.getString("dob");
                String password = rs.getString("pass");
                byte[] pfp = rs.getBytes("pfp");
                String aboutme = rs.getString("aboutme");
                String contactinfo = rs.getString("contactinfo");
                Boolean isapproved = rs.getBoolean("isapproved");
                User user = new User();
                user.setId(id);
                user.setUsername(username);
                user.setEmail(email);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setDob(date);
                user.setPassword(password);
                user.setPhoto(pfp);
                user.setAboutme(aboutme);
                user.setContact(contactinfo);
                user.setApproved(isapproved);
                userList.add(user);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return userList;
    }

    public void archiveUserByID(int id) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "update mart_user set isarchive = ? where user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {

            } else {
                System.out.println("no user found with ID " + id + ".");
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void updateUser(int id, User account) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String preparedSQL = "update mart_user set first_name = ?, last_name = ?, username = ?, email = ?, pfp = ?, aboutme = ?, contactinfo = ? where user_id = ?";
            PreparedStatement ps = conn.prepareStatement(preparedSQL);
            ps.setString(1, account.getFirstname());
            ps.setString(2, account.getLastname());
            ps.setString(3, account.getUsername());
            ps.setString(4, account.getEmail());
            ps.setBytes(5, account.getPhoto());
            ps.setString(6, account.getAboutme());
            ps.setString(7, account.getContact());
            ps.setInt(8, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {

            } else {
                System.out.println("no user found with ID " + id + ".");
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void archiveCommentByID(int id) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pass);

        try {

            String sql = "update listing_comment set isarchive = ? where comment_id = ?";
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

    public boolean removeListing(ArrayList<String> listingIDs) throws SQLException, NumberFormatException {
        boolean success = false;

        Connection conn = null;

        try {
            //This allows us to execute multiple 
            //things for a single transaction.
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);
            // Updated SQL to match the MART_USER table
            String preparedSQL = "update mart_listing set isarchive = 1 where listing_id = ?";
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

    public void approveUsersByIDs(ArrayList<Integer> ids) throws SQLException {
        String sql = "UPDATE mart_user SET isapproved = ? WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (Integer id : ids) {
                ps.setBoolean(1, true);
                ps.setInt(2, id);
                ps.addBatch();         //adds to batch to be updated 
            }

            int[] result = ps.executeBatch(); //executes all updates together

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error approving users in batch.", e);
        }
    }

    public void archiveUsersByIDs(ArrayList<Integer> ids) throws SQLException {
        String userSql = "UPDATE mart_user SET isarchive = ? WHERE user_id = ?";
        String listingSql = "UPDATE mart_listing SET isarchive = 1 WHERE owner_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement userPs = conn.prepareStatement(userSql);
                PreparedStatement listingPs = conn.prepareStatement(listingSql)) {

            conn.setAutoCommit(false);

            for (Integer id : ids) {
                // Update mart_user table
                userPs.setBoolean(1, true);
                userPs.setInt(2, id);
                userPs.addBatch();

                // Update mart_listing table
                listingPs.setInt(1, id);
                listingPs.addBatch();
            }

            // Execute all updates in batch
            userPs.executeBatch();
            listingPs.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error archiving users in batch.", e);
        }
    }

    public Admin authenticateAdmin(String email, String password) throws SQLException {

        Connection conn = null;

        try {
            //This allows us to execute multiple 
            //things for a single transaction.
            conn = DriverManager.getConnection(url, user, pass);

            String preparedSQL = "select * from admin where email = ? AND pass = ?";
            PreparedStatement ps = conn.prepareStatement(preparedSQL);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdmin_id(rs.getInt("admin_id"));
                admin.setEmail(rs.getString("email"));
                admin.setPassword(rs.getString("pass"));
                return admin;
            }

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
        return null;
    }
}
