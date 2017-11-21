package com.samuel.klein.randomchat.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.debug.Debug;

public class AccountSettingsFragment extends Fragment {

    private AccountSettingsActivity accountSettingsActivity;

    public AccountSettingsFragment(){
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Debug.print("On attach called");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.print("On create called");

        accountSettingsActivity = (AccountSettingsActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Debug.print("On create view called!");

        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Debug.print("On view created called");

        Button logoutButton = (Button) view.findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(logoutButtonListener);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Debug.print("On activity result called");
    }

    View.OnClickListener logoutButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Debug.print("Logout button pressed");
            accountSettingsActivity.logout("true");
        }
    };

}
