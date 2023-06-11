package com.example.chatapp.Chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.Dao.ChatDetails;

import java.util.List;

public class ChatListView extends ViewModel {
    private MutableLiveData<List<ChatDetails>> chatList;

    public MutableLiveData<List<ChatDetails>> getChatList() {
        if(chatList==null)
            chatList=new MutableLiveData<>();
        return chatList;
    }


}
