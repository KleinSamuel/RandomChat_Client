package com.samuel.klein.randomchat.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.debug.Debug;
import com.samuel.klein.randomchat.main.MainActivity;

public class AccountSettingsActivity extends AppCompatActivity {

    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
    }

    @Override
    public void onBackPressed() {
        logout("false");
    }

    public void logout(String logout){
        Intent intent = new Intent();
        intent.putExtra("logout", logout);
        setResult(RESULT_OK, intent);
        finish();
        Debug.print("FINISH ACCOUNT SETTINGS ACTIVITY");
    }
}
