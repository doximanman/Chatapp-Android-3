package com.example.chatapp.Chat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.example.chatapp.Chat.viewmodels.ChatListView;
import com.example.chatapp.database.entities.Chat;

public class ChatListReceiver extends BroadcastReceiver {

    private ChatListView chatListView;
    public ChatListReceiver(ChatListView chatListView){
        this.chatListView=chatListView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("type").equals("NewChat")){
            new Thread(()->chatListView.reload()).start();
        }
    }
}
