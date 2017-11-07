package com.samuel.klein.randomchat.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.account.LoginActivity;
import com.samuel.klein.randomchat.debug.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    private static final int REQUEST_LOGIN = 0;

    private MainActivity mainActivity;
    private ChatApplication app;
    private Socket mSocket;
    private String mUsername;

    /* ui elements */
    private TextView usernameView;
    private Button randomChatButton;
    private Button roomListButton;
    private Button privateChatButton;

    public MenuFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        app = mainActivity.getApp();
        mSocket = app.getSocket();

        mSocket.on("receiveRoomList", roomListListener);

        /* get username from main activity */
        mUsername = mainActivity.getUsername();

        /* get to login if no username was found */
        if(mUsername == null){
            signIn();
        }

        app.setUser(mUsername);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameView = (TextView) view.findViewById(R.id.username_menu);
        randomChatButton = (Button) view.findViewById(R.id.enter_random_chat_button_menu);
        privateChatButton = (Button) view.findViewById(R.id.enter_private_chat_button_menu);
        roomListButton = (Button) view.findViewById(R.id.public_chatrooms_button_menu);

        randomChatButton.setOnClickListener(randomChatButtonListener);
        privateChatButton.setOnClickListener(privateChatButtonListener);
        roomListButton.setOnClickListener(roomListButtonListener);

        updateContent();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Activity.RESULT_OK != resultCode) {
            getActivity().finish();
            return;
        }
        mUsername = data.getStringExtra("username");
        mainActivity.storeUsername(mUsername);

        updateContent();
    }

    private void updateContent(){
        usernameView.setText(mUsername);
    }

    /* Open Login activity to get username */
    private void signIn() {
        mUsername = null;
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private void requestRoomList(){
        mSocket.emit("requestRoomList");
    }

    Emitter.Listener roomListListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONArray array = (JSONArray) args[0];
                ArrayList<String> roomList = new ArrayList<>();

                for(int i = 0; i < array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    String roomName = obj.getString("name");
                    String limit = obj.getString("limit");
                    roomList.add(roomName+"_"+limit);
                }

                mainActivity.openRoomList(roomList);

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener randomChatButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Debug.print("Entering random chatroom..");
            mainActivity.requestRandomChatroom();
        }
    };

    View.OnClickListener roomListButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Debug.print("Requesting room list..");
            requestRoomList();
        }
    };

    View.OnClickListener privateChatButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Debug.print("Requesting connection..");
        }
    };

}
