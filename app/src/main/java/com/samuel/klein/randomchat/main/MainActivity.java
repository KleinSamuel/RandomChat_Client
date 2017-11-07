package com.samuel.klein.randomchat.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.samuel.klein.randomchat.R;
import com.samuel.klein.randomchat.account.ChatApplication;
import com.samuel.klein.randomchat.account.Constants;
import com.samuel.klein.randomchat.chat.ChatActivity;
import com.samuel.klein.randomchat.debug.Debug;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private ChatApplication app;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (ChatApplication) getApplication();

        mUsername = fetchUsername();

        setContentView(R.layout.activity_main);
    }

    private String fetchUsername(){
        String username = null;
        try {
            username = readStorageFile().replace("username=", "");
        } catch (Exception e) {
            Debug.print("NO STORAGE FILE FOUND (must be first startup)");
        }
        return username;
    }

    private String readStorageFile() throws Exception {

        FileInputStream fis = openFileInput(Constants.STORAGE_FILENAME);

        Debug.print("STORAGE FILE FOUND");

        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        Debug.print("File content: "+sb.toString());
        fis.close();
        return sb.toString();
    }

    private void createStorageFile(String username){
        try {
            FileOutputStream fos = openFileOutput(Constants.STORAGE_FILENAME, Context.MODE_PRIVATE);
            fos.write(("username="+username).getBytes());
            fos.close();
            Debug.print("STORAGE FILE CREATED!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Debug.print("Yes Pressed!");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Debug.print("No Pressed!");
                    }
                })
                .show();
    }

    public String getUsername(){
        return mUsername;
    }

    public ChatApplication getApp(){
        return app;
    }

    public void storeUsername(String username){
        createStorageFile(username);
    }

    public void switchActivity(){

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("extra", "extratext");
        startActivity(intent);

    }
}
