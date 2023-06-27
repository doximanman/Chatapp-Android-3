package com.example.chatapp.Services;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

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

        Map<String,String> data=message.getData();

        // get the type of message (defined by the server)
        if(!data.containsKey("type"))
            return;

        String type=data.get("type");
        if(type==null)
            return;

        // new chat, notify broadcast listeners
        if(type.equals("NewChat")) {
            Intent intent = new Intent("RECEIVE_MESSAGE");
            intent.putExtra("type", "NewChat");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        // new message, notify broadcast listeners.
        else if(type.equals("NewMessage")){
            Intent intent=new Intent("RECEIVE_MESSAGE");
            intent.putExtra("type","NewMessage");
            intent.putExtra("id",data.get("id"));
            intent.putExtra("message",data.get("message"));
            intent.putExtra("displayName",data.get("displayName"));
            intent.putExtra("username",data.get("username"));
            intent.putExtra("created",data.get("created"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}