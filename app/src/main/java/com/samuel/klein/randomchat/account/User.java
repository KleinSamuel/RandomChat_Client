package com.samuel.klein.randomchat.account;

/**
 * Created by sam on 07.11.17.
 */

public class User {

    private String socketID;
    private String name;

    public User(String socketID, String name) {
        this.socketID = socketID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocketID() {
        return socketID;
    }

    public void setSocketID(String socketID) {
        this.socketID = socketID;
    }
}
