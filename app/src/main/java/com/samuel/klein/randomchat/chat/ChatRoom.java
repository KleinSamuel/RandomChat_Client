package com.samuel.klein.randomchat.chat;

import com.samuel.klein.randomchat.account.User;

import java.util.ArrayList;

/**
 * Created by sam on 07.11.17.
 */

public class ChatRoom {

    private String name;
    private int limit;
    private int load;
    private ArrayList<User> userlist;

    public ChatRoom(String name, int limit, int load) {
        this.name = name;
        this.limit = limit;
        this.load = load;
        this.userlist = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getLimit() {
        return limit;
    }

    public int getLoad() {
        return load;
    }

    public ArrayList<User> getUserlist() {
        return userlist;
    }
}
