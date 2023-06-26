package com.example.chatapp.Services;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ChatService extends FirebaseMessagingService {
    public ChatService() {
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        int a=1;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Intent intent=new Intent("RECEIVE_MESSAGE");
        intent.putExtra("type","NewChat");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}