package com.example.chatapp.database.repositories;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.chatapp.R;
import com.example.chatapp.database.ChatDB;
import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatListRepo {



    private ChatDao dao;
    private ChatListData chatListData;


    public ChatListRepo(Context context){

        ChatDB db=Room.databaseBuilder(context, ChatDB.class,"ChatDB").build();
        dao=db.chatDao();
        chatListData=new ChatListData();
    }

    private class ChatListData extends MutableLiveData<List<ChatDetails>>{
        public ChatListData(){
            super();
            setValue(new ArrayList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(()->{
                chatListData.postValue(dao.getChats());
            }).start();
        }
    }

    public LiveData<List<ChatDetails>> getAll(){
        return chatListData;
    }
    public void add(User sender,ChatDetails cd){
        if(cd==null)
            return;
        new Thread(()->{
            List<ChatDetails> chatList=chatListData.getValue();
            chatList.add(cd);
            chatListData.postValue(chatList);
            dao.insert(cd);
            List<User> users=new ArrayList<>();
            users.add(sender);
            users.add(cd.getUser());
            Chat newChat=new Chat(cd.getId(),users,new ArrayList<Message>());
            dao.insert(newChat);
        }).start();
    }

    public void delete(ChatDetails cd){
        if(cd==null)
            return;
        new Thread(()->{
            List<ChatDetails> chatList=chatListData.getValue();
            chatList.remove(cd);
            chatListData.postValue(chatList);
            dao.delete(cd);
            dao.delete(dao.getChat(cd.getId()));
        }).start();
    }

    public void reload(){
        new Thread(()->{
            chatListData.postValue(dao.getChats());
        }).start();
    }

}
