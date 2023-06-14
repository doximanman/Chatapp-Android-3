package com.example.chatapp.Chat.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chatapp.database.repositories.ChatRepo;
import com.example.chatapp.database.subentities.Message;

import java.util.List;

public class MessageListView extends AndroidViewModel {
    ChatRepo repository;
    private LiveData<List<Message>> msgList;

    public MessageListView(Application application){
        // half a constructor (needs chatID)
        super(application);
    }

    public void setChatID(int chatID) {
        repository=new ChatRepo(getApplication().getApplicationContext(),chatID);
        msgList=repository.getAll();
    }

    public LiveData<List<Message>> get(){return msgList;}

    public void add(Message msg){
        repository.addMessage(msg);
    }

    public void delete(Message msg){
        repository.deleteMessage(msg);
    }

    public void reload(){
        repository.reload();
    }

}
