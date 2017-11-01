package com.samuel.klein.randomchat.main;

import android.app.Activity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Samuel Klein on 01.11.17.
 */

public class Headquarter {

    private final String SERVER_ADDRESS = "http://192.168.178.33:9999";
    private Socket mSocket;

    private boolean isConnected = false;

    private Activity currentActivity;

    public Headquarter(){

    }

    private void connectToServer(){
        try {

            System.out.println("Connecting to chat server ["+SERVER_ADDRESS+"] ...");
            mSocket = IO.socket(SERVER_ADDRESS);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    System.out.println("CONNECTED!");
                    isConnected = true;
                }
            });
            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    System.out.println("DISCONNECTED!");
                    isConnected = false;
                }
            });
            mSocket.on("loginResponse", new Emitter.Listener(){
                @Override
                public void call(Object... args) {

                    JSONObject data = (JSONObject) args[0];
                    boolean response = false;

                    try {

                        String res = data.getString("response");
                        if(res.equals("OK")){
                            //TODO:
                            // switchActivity();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            mSocket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
