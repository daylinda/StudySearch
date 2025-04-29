package com.example.groupprojectapplication.model;

import java.util.ArrayList;

public class Chat {
    String userId;
    ArrayList<Message> message;

    public Chat(String userId, ArrayList<Message> message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Message> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<Message> message) {
        this.message = message;
    }
}
