package com.example.chatapp.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatBodyBinding;

public class ChatBody extends AppCompatActivity {

    private ActivityChatBodyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityChatBodyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}