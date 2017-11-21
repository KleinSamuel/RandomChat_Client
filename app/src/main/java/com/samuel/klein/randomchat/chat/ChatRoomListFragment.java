package com.samuel.klein.randomchat.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.debug.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatRoomListFragment extends Fragment {

    private ChatRoomListActivity chatRoomListActivity;
    private ChatApplication app;
    private Socket mSocket;

    private RecyclerView chatroomListView;
    private ChatRoomAdapter mAdapter;

    public ChatRoomListFragment(){
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        chatRoomListActivity = (ChatRoomListActivity) getActivity();
        app = (ChatApplication) chatRoomListActivity.getApplication();
        mSocket = app.getSocket();

        mSocket.on("receiveRoomListUpdate", roomListUpdateListener);

        mAdapter = new ChatRoomAdapter(context, chatRoomListActivity.getRoomList(), this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_room_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatroomListView = (RecyclerView) view.findViewById(R.id.chatroom_list);
        chatroomListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatroomListView.setAdapter(mAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    Emitter.Listener roomListUpdateListener = new Emitter.Listener() {
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

                chatRoomListActivity.refreshRoomlist(roomList);
                updateAdapter();

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    private void updateAdapter(){
        chatRoomListActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateData(chatRoomListActivity.getRoomList());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}
