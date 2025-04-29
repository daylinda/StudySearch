package com.example.groupprojectapplication;

import com.example.groupprojectapplication.model.User;
// @author Davina
public  class CurrentUserDetails {
    static User user;

    public CurrentUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static User getCurrentUser(){
        return user;
    }

}
