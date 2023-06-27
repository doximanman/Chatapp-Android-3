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
    public ChatListReceiver(ChatListView chatListView){
        this.chatListView=chatListView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type=intent.getStringExtra("type");
        if(type.equals("NewChat")){
            new Thread(()->chatListView.reload()).start();
        }
        else if(type.equals("NewMessage")){
            // create the new message and update viewmodel
            String messageBody=intent.getStringExtra("message");
            String messageId=intent.getStringExtra("id");
            String username=intent.getStringExtra("username");
            String displayName=intent.getStringExtra("displayName");
            String created=intent.getStringExtra("created");
            Message newMessage=new Message(messageId,created,new User(username,displayName,""),messageBody);
            chatListView.update(username,newMessage);
        }
    }
}
