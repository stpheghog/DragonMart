package asia.uap.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class Listing implements Serializable {

    private int id;
    private String title;
    private byte[] photo;
    private String ownername;
    private int ownerid;
    private double price;
    private String desc;
    private int tagId;
    private String status;
    private boolean isArchived;
    private Timestamp dateTime;
    private int stock;
    private String tags;

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    // Getter and Setter methods
    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public int getOwnerID() {
        return ownerid;
    }

    public double getPrice() {
        return price;
    }

    public String getDesc() {
        return desc;
    }

    public String getStatus() {
        return status;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public int getId() {
        return id;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOwnerID(int owner) {
        this.ownerid = owner;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

}
