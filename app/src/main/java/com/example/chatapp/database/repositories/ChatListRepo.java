package com.example.chatapp.database.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.chatapp.database.ChatDB;
import com.example.chatapp.database.api.ChatListAPI;
import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.entities.User;
import com.example.chatapp.database.subentities.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatListRepo {
    private final ChatDao dao;
    private final ChatListData chatListData;
    private final ChatListAPI api;
    private final UserData userData;


    public ChatListRepo(Application application, String serverURL, String JWT) {
        ChatDB db = Room.databaseBuilder(application.getApplicationContext(), ChatDB.class, "ChatDB").build();
        dao = db.chatDao();
        chatListData = new ChatListData();
        userData = new UserData();
        api = new ChatListAPI(chatListData, dao, serverURL, JWT);
    }

    private class UserData extends MutableLiveData<User> {
        public UserData() {
            super();
            setValue(null);
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> userData.postValue(dao.getUser())).start();
        }
    }

    private class ChatListData extends MutableLiveData<List<ChatDetails>> {
        public ChatListData() {
            super();
            setValue(new ArrayList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> chatListData.postValue(dao.getChats())).start();
        }
    }

    public LiveData<List<ChatDetails>> getAll() {
        return chatListData;
    }

    public LiveData<User> getUser() {
        return userData;
    }

    public void update(String chatId, Message lastMessage) {
        new Thread(() -> {
            ChatDetails cd = dao.getChatDetails(chatId);
            cd.setLastMessage(lastMessage);
            dao.upsert(cd);
            chatListData.postValue(dao.getChats());
        }).start();
    }

    public void registerFirebaseToken(String username, String token) {
        api.registerFirebaseToken(username, token);
    }

    public void unregisterFirebaseToken(String username, String token) {
        api.unregisterFirebaseToken(username, token);
    }

    public void add(String username) {
        api.newChat(username);
    }

    public boolean setUser(String username) {
        User newUser = api.getUser(username);
        if (newUser != null) {
            dao.deleteUser();
            dao.upsert(newUser);
            userData.postValue(newUser);
            return true;
        } else {
            userData.postValue(null);
            return false;
        }
    }

    public void clearLocal() {
        Thread tr = new Thread(() -> {
            dao.deletePreviews();
            dao.deleteChats();
            dao.deleteUser();
        });
        tr.start();
        try {
            tr.join();
        } catch (InterruptedException ignored) {

        }


    }

    public void reload() {
        api.getChats();
    }

}
