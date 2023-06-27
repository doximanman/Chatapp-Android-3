package com.example.chatapp.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatService extends FirebaseMessagingService {


    public ChatService() {
    }

    @Override
    public void onNewToken(@NonNull String token) {
        // new token
        Intent intent=new Intent("RECEIVE_MESSAGE");
        intent.putExtra("type","NewToken");
        intent.putExtra("token",token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
            String messageBody=data.get("message");
            String displayName=data.get("displayName");

            // channel name of notification channel is the display name of the sender.
            //createNotificationChannel(displayName);

            Intent intent=new Intent("RECEIVE_MESSAGE");
            intent.putExtra("type","NewMessage");
            intent.putExtra("messageId",data.get("messageId"));
            intent.putExtra("chatId",data.get("chatId"));
            intent.putExtra("message",data.get("message"));
            intent.putExtra("username",data.get("username"));
            intent.putExtra("created",data.get("created"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private void createNotificationChannel(String channelName){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String description="Chatapp Notification";
            NotificationChannel channel=new NotificationChannel(channelName,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
        }
    }

}