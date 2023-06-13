package com.example.chatapp.Dao;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity
public class Chat {

    @PrimaryKey
    private int id;


    private List<User> users;

    private List<Message> messages;

    public Chat(int id,List<User> users,List<Message> messages){
        this.id=id;
        this.users=users;
        this.messages=messages;
    }

    public int getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
