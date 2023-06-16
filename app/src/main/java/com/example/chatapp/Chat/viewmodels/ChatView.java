package com.example.chatapp.Chat.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.repositories.ChatRepo;
import com.example.chatapp.database.subentities.Message;

public class ChatView extends AndroidViewModel {
    ChatRepo repository;
    private LiveData<Chat> chat;
    String chatId=null;
    String username=null;

    public ChatView(Application application){
        // half a constructor (needs chatID and user)
        super(application);
    }

    public void finishConstruction(String chatId, String username) {
        // rest of the constructor
        this.chatId=chatId;
        this.username=username;

        repository=new ChatRepo(getApplication(),chatId,username);
        chat=repository.get();
    }

    public LiveData<Chat> get(){return chat;}

    public void add(String message){
        repository.addMessage(message);
    }

    public void reload(){
        repository.reload();
    }

}
