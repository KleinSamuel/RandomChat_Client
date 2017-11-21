package com.samuel.klein.randomchat.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.account.LoginActivity;
import com.samuel.klein.randomchat.chat.ChatActivity;
import com.samuel.klein.randomchat.chat.ChatRoomListActivity;
import com.samuel.klein.randomchat.debug.Debug;
import com.samuel.klein.randomchat.settings.AccountSettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_CHAT = 10;
    private static final int REQUEST_CHATROOM_LIST = 11;
    private static final int REQUEST_ACCOUNT_SETTINGS = 100;

    private MainActivity mainActivity;
    private ChatApplication app;
    private Socket mSocket;
    private String mUsername;

    private boolean inChatroom;

    /* ui elements */
    private TextView usernameView;
    private TextView levelView;
    private TextView coinsView;

    private Button randomChatButton;
    private Button roomListButton;
    private Button createChatButton;
    private Button privateChatButton;

    private Button accountSettingsButton;
    private Button globalSettingsButton;

    public MenuFragment(){
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inChatroom = false;

        mainActivity = (MainActivity) getActivity();
        app = mainActivity.getApp();
        mSocket = app.getSocket();

        mSocket.on("receiveRoomList", roomListListener);
        mSocket.on("assignRoom", assignRoomListener);

        /* get username from main activity */
        mUsername = mainActivity.getUsername();

        /* get to login if no username was found */
        if(mUsername == null){
            signIn();
        }

        int level = mainActivity.getLevel();
        int coins = mainActivity.getCoins();

        app.setUser(mUsername, level, coins);
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
        levelView = (TextView) view.findViewById(R.id.level_menu);
        coinsView = (TextView) view.findViewById(R.id.coins_menu);

        randomChatButton = (Button) view.findViewById(R.id.enter_random_chat_button_menu);
        roomListButton = (Button) view.findViewById(R.id.public_chatrooms_button_menu);
        createChatButton = (Button) view.findViewById(R.id.create_chatroom_button_menu);
        privateChatButton = (Button) view.findViewById(R.id.enter_private_chat_button_menu);

        accountSettingsButton = (Button) view.findViewById(R.id.settings_account_button_menu);
        globalSettingsButton = (Button) view.findViewById(R.id.settings_global_button_menu);

        randomChatButton.setOnClickListener(randomChatButtonListener);
        roomListButton.setOnClickListener(roomListButtonListener);
        createChatButton.setOnClickListener(createChatButtonListener);
        privateChatButton.setOnClickListener(privateChatButtonListener);

        accountSettingsButton.setOnClickListener(accountSettingsButtonListener);
        globalSettingsButton.setOnClickListener(globalSettingsButtonListener);

        updateContent();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (Activity.RESULT_OK != resultCode) {
            Debug.print("FINISH ACTIVITY!");
            getActivity().finish();
            return;
        }*/

        switch (requestCode){
            case REQUEST_LOGIN:
                Debug.print("Response for login request.");
                mUsername = data.getStringExtra("username");
                int level = Integer.parseInt(data.getStringExtra("level"));
                int coins = Integer.parseInt(data.getStringExtra("coins"));
                mainActivity.storeUsername(mUsername, level, coins);
                updateContent();
                app.setUser(mUsername, level, coins);
                break;

            case REQUEST_CHAT:
                Debug.print("Response for request chat.");
                inChatroom = false;
                break;

            case REQUEST_CHATROOM_LIST:
                Debug.print("Response for request chatroom list.");
                break;

            case REQUEST_ACCOUNT_SETTINGS:
                Debug.print("Response for account settings request.");
                boolean logout = Boolean.parseBoolean(data.getStringExtra("logout"));
                if(logout){
                    mainActivity.restartApp();
                }
                break;
        }
    }

    private void updateContent(){
        usernameView.setText(mUsername);

        if(app.getUser() != null){
            levelView.setText(""+app.getUser().getLevel());
            coinsView.setText(""+app.getUser().getCoins());
        }
    }

    /* Open Login activity to get username */
    private void signIn() {
        mUsername = null;
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private void startChatActivity(String roomName, ArrayList<String> userList, String limit){
        Intent intent = new Intent(mainActivity, ChatActivity.class);
        intent.putExtra("roomName", roomName);
        intent.putStringArrayListExtra("userlist", userList);
        intent.putExtra("limit", limit);
        inChatroom = true;
        startActivityForResult(intent, REQUEST_CHAT);
    }

    private void startChatRoomListActivity(ArrayList<String> list){
        Intent intent = new Intent(mainActivity, ChatRoomListActivity.class);
        intent.putExtra("roomList", list);
        startActivityForResult(intent, REQUEST_CHATROOM_LIST);
    }

    private void startAccountSettingsActivity(){
        Intent intent = new Intent(mainActivity, AccountSettingsActivity.class);
        startActivityForResult(intent, REQUEST_ACCOUNT_SETTINGS);
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
                    String load = obj.getString("load");
                    roomList.add(roomName+"_"+limit+"_"+load);
                }

                startChatRoomListActivity(roomList);

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    Emitter.Listener assignRoomListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject obj = (JSONObject) args[0];
                String roomName = obj.getString("roomName");
                String limit = obj.getString("limit");
                JSONArray array = obj.getJSONArray("userlist");

                ArrayList<String> userList = new ArrayList<>();

                for(int i = 0; i < array.length(); i++){
                    JSONObject tmp = (JSONObject) array.get(i);
                    String socketID = tmp.getString("socketID");
                    String userName = tmp.getString("name");
                    userList.add(socketID+"_"+userName);
                }

                Debug.print("Server assigned chatroom to me ["+roomName+"]");

                if(inChatroom){
                    Debug.print("Already in a chatroom.");
                    return;
                }

                //joinChatroom(roomName, userList, limit);
                startChatActivity(roomName, userList, limit);

            } catch (JSONException e) {
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
    View.OnClickListener createChatButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "Coming soon..", Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener privateChatButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "Coming soon..", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener accountSettingsButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(getContext(), "Coming soon..", Toast.LENGTH_SHORT).show();
            startAccountSettingsActivity();
        }
    };
    View.OnClickListener globalSettingsButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "Coming soon..", Toast.LENGTH_SHORT).show();
        }
    };

}
