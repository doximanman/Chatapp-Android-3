package com.example.chatapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatapp.Chat.Chat;
import com.example.chatapp.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent chat=new Intent(this, Chat.class);
        chat.putExtra("username","hello");
        chat.putExtra("JWT","aabbcc");
        startActivity(chat);
    }
}