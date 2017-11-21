package com.samuel.klein.randomchat.chat;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.account.User;
import com.samuel.klein.randomchat.debug.Debug;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 05.11.17.
 */

public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    private ChatApplication app;
    private ChatActivity chatActivity;
    private Socket mSocket;

    private EditText messageInput;
    private Button sendButton;

    private RecyclerView mMessagesView;
    private RecyclerView.Adapter mAdapter;
    private boolean keyBoardIsOpen = false;

    private List<Message> messageList;

    public ChatFragment(){
        super();
        Debug.print("Fragment constructor called!");

        messageList = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Debug.print("On attach called");

        mAdapter = new MessageAdapter(context, messageList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatActivity = (ChatActivity) getActivity();
        app = (ChatApplication) chatActivity.getApplication();
        mSocket = app.getSocket();

        addListener();
    }

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Debug.print("On create view called!");

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mView = view;

        /* used to detect if keyboard is opened after it was closed to center messages */
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mView.getWindowVisibleDisplayFrame(r);
                int heightDiff = mView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 500 && !keyBoardIsOpen) {
                    scrollToBottom();
                    keyBoardIsOpen = true;
                }else{
                    keyBoardIsOpen = false;
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Debug.print("On view created called");

        mMessagesView = (RecyclerView) view.findViewById(R.id.messages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesView.setAdapter(mAdapter);

        messageInput = (EditText) view.findViewById(R.id.message_input);
        sendButton = (Button) view.findViewById(R.id.send_button);

        sendButton.setOnClickListener(sendButtonListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeListener();
    }

    private void addMessage(Message message){
        messageList.add(message);
        mAdapter.notifyItemChanged(messageList.size() - 1);
    }

    private void addListener(){
        mSocket.on("msg", receiveMessageListener);
        mSocket.on("userJoinedRoom", userJoinListener);
        mSocket.on("userLeftRoom", userLeaveListener);
    }

    private void removeListener(){
        mSocket.off("msg", receiveMessageListener);
        mSocket.off("userJoinedRoom", userJoinListener);
        mSocket.off("userLeftRoom", userLeaveListener);
    }

    View.OnClickListener sendButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String messageText = messageInput.getText().toString().trim();

            if (TextUtils.isEmpty(messageText)) {
                messageInput.requestFocus();
                return;
            }

            Message message = new Message(app.getUser(), messageText, 1);
            addMessage(message);
            chatActivity.sendMessage(messageText);

            messageInput.setText("");
            scrollToBottom();

        }
    };

    Emitter.Listener receiveMessageListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            chatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = (JSONObject) args[0];
                        String message = obj.getString("message");
                        String socketID = obj.getString("socketID");
                        String sender = obj.getString("userName");

                        Message msg = new Message(new User(socketID, sender, 0, 0), message, 1);
                        addMessage(msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    Emitter.Listener userJoinListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            chatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = (JSONObject) args[0];
                        String userName = obj.getString("userName");
                        String socketID = obj.getString("socketID");

                        chatActivity.addUser(userName, socketID);

                        Debug.print("user ["+userName+"] joined the room");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    Emitter.Listener userLeaveListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            chatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = (JSONObject) args[0];
                        String userName = obj.getString("userName");
                        String socketID = obj.getString("socketID");

                        chatActivity.removeUser(userName, socketID);

                        Debug.print("user ["+userName+"] left the room");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }
}
