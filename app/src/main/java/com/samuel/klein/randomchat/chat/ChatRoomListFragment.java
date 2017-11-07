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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.debug.Debug;

public class ChatRoomListFragment extends Fragment {

    private ChatRoomListActivity chatRoomListActivity;
    private ChatApplication app;
    private Socket mSocket;

    private RecyclerView chatroomListView;
    private RecyclerView.Adapter mAdapter;

    public ChatRoomListFragment(){
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Debug.print("On attach called");

        chatRoomListActivity = (ChatRoomListActivity) getActivity();
        app = (ChatApplication) chatRoomListActivity.getApplication();
        mSocket = app.getSocket();

        mAdapter = new ChatRoomAdapter(context, chatRoomListActivity.getRoomList());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.print("On create called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Debug.print("On create view called!");
        return inflater.inflate(R.layout.fragment_chat_room_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Debug.print("On view created called");

        chatroomListView = (RecyclerView) view.findViewById(R.id.chatroom_list);
        chatroomListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatroomListView.setAdapter(mAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Debug.print("On activity result called");
    }

}
