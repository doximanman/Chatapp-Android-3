package com.example.chatapp.database.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Struct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    Retrofit retrofit;

    WebServiceAPI webServiceAPI;

    SharedPreferences prefs;

    public UserAPI(Application application) {
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.143:5000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        prefs = application.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public boolean ValidateUser(String username, String password) {
        WebServiceAPI.UsernamePassword usernamePassword = new WebServiceAPI.UsernamePassword(username, password);
        final boolean[] isFound = new boolean[1];
        Call<String> call = webServiceAPI.verify(usernamePassword);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        isFound[0] = true;
                    }
                } else {
                    isFound[0] = false;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isFound[0] = false;
            }
        });
        return isFound[0];
    }

    public void getUser(String username) {
        String JWT = prefs.getString("JWT", "");
        Call<User> call = webServiceAPI.getUser("Bearer " + JWT, username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    // dont know what to do
//                    new Thread(() -> {

//                        // updates the new chat in the dao
//                        Chat newChat = response.body();
//                        chatDao.upsert(newChat);
//
//                        // updates the chat preview in the dao
//                        ChatDetails cd = chatDao.getChatDetails(chatId);
//                        assert newChat != null;
//                        if (newChat.getMessages().size() > 0) {
//                            cd.setLastMessage(newChat.getMessages().get(0));
//                        }
//                        chatDao.upsert(cd);
//
//                        chatData.postValue(newChat);

//                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

            }
        });

    }

}
