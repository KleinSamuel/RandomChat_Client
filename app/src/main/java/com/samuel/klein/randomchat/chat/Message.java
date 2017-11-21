package com.samuel.klein.randomchat.chat;

import com.samuel.klein.randomchat.account.User;

/**
 * Created by sam on 07.11.17.
 */

public class Message {

    public static final int NORMAL_MESSAGE = 1;

    private User user;
    private String text;

    private int mType;

    public Message(User user, String text, int type) {
        this.user = user;
        this.text = text;
        this.mType = type;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return mType;
    }
}
