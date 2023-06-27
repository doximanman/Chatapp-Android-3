package com.example.chatapp.database.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.R;
import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatListAPI {
    private MutableLiveData<List<ChatDetails>> chatListData;
    private ChatDao chatDao;
    String JWT;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public ChatListAPI(MutableLiveData<List<ChatDetails>> chatListData, ChatDao chatDao, String serverURL, String JWT) {
        this.chatListData = chatListData;
        this.chatDao = chatDao;
        this.JWT = JWT;
        // Gson builder
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        // todo: JWT should already be in shared preferences. After implementing login,
        //  remove this line.
//        getToken("hello", "Helloworld1!");
    }
    public void getChats() {
        Call<List<ChatDetails>> call = webServiceAPI.getChats("Bearer " + JWT);
        call.enqueue(new Callback<List<ChatDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChatDetails>> call, @NonNull Response<List<ChatDetails>> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {

                        // clears database
                        chatDao.deleteChats();
                        chatDao.deletePreviews();

                        // converts list to array to be able to use insert(ChatDetails... chats)
                        List<ChatDetails> chatList = response.body();
                        assert chatList != null;
                        ChatDetails[] chatArray = chatList.toArray(new ChatDetails[0]);

                        // inserts all chats (placeholder chat for the inner chats)
                        chatDao.upsert(chatArray);
                        chatDao.upsert(chatList.stream().map(cd -> {
                            List<User> users = new ArrayList<>();
                            users.add(cd.getUser());
                            return new Chat(cd.getId(), users, new ArrayList<>());
                        }).toArray(Chat[]::new));
                        chatListData.postValue(chatDao.getChats());
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChatDetails>> call, @NonNull Throwable t) {

            }
        });
    }

    public void registerFirebaseToken(String username,String token){
        WebServiceAPI.UsernameToken userToken=new WebServiceAPI.UsernameToken(username,token);
        Call<Void> call=webServiceAPI.registerFirebaseToken("Bearer "+JWT,userToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }


    public void unregisterFirebaseToken(String username,String token){
        WebServiceAPI.UsernameToken userToken=new WebServiceAPI.UsernameToken(username,token);
        Call<Void> call=webServiceAPI.unregisterFirebaseToken("Bearer "+JWT,userToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }

    public void newChat(String username) {
        WebServiceAPI.Username userName = new WebServiceAPI.Username(username);
        Call<ChatDetails> call = webServiceAPI.newChat("Bearer " + JWT, userName);
        call.enqueue(new Callback<ChatDetails>() {
            @Override
            public void onResponse(@NonNull Call<ChatDetails> call, @NonNull Response<ChatDetails> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        // creates new chat in the local database

                        ChatDetails details = response.body();
                        List<User> users = new ArrayList<>();
                        assert details != null;
                        users.add(details.getUser());
                        Chat newChat = new Chat(details.getId(), users);
                        chatDao.upsert(newChat);
                        chatDao.upsert(details);

                        // updates UI
                        List<ChatDetails> chatList = chatListData.getValue();
                        assert chatList != null;
                        chatList.add(details);
                        chatListData.postValue(chatList);
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatDetails> call, @NonNull Throwable t) {

            }
        });
    }
}
