package com.example.chatapp.Chat.receivers;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.chatapp.Chat.viewmodels.ChatListView;
import com.example.chatapp.R;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.entities.User;

public class ChatListReceiver extends BroadcastReceiver {
    private final ChatListView chatListView;
    String username;
    Application application;

    public ChatListReceiver(ChatListView chatListView, String username, Application application) {
        this.chatListView = chatListView;
        this.username = username;
        this.application = application;
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
                String messageBody = intent.getStringExtra("message");
                String displayName = intent.getStringExtra("displayName");
                String username = intent.getStringExtra("username");
                // notifications!
                if (displayName != null && ContextCompat.checkSelfPermission(application, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED) {

                    // send notification only if the sender isn't the current user
                    if (!username.equals(this.username)) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(application, displayName)
                                .setSmallIcon(R.drawable.logo)
                                .setLargeIcon(BitmapFactory.decodeResource(application.getResources(), R.drawable.logo))
                                .setContentTitle(displayName)
                                .setContentText(messageBody)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(application);

                        notificationManager.notify(displayName, 0, builder.build());
                    }
                }

                // create the new message and update viewmodel

                String messageId = intent.getStringExtra("messageId");

                String chatId = intent.getStringExtra("chatId");
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
