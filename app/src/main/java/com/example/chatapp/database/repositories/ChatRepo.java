package com.example.chatapp.database.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.chatapp.database.ChatDB;
import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.subentities.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatRepo {
    private ChatDao dao;
    private MessageListData messageListData;

    private int chatID;

    public ChatRepo(Context context,int chatID){
        this.chatID=chatID;
        ChatDB db= Room.databaseBuilder(context,ChatDB.class,"ChatDB").build();
        dao=db.chatDao();
        messageListData=new MessageListData();
    }

    private class MessageListData extends MutableLiveData<List<Message>>{
        public MessageListData(){
            super();
            setValue(new ArrayList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(()->{
                messageListData.postValue(dao.getChat(chatID).getMessages());
            }).start();
        }
    }

    public LiveData<List<Message>> getAll(){
        return messageListData;
    }

    public void addMessage(Message msg){
        if(msg==null)
            return;
        new Thread(()->{
            List<Message> msgList=messageListData.getValue();
            msgList.add(msg);
            messageListData.postValue(msgList);
            Chat thisChat=dao.getChat(chatID);
            thisChat.addMessage(msg);
            dao.update(thisChat);
        }).start();
    }

    public void deleteMessage(Message msg){
        if(msg==null)
            return;
        new Thread(()->{
            List<Message> msgList=messageListData.getValue();
            msgList.remove(msg);
            messageListData.postValue(msgList);
            Chat thisChat=dao.getChat(chatID);
            thisChat.removeMessage(msg);
            dao.update(thisChat);
        }).start();
    }

    public void reload(){
        new Thread(()->{
            messageListData.postValue(dao.getChat(chatID).getMessages());
        }).start();
    }
}
