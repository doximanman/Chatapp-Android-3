package com.example.chatapp.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.chatapp.database.subentities.Message;

@Entity
public class ChatDetails {

    @PrimaryKey
    @NonNull
    private String id;

    @Embedded(prefix = "usr_")
    private User user;

    @Embedded(prefix = "msg_")
    private Message lastMessage;

    public ChatDetails(@NonNull String id,@NonNull User user, Message lastMessage) {
        this.id = id;
        this.user = user;
        this.lastMessage = lastMessage;
    }

    @Ignore
    public ChatDetails(@NonNull String id, User user) {
        this.id = id;
        this.user = user;
        this.lastMessage = null;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public User getUser() {
        return user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUser(User user) {
        this.user = user;
    }

}