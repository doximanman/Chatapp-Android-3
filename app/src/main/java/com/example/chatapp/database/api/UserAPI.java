package com.example.chatapp.database.api;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.database.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private final MutableLiveData<String> jwt;
    private final MutableLiveData<String> postUserRes;
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

    public UserAPI(MutableLiveData<String> jwt, String serverUrl) {
        this.jwt = jwt;
        this.postUserRes = null;
        gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public UserAPI(String serverUrl, MutableLiveData<String> postUserRes) {
        this.jwt = null;
        this.postUserRes = postUserRes;
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



    public static String displayApiResponseErrorBody(Response<?> response) {
        InputStream i = response.errorBody().byteStream();
        BufferedReader r = new BufferedReader(new InputStreamReader(i));
        StringBuilder errorResult = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                errorResult.append(line).append('\n');
            }
            Log.d("API_RESPONSE_ERROR_BODY", String.valueOf(errorResult));
            return String.valueOf(errorResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void postUser(String username, String password, String displayName, String profilePic) {
        Call<Object> call = webServiceAPI.postUser(new WebServiceAPI.FullUser(username, password, displayName, profilePic));
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                assert postUserRes != null;
                if (response.isSuccessful() && response.code() != 404) {
                    postUserRes.setValue("OK");
                } else {
                    postUserRes.setValue(displayApiResponseErrorBody(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                assert postUserRes != null;
                postUserRes.setValue("ErrorServer");
            }
        });
    }
}


