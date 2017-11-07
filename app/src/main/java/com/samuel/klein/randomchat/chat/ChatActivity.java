package com.samuel.klein.randomchat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.debug.Debug;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {

    private ChatApplication app;
    private Socket mSocket;

    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        roomName = intent.getStringExtra("roomName");
        Debug.print("Chat Activity got roomname ["+roomName+"]");

        app = (ChatApplication) getApplication();
        mSocket = app.getSocket();

        setContentView(R.layout.activity_chat);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.layout_chat_actionbar, null);

        TextView mRoomTextView = (TextView) mCustomView.findViewById(R.id.roomname);
        mRoomTextView.setText(roomName);

        TextView mButton = (TextView) mCustomView.findViewById(R.id.userlist_button);
        mButton.setText("1/15");

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        leaveChatroom();
        Debug.print("FINISH CHAT ACTIVITY");
        this.finish();
    }

    public void sendMessage(String message){
        try {
            JSONObject obj = new JSONObject();
            obj.put("message", message);
            obj.put("userName", app.getUser().getName());
            obj.put("roomName", roomName);
            mSocket.emit("msg", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void leaveChatroom(){
        Debug.print("Send request to leave chatroom ["+roomName+"]");

        try {
            JSONObject obj = new JSONObject();
            obj.put("roomName", roomName);
            mSocket.emit("leaveRoom", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
