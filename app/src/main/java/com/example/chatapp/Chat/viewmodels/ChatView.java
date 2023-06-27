package com.example.chatapp.Chat.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.repositories.ChatRepo;
import com.example.chatapp.database.subentities.Message;

import java.util.stream.Stream;

public class ChatView extends AndroidViewModel {
    ChatRepo repository;
    private LiveData<Chat> chat;
    String chatId = null;
    String username = null;

    SharedPreferences prefs;

    public ChatView(Application application) {
        // half a constructor (needs chatID and user)
        super(application);
        prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);

    }

    public void finishConstruction(String chatId, String username) {
        // rest of the constructor
        this.chatId = chatId;
        this.username = username;
        repository = new ChatRepo(getApplication(), chatId, prefs.getString("jwt", ""),  prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""), username);
        chat = repository.get();
    }

    public LiveData<Chat> get() {
        return chat;
    }

    /**
     * Assumes current user is the sender
     * @param message content of the message
     */
    public void add(String message) {
        repository.addMessage(message);
    }

    /**
     * Adds the message to the local repository
     * @param message the message to add
     */
    public void add(Message message){
        assert chat.getValue()!=null;
        // filter messages to see if the message exists already (happens when
        // this is the sender of the message, for example).
        boolean messageExists=chat.getValue().getMessages().
                stream().anyMatch(currentMessage->
                        currentMessage.getId().equals(message.getId())
                );
        if(!messageExists)
            repository.addMessage(message);
    }

    public void reload() {
        repository.reload();
    }

}
