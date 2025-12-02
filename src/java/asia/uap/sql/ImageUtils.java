/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.sql;

/**
 *
 * @author blonyagoncillo
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ImageUtils implements Serializable {

    private String driver;
    private String url;
    private String user;
    private String pass;
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

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
        this.imagePath = sql.getString("imagePath");
        
    }

    public ImageUtils(String driver, String url, String user, String pass, String imagePath) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.driver = driver;
        this.imagePath = imagePath;
    }

    public ImageUtils() {
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
    
    // Utility method to save an image from BLOB to file
    public static void saveImageFromBlob(Blob imageBlob, File file) throws IOException {
        try (InputStream inputStream = imageBlob.getBinaryStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            } 
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public Blob getImageBlobByListingId(int listingId) throws SQLException {
        Blob imageBlob = null;
        String query = "SELECT image FROM mart_listing WHERE listing_id = ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, listingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    imageBlob = rs.getBlob("image");
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return imageBlob;
    }
    
    public Blob getImageBlobByUserId(int userid) throws SQLException {
        Blob imageBlob = null;
        String query = "SELECT pfp FROM mart_user WHERE user_id = ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userid);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    imageBlob = rs.getBlob("pfp");
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return imageBlob;
    }
    
    public static void saveImageFromBytes(byte[] photoBytes, File file) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(photoBytes);
        fos.flush();
    }
}

}

    
