package com.example.chatapp.database.repositories;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.chatapp.database.ChatDB;
import com.example.chatapp.database.api.ChatAPI;
import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;

import java.util.ArrayList;
import java.util.List;

public class ChatRepo {
    private ChatDao dao;
    private ChatData chatData;

    private ChatAPI api;
    private String chatID;

    private String username;

    public ChatRepo(Application application, String chatID,String username){
        this.chatID=chatID;
        ChatDB db= Room.databaseBuilder(application.getApplicationContext(),ChatDB.class,"ChatDB").build();
        dao=db.chatDao();
        chatData=new ChatData();
        this.username=username;
        api=new ChatAPI(chatData,dao,application,username);
    }

    private class ChatData extends MutableLiveData<Chat>{
        public ChatData(){
            super();
            setValue(new Chat("0",new ArrayList<>(),new ArrayList<>()));
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(()->{
                chatData.postValue(dao.getChat(chatID));
            }).start();
        }
    }

    public LiveData<Chat> get(){
        return chatData;
    }

    public void addMessage(Message msg){
        if(msg==null)
            return;
        new Thread(()->{
            Chat chat=chatData.getValue();
            assert chat != null;
            chat.getMessages().add(msg);
            chatData.postValue(chat);
            dao.update(chat);
        }).start();
    }

    public void reload(){
        api.getChat(chatID);
    }
}
