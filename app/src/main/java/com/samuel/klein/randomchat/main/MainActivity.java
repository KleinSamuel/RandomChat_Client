package com.samuel.klein.randomchat.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.account.Constants;
import com.samuel.klein.randomchat.chat.ChatActivity;
import com.samuel.klein.randomchat.chat.ChatRoomListActivity;
import com.samuel.klein.randomchat.debug.Debug;
import com.samuel.klein.randomchat.settings.AccountSettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ChatApplication app;
    private Socket mSocket;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (ChatApplication) getApplication();
        mSocket = app.getSocket();

        mUsername = fetchUsername();

        mSocket.on(Socket.EVENT_CONNECT, onConnectListener);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnectListener);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onErrorListener);

        if(!mSocket.connected()){
            mSocket.connect();
        }

        setContentView(R.layout.activity_main);
    }

    private String fetchUsername(){
        String username = null;
        try {
            username = readStorageFile().replace("username=", "");
        } catch (Exception e) {
            Debug.print("NO STORAGE FILE FOUND (must be first startup)");
        }
        return username;
    }

    private String readStorageFile() throws Exception {

        FileInputStream fis = openFileInput(Constants.STORAGE_FILENAME);

        Debug.print("STORAGE FILE FOUND");

        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        Debug.print("File content: "+sb.toString());
        fis.close();
        return sb.toString();
    }

    private void createStorageFile(String username){
        try {
            FileOutputStream fos = openFileOutput(Constants.STORAGE_FILENAME, Context.MODE_PRIVATE);
            fos.write(("username="+username).getBytes());
            fos.close();
            Debug.print("STORAGE FILE CREATED!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteStorageFile() {
        getApplicationContext().deleteFile(Constants.STORAGE_FILENAME);
        Debug.print("Storage file deleted.");
    }

    public void restartApp(){
        deleteStorageFile();
        app.reset();
        mSocket.disconnect();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        // DO NOT CLOSE APP ON BACK PRESSED
    }

    public String getUsername(){
        return mUsername;
    }

    public ChatApplication getApp(){
        return app;
    }

    public void storeUsername(String username){
        createStorageFile(username);
    }

    public void requestChatroom(String roomName){
        try {
            JSONObject object = new JSONObject();
            object.put("roomName", roomName);
            object.put("userName", mUsername);
            mSocket.emit("joinRoom", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestRandomChatroom(){
        try {
            JSONObject object = new JSONObject();
            object.put("userName", mUsername);
            mSocket.emit("joinRandomRoom", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public void joinChatroom(String roomName, ArrayList<String> userList, String limit){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("roomName", roomName);
        intent.putStringArrayListExtra("userlist", userList);
        intent.putExtra("limit", limit);
        startActivity(intent);
    }

    public void openRoomList(ArrayList<String> list){
        Intent intent = new Intent(this, ChatRoomListActivity.class);
        intent.putExtra("roomList", list);
        startActivity(intent);
    }

    public void openAccountSettngs(){
        Intent intent = new Intent(this, AccountSettingsActivity.class);
        startActivity(intent);
    }*/

    Emitter.Listener onConnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Debug.print("CONNECTED!");
        }
    };

    Emitter.Listener onDisconnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Debug.print("DISCONNECTED!");
        }
    };

    Emitter.Listener onErrorListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Debug.print("ERROR!");
        }
    };
}
