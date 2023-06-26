package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.chatapp.Chat.Chat;
import com.example.chatapp.Login.Login;
import com.example.chatapp.Register.Register;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("serverIP", "10.0.2.2");
        editor.putString("serverPort", "5000");
        editor.apply();

        Intent login = new Intent(this, Login.class);
        startActivity(login);

//        Intent chat = new Intent(this, Chat.class);
//        startActivity(chat);
    }

}