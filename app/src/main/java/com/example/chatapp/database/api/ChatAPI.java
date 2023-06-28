package com.example.chatapp.database.api;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAPI {
    private final MutableLiveData<Chat> chatData;
    private final ChatDao chatDao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    String username;
    String JWT;

    public ChatAPI(MutableLiveData<Chat> chatData, ChatDao chatDao, String username, String serverURL, String JWT) {
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

                        // no need to save the pfp of the sender for every message
                        assert newChat != null;
                        newChat.getMessages().forEach(message -> message.getSender().setProfilePic(""));
                        chatDao.upsert(newChat);

                        // updates the chat preview in the dao
                        ChatDetails cd = chatDao.getChatDetails(chatId);

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
                        // no need to save pfp
                        assert newMessage != null;
                        newMessage.getSender().setProfilePic("");

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
