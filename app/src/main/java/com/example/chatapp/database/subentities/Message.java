package com.example.chatapp.database.subentities;

import androidx.room.Embedded;

public class Message {

    private String id;
    private String created;

    @Embedded
    private User sender;
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

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isSender(User user2) {
        return sender.getUsername().equals(user2.getUsername());
    }

    public boolean isSender(String username) {
        return sender.getUsername().equals(username);
    }
}
