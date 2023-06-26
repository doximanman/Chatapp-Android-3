package com.example.chatapp.Chat.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.repositories.ChatListRepo;
import com.example.chatapp.database.subentities.User;

import java.util.List;

public class ChatListView extends AndroidViewModel {

    ChatListRepo repository;

    private LiveData<List<ChatDetails>> chatList;

    public ChatListView(Application application) {
        super(application);
        SharedPreferences prefs = application.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        repository = new ChatListRepo(application, prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""), prefs.getString("jwt", ""));
        chatList = repository.getAll();
    }

    public LiveData<List<ChatDetails>> get() {
        return chatList;
    }

    public void add(String username) {
        repository.add(username);
    }

    public void reload() {
        repository.reload();
    }

}
