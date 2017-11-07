package com.samuel.klein.randomchat.chat;

/**
 * Created by sam on 07.11.17.
 */

public class Message {

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
