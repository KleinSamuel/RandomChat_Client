package com.samuel.klein.randomchat.account;

/**
 * Created by sam on 07.11.17.
 */

public class User {

    private String socketID;
    private String name;
    private int level;
    private int coins;

    public User(String socketID, String name, int level, int coins) {
        this.socketID = socketID;
        this.name = name;
        this.level = level;
        this.coins = coins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel(){
        return level;
    }

    public int getCoins(){
        return coins;
    }

    public String getSocketID() {
        return socketID;
    }

    public void setSocketID(String socketID) {
        this.socketID = socketID;
    }
}
