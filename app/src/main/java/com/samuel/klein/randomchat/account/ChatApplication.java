package com.samuel.klein.randomchat.account;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.debug.Debug;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by sam on 05.11.17.
 */

public class ChatApplication extends Application {

    private Socket mSocket;
    private User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        Debug.print("Created ChatApplication");
        openSocket();
    }

    private void openSocket(){

        Debug.print("Trying to open socket connection to ["+Constants.CHAT_SERVER_URL+"] ...");

        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            Debug.print("Successfully opened socket connection.");
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    public void requestChatroom(String roomName){
        try {
            JSONObject object = new JSONObject();
            object.put("roomName", roomName);
            object.put("userName", mUser.getName());
            mSocket.emit("joinRoom", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void reset(){
        Debug.print("Restarted ChatApplication");
        mSocket = null;
        mUser = null;
        openSocket();
    }

    public Socket getSocket(){
        return mSocket;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(String name) {
        this.mUser = new User("", name);
    }
}
