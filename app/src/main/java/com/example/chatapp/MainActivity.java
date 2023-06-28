package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.chatapp.Chat.Chat;
import com.example.chatapp.Login.Login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setTheme(R.style.Theme_Chatapp);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // set the serverIP and port only if they don't exist yet
        if (!prefs.contains("serverIP")) {
            editor.putString("serverIP", "10.100.102.20");
        }
        if (!prefs.contains("serverPort")) {
            editor.putString("serverPort", "5000");
        }
        if (!prefs.contains("jwt")) {
            editor.putString("jwt", "");
        }
        editor.apply();
        if (prefs.getString("jwt", "").equals("")) {
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        } else {
            Intent chat = new Intent(this, Chat.class);
            startActivity(chat);
        }

    }

}