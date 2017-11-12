package com.samuel.klein.randomchat.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.account.User;
import com.samuel.klein.randomchat.chat.userlist.UserlistAdapter;
import com.samuel.klein.randomchat.debug.Debug;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ChatApplication app;
    private Socket mSocket;

    private String roomName;
    private int roomLimit;
    private ArrayList<User> userList;

    private TextView mUserAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userList = new ArrayList<>();

        Intent intent = getIntent();
        roomName = intent.getStringExtra("roomName");
        roomLimit = Integer.parseInt(intent.getStringExtra("limit"));
        ArrayList<String> tmpUserList = intent.getStringArrayListExtra("userlist");
        Debug.print("Chat Activity got roomname ["+roomName+"]");

        for(String s : tmpUserList){
            String[] tmpData = s.split("_");
            userList.add(new User(tmpData[0], tmpData[1]));
            Debug.print("USERS IN THIS ROOM ["+roomName+"]:\t"+s);
        }

        app = (ChatApplication) getApplication();
        mSocket = app.getSocket();

        userList.add(new User(app.getUser().getSocketID(), app.getUser().getName()));

        setContentView(R.layout.activity_chat);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.layout_chat_actionbar, null);

        TextView mRoomTextView = (TextView) mCustomView.findViewById(R.id.roomname);
        mRoomTextView.setText(roomName);

        mUserAmount = (TextView) mCustomView.findViewById(R.id.userlist_button);
        refreshUserAmount();

        mUserAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Debug.print("Clicked on User List.");

                hideKeyboard();

                View popupView = getLayoutInflater().inflate(R.layout.layout_userlist_popup, null);
                PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                final ArrayList<String> list = new ArrayList<>();
                ListView listView = (ListView) popupView.findViewById(R.id.userlistView);

                for(int i = 0; i < userList.size(); i++) {
                    list.add(userList.get(i).getName());
                }

                UserlistAdapter adapter = new UserlistAdapter(getBaseContext(), R.layout.layout_userlist_item, list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Debug.print("On user in userlist clicked: "+list.get(position));
                        Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_SHORT).show();
                    }
                });

                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onBackPressed() {
        closeAcitivity();
    }

    private void closeAcitivity(){
        leaveChatroom();
        Intent intent = new Intent();
        intent.putExtra("test", "value");
        setResult(RESULT_OK, intent);
        finish();
        Debug.print("FINISH CHAT ACTIVITY");
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

    private void hideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void addUser(String username, String socketID){
        userList.add(new User(socketID, username));
        refreshUserAmount();
    }

    public void removeUser(String username, String socketID){
        int index = -1;
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getSocketID().equals(socketID)){
                index = i;
                break;
            }
        }
        if(index > -1){
            userList.remove(index);
        }
        refreshUserAmount();
    }

    private void refreshUserAmount(){
        mUserAmount.setText(userList.size()+"/"+roomLimit);
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
