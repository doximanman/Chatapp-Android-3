package com.example.chatapp.database.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public void ValidateUser(String username, String password) {
        WebServiceAPI.UsernamePassword usernamePassword = new WebServiceAPI.UsernamePassword(username, password);
        Call<String> call = webServiceAPI.verify(usernamePassword);



    }

}
