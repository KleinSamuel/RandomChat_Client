package com.samuel.klein.randomchat.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.LoginActivity;
import com.samuel.klein.randomchat.debug.Debug;

/**
 * Created by sam on 05.11.17.
 */

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    private static final int REQUEST_LOGIN = 0;

    private TextView textview;

    private String mUsername;

    public MainFragment(){
        super();
        Debug.print("Fragment constructor called!");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Debug.print("On create called");

        if(mUsername == null) {
            signIn();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Debug.print("On create view called!");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Debug.print("On view created called");
        textview = (TextView) view.findViewById(R.id.textview);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Debug.print("On activity result called");

        if (Activity.RESULT_OK != resultCode) {
            getActivity().finish();
            return;
        }
        mUsername = data.getStringExtra("username");
        textview.setText("Welcome "+mUsername);
    }

    /**
     * Open Login activity
     */
    public void signIn() {
        mUsername = null;
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }
}
