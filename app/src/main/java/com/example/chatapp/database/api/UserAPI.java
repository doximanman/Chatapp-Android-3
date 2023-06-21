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

import java.io.IOException;
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
/*
    public boolean ValidateUser(String username, String password) {
        WebServiceAPI.UsernamePassword usernamePassword = new WebServiceAPI.UsernamePassword(username, password);
        Call<String> call = webServiceAPI.verify(usernamePassword);
        try {
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("JWT", response.body());
                    editor.apply();
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            // Handle the IOException if the execution fails
            return false;
        }
        return false;
    }
*/
public boolean ValidateUser(String username, String password) {
    WebServiceAPI.UsernamePassword usernamePassword = new WebServiceAPI.UsernamePassword(username, password);
    Call<String> call = webServiceAPI.verify(usernamePassword);
    final boolean[] found = new boolean[1];
//    found[0] = true;
    call.enqueue(new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {

            if (response.isSuccessful()) {
                class ThreadRun extends Thread {
                    @Override
                    public void run() {
                        if (response.body() != null) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("JWT", response.body());
                            editor.apply();
                            found[0] = true;
                        }
                        else {
                            found[0] = false;
                        }
                    }
                }
                ThreadRun threadRun = new ThreadRun();
                threadRun.start();
                try {
                    threadRun.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        @Override
        public void onFailure(Call<String> call, Throwable t) {
            found[0] = false;
        }
    });
    return found[0]; // Return a default value if needed
}

//    public void getUser(String username) {
//        String JWT = prefs.getString("JWT", "");
//        Call<User> call = webServiceAPI.getUser("Bearer " + JWT, username);
//
//        call.enqueue(new Callback<Chat>() {
//            @Override
//            public void onResponse(@NonNull Call<Chat> call, @NonNull Response<Chat> response) {
//                if (response.isSuccessful()) {
//                    new Thread(() -> {
//
//
//                    }).start();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Chat> call, @NonNull Throwable t) {
//
//            }
//        });
//
//
//    }

}
