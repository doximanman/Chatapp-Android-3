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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private final MutableLiveData<String> jwt;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    //SharedPreferences prefs;
    Gson gson;

    public void setServerUrl(String serverUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

//    public void setJwt()

    public UserAPI(Application application, MutableLiveData<String> jwt, String serverUrl) {
        this.jwt = jwt;
        gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public UserAPI(Application application, String serverUrl) {
        this.jwt = null;
        gson = new GsonBuilder().setLenient().create();
        // .baseUrl("http://" + prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""))
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void ValidateUser(String username, String password) {
        assert jwt != null;
        WebServiceAPI.UsernamePassword usernamePassword = new WebServiceAPI.UsernamePassword(username, password);
        Call<String> call = webServiceAPI.verify(usernamePassword);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        jwt.setValue(response.body());
                    }
                } else {
                    jwt.setValue("Failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                jwt.setValue("ErrorServer");
            }
        });
    }


    //    public User getUser(String username) {
//        final User[] currentUser = new User[1];
//        String JWT = prefs.getString("JWT", "");
//        Call<User> call = webServiceAPI.getUser("Bearer " + JWT, username);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                currentUser[0] = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                currentUser[0] = new User("HHH","hhh",null);
//            }
//        });
//        return currentUser[0];
//    }
    public User getUser(String JWT, String username) {
        Call<User> call = webServiceAPI.getUser("Bearer " + JWT, username);
        try {
            Response<User> response = call.execute();
            if (response.isSuccessful())
                return response.body();
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public String postUser(String username, String password, String displayName,String profilePic) {
        Call<Object> call = webServiceAPI.postUser(new User(username, displayName, profilePic));
        try {
            Response<Object> response = call.execute();
            if (response.body() instanceof User)
                return "OK";
            else {
                return (String) response.body();
            }
        } catch (IOException e) {
            return "Error";
        }
    }
}


