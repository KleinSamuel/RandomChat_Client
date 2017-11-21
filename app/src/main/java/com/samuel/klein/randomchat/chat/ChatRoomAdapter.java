package com.samuel.klein.randomchat.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;

import java.util.List;

/**
 * Created by sam on 07.11.17.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private ChatRoomListFragment fragment;
    private ChatApplication app;
    private Socket mSocket;

    private List<ChatRoom> mRooms;

    private int selectedRoomPosition;

    public ChatRoomAdapter(Context context, List<ChatRoom> mRooms, ChatRoomListFragment fragment) {
        this.mRooms = mRooms;
        this.fragment = fragment;
        app = (ChatApplication) fragment.getActivity().getApplication();
        mSocket = app.getSocket();
    }

    public void updateData(List<ChatRoom> mRooms){
        this.mRooms = mRooms;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatroom_list_entry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ChatRoom room = mRooms.get(position);
        viewHolder.setName(room.getName());
        viewHolder.setLimit(room.getLimit()+"", room.getLoad()+"");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterClickedRoom(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }

    private void enterClickedRoom(int position){
        ChatRoom room = mRooms.get(position);

        /* check if room is already full */
        if(room.getUserlist().size() >= room.getLimit()){
            Toast toast = Toast.makeText(fragment.getActivity().getApplicationContext(), "This room is full.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        app.requestChatroom(room.getName());

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameView;
        private TextView mLimitView;

        public ViewHolder(View itemView){
            super(itemView);

            mNameView = (TextView) itemView.findViewById(R.id.chatroomNameTextView);
            mLimitView = (TextView) itemView.findViewById(R.id.chatroomLimitTextView);
        }

        public void setName(String name){
            if(mNameView == null){
                return;
            }
            mNameView.setText(name);
        }

        public void setLimit(String limit, String load){
            if(mLimitView == null){
                return;
            }
            mLimitView.setText(load+"/"+limit);
        }

    }
}
