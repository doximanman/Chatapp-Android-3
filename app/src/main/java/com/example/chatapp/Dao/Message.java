package com.example.chatapp.Dao;

public class Message {
    private int id;
    private String created;
    private String content;

    public Message(int id, String created, String content){
        this.id=id;
        this.created=created;
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
