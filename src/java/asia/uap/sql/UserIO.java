/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.sql;

import asia.uap.beans.Listing;
import asia.uap.beans.User;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;

/**
 *
 * @author Stephanie
 */
public class UserIO implements Serializable {

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

    public UserIO(String driver, String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.driver = driver;
    }

    public UserIO() {
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

    public ArrayList<User> getAllUsers() throws SQLException {
        Connection conn = null;

        ArrayList<User> userList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "select * from mart_user WHERE isarchive = ?";
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

    public ArrayList<User> getAllApprovedUsers() throws SQLException {
        Connection conn = null;

        ArrayList<User> userList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "select * from mart_user WHERE isarchive = ? AND isapproved = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, false);
            ps.setBoolean(2, true);
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

    public ArrayList<User> getAllUnapprovedUsers() throws SQLException {
        Connection conn = null;

        ArrayList<User> userList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "select * from mart_user WHERE isarchive = ? AND isapproved = ? ORDER BY user_id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, false);
            ps.setBoolean(2, false);
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

    public User getUserByID(int id) throws SQLException {
        Connection conn = null;

        User currentUser = new User();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "select * from mart_user where user_id = ? AND isarchive = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setBoolean(2, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int user_id = id;
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
                currentUser.setId(user_id);
                currentUser.setUsername(username);
                currentUser.setEmail(email);
                currentUser.setFirstname(firstname);
                currentUser.setLastname(lastname);
                currentUser.setDob(date);
                currentUser.setPassword(password);
                currentUser.setPhoto(pfp);
                currentUser.setAboutme(aboutme);
                currentUser.setContact(contactinfo);
                currentUser.setApproved(isapproved);

            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return currentUser;
    }

    public User getUserByUsername(String username) throws SQLException {
        Connection conn = null;

        User currentUser = new User();
        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "select * from mart_user where username = ? AND isarchive=0";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String firstname = rs.getString("first_name");
                String lastname = rs.getString("last_name");
                String email = rs.getString("email");
                String date = rs.getString("dob");
                String password = rs.getString("pass");
                byte[] pfp = rs.getBytes("pfp");
                String aboutme = rs.getString("aboutme");
                String contactinfo = rs.getString("contactinfo");
                Boolean isapproved = rs.getBoolean("isapproved");
                currentUser.setId(id);
                currentUser.setUsername(username);
                currentUser.setEmail(email);
                currentUser.setFirstname(firstname);
                currentUser.setLastname(lastname);
                currentUser.setDob(date);
                currentUser.setPassword(password);
                currentUser.setPhoto(pfp);
                currentUser.setAboutme(aboutme);
                currentUser.setContact(contactinfo);
                currentUser.setApproved(isapproved);

            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return currentUser;
    }

    public void archiveUserByID(int id) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "UPDATE mart_user SET isarchive = ? WHERE user_id = ?";
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

    public int addUser(User row) throws SQLException {
        int rows = 0;
        Connection conn = null;

        try {
            //This allows us to execute multiple 
            //things for a single transaction.
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);
            // Updated SQL to match the MART_USER table
            String preparedSQL = "INSERT INTO mart_user (first_name, last_name, username, email, dob, pass, pfp, aboutme, contactinfo, isapproved, isarchive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(preparedSQL);

            ps.setString(1, row.getFirstname());
            ps.setString(2, row.getLastname());
            ps.setString(3, row.getUsername());
            ps.setString(4, row.getEmail());
            ps.setDate(5, java.sql.Date.valueOf(row.getDob()));
            ps.setString(6, row.getPassword());
            ps.setString(7, "");
            ps.setString(8, "");
            ps.setString(9, "");
            ps.setBoolean(10, false);
            ps.setBoolean(11, false);
            rows = ps.executeUpdate(); // Execute the update
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

    public void approveUserByID(int id) throws SQLException {
        String sql = "UPDATE mart_user SET isapproved = ? WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, true);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public boolean userExists(int id) throws SQLException {

        User user = getUserByID(id);
        if (user.getUsername() != null) {
            return true;
        }
        return false;
    }

    public void updateUser(int id, User account) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);

            String preparedSQL = "UPDATE mart_user SET first_name = ?, last_name = ?, username = ?, email = ?, pfp = ?, aboutme = ?, contactinfo = ? WHERE user_id = ?";
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

    public ArrayList<User> searchUnapprovedUsersByUsername(String username) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM mart_user WHERE isapproved = 0 AND username LIKE ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + username + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Create and add User objects to the list
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setFirstname(rs.getString("first_name"));
                user.setLastname(rs.getString("last_name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setDob(rs.getString("dob"));
                users.add(user);
            }
        }
        return users;
    }
    public ArrayList<User> getUsersByIDs(ArrayList<Integer> ids) throws SQLException {
        Connection conn = null;
        ArrayList<User> usersList = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, user, pass);

            // uses in clause to check for multiple ids and dynamically changes depending on size of arraylist
            StringBuilder sql = new StringBuilder("SELECT * FROM mart_user WHERE user_id IN (");
            for (int i = 0; i < ids.size(); i++) {
                sql.append("?");
                if (i < ids.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(") AND isarchive = false");

            PreparedStatement ps = conn.prepareStatement(sql.toString());

            // this sets the parameters for each user ID
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) { //set user bean
                User currentUser = new User();
                int user_id = rs.getInt("user_id");
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

                currentUser.setId(user_id);
                currentUser.setUsername(username);
                currentUser.setEmail(email);
                currentUser.setFirstname(firstname);
                currentUser.setLastname(lastname);
                currentUser.setDob(date);
                currentUser.setPassword(password);
                currentUser.setPhoto(pfp);
                currentUser.setAboutme(aboutme);
                currentUser.setContact(contactinfo);
                currentUser.setApproved(isapproved);

                // adds the user to the userlist
                usersList.add(currentUser);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return usersList;
    }

}
