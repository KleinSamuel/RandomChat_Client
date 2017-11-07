package com.samuel.klein.randomchat.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.debug.Debug;

import java.util.ArrayList;

public class ChatRoomListActivity extends AppCompatActivity {

    private ChatApplication app;
    private Socket mSocket;

    private ArrayList<ChatRoom> roomList;

    public ChatRoomListActivity() {
        roomList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        ArrayList<String> tmp = intent.getStringArrayListExtra("roomList");

        for(String s : tmp){
            String[] tmpArray = s.split("_");
            String name = tmpArray[0];
            int limit = Integer.parseInt(tmpArray[1]);
            roomList.add(new ChatRoom(name, limit));
        }

        app = (ChatApplication) getApplication();
        mSocket = app.getSocket();

        setContentView(R.layout.activity_chat_room_list);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Debug.print("FINISH CHAT ROOM LIST ACTIVITY");
        this.finish();
    }

    public ArrayList<ChatRoom> getRoomList() {
        return roomList;
    }
}
