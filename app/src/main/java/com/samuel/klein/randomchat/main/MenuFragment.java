package com.samuel.klein.randomchat.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.account.LoginActivity;
import com.samuel.klein.randomchat.chat.MainActivity;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    private static final int REQUEST_LOGIN = 0;

    private MainActivity mainActivity;
    private ChatApplication app;
    private String mUsername;

    private TextView usernameView;

    public MenuFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        app = mainActivity.getApp();

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

        usernameView = (TextView) view.findViewById(R.id.usernameTextView);

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

    /**
     * Open Login activity to get username
     */
    public void signIn() {
        mUsername = null;
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

}
