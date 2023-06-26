package com.example.chatapp.database.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.R;
import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAPI {
    private MutableLiveData<Chat> chatData;
    private ChatDao chatDao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    String username;
    String JWT;

    public ChatAPI(MutableLiveData<Chat> chatData, ChatDao chatDao, Application application, String username, String serverURL, String JWT) {
        this.chatData = chatData;
        this.chatDao = chatDao;
        this.username = username;
        this.JWT = JWT;
        // Gson builder
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getChat(String chatId) {
        Call<Chat> call = webServiceAPI.getChat("Bearer " + JWT, chatId);
        call.enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(@NonNull Call<Chat> call, @NonNull Response<Chat> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {

                        // updates the new chat in the dao
                        Chat newChat = response.body();
                        chatDao.upsert(newChat);

                        // updates the chat preview in the dao
                        ChatDetails cd = chatDao.getChatDetails(chatId);
                        assert newChat != null;
                        if (newChat.getMessages().size() > 0) {
                            cd.setLastMessage(newChat.getMessages().get(0));
                        }
                        chatDao.upsert(cd);

                        chatData.postValue(newChat);
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Chat> call, @NonNull Throwable t) {

            }
        });
    }

    public void postMessage(String message, String chatId) {
        WebServiceAPI.MessageBody msg = new WebServiceAPI.MessageBody(message);
        Call<Message> call = webServiceAPI.postMessage("Bearer " + JWT, msg, chatId);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {

                        Message newMessage = response.body();

                        Chat chat = chatDao.getChat(chatId);
                        chat.addMessage(newMessage);
                        chatDao.upsert(chat);

                        ChatDetails cd = chatDao.getChatDetails(chatId);
                        cd.setLastMessage(newMessage);

                        chatDao.upsert(cd);

                        chatData.postValue(chat);
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {

            }
        });
    }
}
