package com.example.chatapp.Chat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.example.chatapp.Chat.viewmodels.ChatListView;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;

public class ChatListReceiver extends BroadcastReceiver {

    private ChatListView chatListView;
    String username;

    public ChatListReceiver(ChatListView chatListView, String username) {
        this.chatListView = chatListView;
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("type");
        switch (type) {
            case "NewChat":
                new Thread(() -> chatListView.reload()).start();
                break;
            case "NewMessage":
                // create the new message and update viewmodel
                String messageBody = intent.getStringExtra("message");
                String messageId = intent.getStringExtra("messageId");
                String username = intent.getStringExtra("username");
                String chatId=intent.getStringExtra("chatId");
                String created = intent.getStringExtra("created");
                Message newMessage = new Message(messageId, created, new User(username, "", ""), messageBody);
                chatListView.update(chatId, newMessage);
                break;
            case "NewToken":
                new Thread(() -> chatListView.registerFirebaseToken(this.username,
                        intent.getStringExtra("token"))).start();
                break;
        }
    }
}
