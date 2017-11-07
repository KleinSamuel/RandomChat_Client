package com.samuel.klein.randomchat.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.samuel.klein.randomchat.R;

import java.util.List;

/**
 * Created by sam on 07.11.17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;

    public MessageAdapter(Context context, List<Message> messages){
        mMessages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message.getText());
        viewHolder.setUsername(message.getUser().getName());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mUsernameView;
        private TextView mMessageView;

        public ViewHolder(View itemView){
            super(itemView);

            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
        }

        public void setUsername(String username){
            if(mUsernameView == null){
                return;
            }
            mUsernameView.setText(username);
        }

        public void setMessage(String message){
            if(mMessageView == null){
                return;
            }
            mMessageView.setText(message);
        }

    }

}
