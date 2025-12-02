/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author blonyagoncillo
 */
public class DeleteListingRequest implements Serializable{
    
    private int requestId;
    private int adminId;
    private String status;
    private ArrayList<String> listingIds;
    
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getListingIds() {
        return listingIds;
    }

    public void setListingIds(ArrayList<String> listingIds) {
        this.listingIds = listingIds;
    }
    
   
}
