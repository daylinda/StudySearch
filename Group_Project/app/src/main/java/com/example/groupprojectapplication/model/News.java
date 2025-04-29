package com.example.groupprojectapplication.model;

import static com.example.groupprojectapplication.ListAVLTree.numberToTime;

import java.io.Serializable;
import java.util.ArrayList;

public class News implements Serializable {
    String id;
    String topic;
    String time;
    String createdUserId;
    String likes;
    boolean promoted;

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public News() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public News(String id, String topic, String time, String createdUserId, String likes) {
        this.id = id;
        this.topic = topic;
        time = Integer.valueOf(time).toString();
        time = String.format("%5s", time).replace(' ', '0');
        this.time = time;
        this.createdUserId = createdUserId;
        this.likes = likes;
        this.promoted = false;
    }

    public News(String id, String topic, String time, String createdUserId) {
        time = Integer.valueOf(time).toString();
        time = String.format("%5s", time).replace(' ', '0');
        this.id = id;
        this.topic = topic;
        this.time = time;
        this.createdUserId = createdUserId;
        this.likes = "0";
        this.promoted = false;
    }

    public String show(){
        String time = String.valueOf(this.time);
        String ret = "\n";
        ret += numberToTime(time);
        ArrayList<String> strings = new ArrayList<>();

        ret+= " Topic: " +this.topic;
        return ret;
    }

}
