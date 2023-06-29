package com.example.chatapp.database.subentities;

import androidx.room.Embedded;

import com.example.chatapp.database.entities.User;

public class Message {
    private String id;
    private final String created;

    @Embedded
    private final User sender;
    private String content;

    public Message(String id, String created, User sender, String content) {
        this.id = id;
        this.created = created;
        this.content = content;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public User getSender() {
        return sender;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public boolean isSender(String username) {
        if(sender==null)
            return false;
        return sender.getUsername().equals(username);
    }
}
