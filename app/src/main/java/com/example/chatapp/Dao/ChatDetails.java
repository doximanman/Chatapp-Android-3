package com.example.chatapp.Dao;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatDetails {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private User user;
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
