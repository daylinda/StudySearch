package com.example.groupprojectapplication.model;

public class Message {
    String key;
    String message;
    String userName;
    String date;

    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Message(String key, String message, String userName, String date, String time) {
        this.key = key;
        this.message = message;
        this.userName = userName;
        this.date = date;
        this.time = time;
    }

    public Message() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}


