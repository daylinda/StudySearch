package com.example.groupprojectapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    String id;
    String username;
    String useremail;
    String imageURL;


    public User(){
    }

    public User(String id, String username, String useremail, String imageURL) {
        this.id = id;
        this.username = username;
        this.useremail = useremail;
        this.imageURL = imageURL;
    }
    public User(Object createdtopics,String id, String username, String useremail, String imageURL) {
        this.id = id;
        this.username = username;
        this.useremail = useremail;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
