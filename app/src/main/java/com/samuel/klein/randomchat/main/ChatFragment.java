package com.samuel.klein.randomchat.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.account.LoginActivity;
import com.samuel.klein.randomchat.chat.Message;
import com.samuel.klein.randomchat.chat.MessageAdapter;
import com.samuel.klein.randomchat.debug.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 05.11.17.
 */

public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    private ChatApplication app;

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

        app = (ChatApplication) getActivity().getApplication();
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

    private void addMessage(Message message){
        messageList.add(message);
        mAdapter.notifyItemInserted(messageList.size() - 1);
    }

    View.OnClickListener sendButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String messageText = messageInput.getText().toString().trim();

            if (TextUtils.isEmpty(messageText)) {
                messageInput.requestFocus();
                return;
            }

            Debug.print("Entered Message: "+messageText);

            Message message = new Message(app.getUser(), messageText, 1);
            addMessage(message);

            messageInput.setText("");
            scrollToBottom();

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
