package com.example.chatapp.database.repositories;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.chatapp.R;
import com.example.chatapp.database.ChatDB;
import com.example.chatapp.database.api.ChatListAPI;
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
    private ChatListAPI api;


    public ChatListRepo(Application application, String serverURL, String JWT) {
        ChatDB db = Room.databaseBuilder(application.getApplicationContext(), ChatDB.class, "ChatDB").build();
        dao = db.chatDao();
        chatListData = new ChatListData();
        api = new ChatListAPI(chatListData, dao, serverURL, JWT);
    }

    private class ChatListData extends MutableLiveData<List<ChatDetails>> {
        public ChatListData() {
            super();
            setValue(new ArrayList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                chatListData.postValue(dao.getChats());
            }).start();
        }
    }

    public LiveData<List<ChatDetails>> getAll() {
        return chatListData;
    }

    public void add(String username) {
        api.newChat(username);
    }

    public void reload() {
        api.getChats();
    }

}
