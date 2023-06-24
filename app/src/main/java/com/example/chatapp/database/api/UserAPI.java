package com.example.chatapp.database.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.R;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private final MutableLiveData<String> jwt;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    SharedPreferences prefs;

    public UserAPI(Application application, MutableLiveData<String> jwt, String serverUrl) {
        this.jwt = jwt;
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        prefs = application.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void ValidateUser(String username, String password) {
        WebServiceAPI.UsernamePassword usernamePassword = new WebServiceAPI.UsernamePassword(username, password);
        Call<String> call = webServiceAPI.verify(usernamePassword);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        jwt.setValue(response.body());
                    }
                } else {
                    jwt.setValue("Failed");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                jwt.setValue("ErrorServer");
            }
        });
    }

    public void getUser(String username) {
        String JWT = prefs.getString("JWT", "");
        Call<User> call = webServiceAPI.getUser("Bearer " + JWT, username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }
}


