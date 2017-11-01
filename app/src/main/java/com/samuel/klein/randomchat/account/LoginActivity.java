package com.samuel.klein.randomchat.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.chat.ChatTestActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;

    private Button loginButton;
    private Button switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.username_input);
        passwordInput = (EditText) findViewById(R.id.password_input);

        loginButton = (Button) findViewById(R.id.login_button);
        switchButton = (Button) findViewById(R.id.switch_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Username: "+usernameInput.getText());
                //System.out.println("Password: "+passwordInput.getText());
                login();
            }
        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivity();
            }
        });

    }

    private void login(){
        //TODO:
        // mSocket.emit("login", "IWANTTOLOGIN");
    }

    private void switchActivity(){
        Intent intent = new Intent(this, ChatTestActivity.class);
        startActivity(intent);
        finish();
    }
}
