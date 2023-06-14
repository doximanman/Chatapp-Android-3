package com.example.chatapp.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat {

    @PrimaryKey(autoGenerate = true)
    private long identifier;

    private String id;


    private List<User> users;

    private List<Message> messages;

    public Chat(@NonNull String id, List<User> users, List<Message> messages){
        this.id=id;
        this.users=users;
        this.messages=messages;
    }

    @Ignore
    public Chat(@NonNull String id, List<User> users){
        this.id=id;
        this.users=users;
        this.messages=new ArrayList<>();
    }

    public long getIdentifier() {
        return identifier;
    }

    public String getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message msg){
        messages.add(msg);
    }

    public void removeMessage(Message msg){
        messages.remove(msg);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
