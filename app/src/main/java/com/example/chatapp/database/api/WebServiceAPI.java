package com.example.chatapp.database.api;

import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.Message;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {

    class UsernamePassword{
        String username;
        String password;
        public UsernamePassword(String username,String password){
            this.username=username;
            this.password=password;
        }
    }

    @GET("Chats")
    Call<List<ChatDetails>> getChats(@Header("Authorization") String JWT);

    @POST("Chats")
    Call<ChatDetails> newChat(@Header("Authorization") String JWT, @Body String username);

    @GET("Chats/{id}")
    Call<Chat> getChat(@Header("Authorization") String JWT, @Path("id") int chatId);

    @DELETE("Chats/{id}")
    Call<Void> deleteChat(@Header("Authorization") String JWT, @Path("id") int chatId);

    @GET("Chats/{id}/Messages")
    Call<List<Message>> getMessages(@Header("Authorization") String JWT, @Path("id") int chatId);

    @POST("Chats/{id}/Messages")
    Call<Message> postMessage(@Header("Authorization") String JWT, @Body String msg);

    @POST("Tokens")
    Call<String> verify(@Body UsernamePassword usernamePassword);
}
