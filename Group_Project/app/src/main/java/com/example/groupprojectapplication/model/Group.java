package com.example.groupprojectapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String groupName;
    ArrayList<User> users;

    ArrayList<Message> messages;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public Group(String groupName, ArrayList<User> users, ArrayList<Message> messages) {
        this.groupName = groupName;
        this.users = users;
        this.messages = messages;
    }
    public Group(){
    }
}
