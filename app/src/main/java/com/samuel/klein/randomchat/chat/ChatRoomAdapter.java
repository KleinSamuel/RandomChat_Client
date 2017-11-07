package com.samuel.klein.randomchat.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samuel.klein.randomchat.R;

import java.util.List;

/**
 * Created by sam on 07.11.17.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private List<ChatRoom> mRooms;

    public ChatRoomAdapter(Context context, List<ChatRoom> mRooms) {
        this.mRooms = mRooms;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatroom_list_entry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChatRoom room = mRooms.get(position);
        viewHolder.setName(room.getName());
        viewHolder.setLimit(room.getLimit()+"");
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
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

        public void setLimit(String limit){
            if(mLimitView == null){
                return;
            }
            mLimitView.setText(limit);
        }

    }
}
