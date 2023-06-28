package com.example.chatapp.database.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.chatapp.database.ChatDB;
import com.example.chatapp.database.api.ChatAPI;
import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.subentities.Message;

import java.util.ArrayList;

public class ChatRepo {
    private final ChatDao dao;
    private final ChatData chatData;
    private final ChatAPI api;
    private final String chatID;

    public ChatRepo(Application application, String chatID, String JWT, String serverURL, String username) {
        this.chatID = chatID;
        ChatDB db = Room.databaseBuilder(application.getApplicationContext(), ChatDB.class, "ChatDB").build();
        dao = db.chatDao();
        chatData = new ChatData();
        api = new ChatAPI(chatData, dao, username, serverURL, JWT);
    }

    private class ChatData extends MutableLiveData<Chat> {
        public ChatData() {
            super();
            // placeholder chat
            setValue(new Chat("0", new ArrayList<>(), new ArrayList<>()));
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                chatData.postValue(dao.getChat(chatID));
                reload();
            }).start();
        }
    }

    public LiveData<Chat> get() {
        return chatData;
    }

    /**
     * Assumes current user is the sender
     *
     * @param message content of the message
     */
    public void addMessage(String message) {
        api.postMessage(message, chatID);
    }

    /**
     * Adds the message to the local repository
     *
     * @param message the message to add
     */
    public void addMessage(Message message) {
        new Thread(() -> {
            Chat chat = dao.getChat(chatID);
            chat.addMessage(message);
            dao.upsert(chat);
            chatData.postValue(chat);
        }).start();
    }

    public void reload() {
        api.getChat(chatID);
    }
}
