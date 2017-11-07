package com.samuel.klein.randomchat.chat;

import com.samuel.klein.randomchat.account.User;

import java.util.ArrayList;

/**
 * Created by sam on 07.11.17.
 */

public class ChatRoom {

    private String name;
    private int limit;
    private ArrayList<User> userlist;

    public ChatRoom(String name, int limit) {
        this.name = name;
        this.limit = limit;
        this.userlist = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ArrayList<User> getUserlist() {
        return userlist;
    }

    public void setUserlist(ArrayList<User> userlist) {
        this.userlist = userlist;
    }
}
