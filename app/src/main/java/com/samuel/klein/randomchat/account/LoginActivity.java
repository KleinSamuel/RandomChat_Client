package com.samuel.klein.randomchat.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.debug.Debug;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Socket mSocket;

    private EditText usernameInput;
    private EditText passwordInput;

    private Button loginButton;

    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();

        mSocket.connect();

        usernameInput = (EditText) findViewById(R.id.username_input);
        passwordInput = (EditText) findViewById(R.id.password_input);

        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(loginListener);

        mSocket.on("loginResponse", onLogin);

    }

    /**
     * Simple validation if user entered something
     *
     * @param username
     * @param password
     * @return true if user entered username and password
     */
    private boolean validateInput(String username, String password){

        if(TextUtils.isEmpty(username)) {
            usernameInput.setError(getString(R.string.error_field_required));
            usernameInput.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(password)) {
            passwordInput.setError(getString(R.string.error_field_required));
            passwordInput.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Send login request to server
     *
     * @param username
     * @param password
     */
    private void login(String username, String password){

        Debug.print("Trying to login to server [username="+username+"] ..");

        try {

            JSONObject object = new JSONObject();
            object.put("username", username);
            object.put("password", password);
            mSocket.emit("login", object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listener for login button
     */
    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            if(validateInput(username, password)){
                mUsername = username;
                login(username, password);
            }
        }
    };

    /**
     * Listener for login response event
     */
    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {

                JSONObject data = (JSONObject) args[0];
                String response = data.getString("response");
                Debug.print("Login response: "+response);

                switchActivity();

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    /**
     * Close login activity
     */
    private void switchActivity(){
        Intent intent = new Intent();
        intent.putExtra("username", mUsername);
        setResult(RESULT_OK, intent);
        finish();
    }
}
