package com.example.chatapp.database.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;

@Entity
public class ChatDetails {
    @PrimaryKey
    private int id;

    @Embedded(prefix="usr_")
    private User user;

    @Embedded(prefix="msg_")
    private Message lastMessage;

    public ChatDetails(int id, User user, Message lastMessage){
        this.id=id;
        this.user=user;
        this.lastMessage = lastMessage;
    }

    public int getId() {
        return id;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
