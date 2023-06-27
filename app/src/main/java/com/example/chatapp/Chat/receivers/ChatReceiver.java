package com.example.chatapp.Chat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.chatapp.Chat.viewmodels.ChatView;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;

public class ChatReceiver extends BroadcastReceiver {
    private ChatView chatView;
    String chatId;

    public ChatReceiver(ChatView chatView,String chatId){
        this.chatView=chatView;
        this.chatId=chatId;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type=intent.getStringExtra("type");
        switch(type){
            case "NewMessage":
                // check if the new message is relevant
                if(!intent.getStringExtra("chatId").equals(chatId))
                    return;
                // create the new message and update viewmodel
                String messageBody = intent.getStringExtra("message");
                String messageId = intent.getStringExtra("messageId");
                String username = intent.getStringExtra("username");
                String created = intent.getStringExtra("created");
                Message newMessage = new Message(messageId, created, new User(username, "", ""), messageBody);
                chatView.add(newMessage);
                break;
        }
    }
}
