package com.samuel.klein.randomchat.chat;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.debug.Debug;

import java.util.ArrayList;
import java.util.Arrays;

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
            int load = Integer.parseInt(tmpArray[2]);
            roomList.add(new ChatRoom(name, limit, load));
        }

        app = (ChatApplication) getApplication();
        mSocket = app.getSocket();

        setContentView(R.layout.activity_chat_room_list);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.layout_chatroom_list_actionbar, null);

        ImageView mRefreshButton = (ImageView) mCustomView.findViewById(R.id.userlist_refresh_button);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Debug.print("refresh room list");
                AnimationSet animSet = new AnimationSet(true);
                animSet.setInterpolator(new DecelerateInterpolator());
                animSet.setFillAfter(true);
                animSet.setFillEnabled(true);

                RotateAnimation animRotate = new RotateAnimation(0.0f, -360.0f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                animRotate.setDuration(700);
                animRotate.setFillAfter(true);
                animSet.addAnimation(animRotate);

                v.startAnimation(animSet);
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
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
