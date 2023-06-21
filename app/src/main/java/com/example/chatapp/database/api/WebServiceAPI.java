package com.example.chatapp.database.api;

import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;
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

    class UsernamePassword {
        String username;
        String password;

        public UsernamePassword(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    class Username {
        String username;

        public Username(String username) {
            this.username = username;
        }
    }

    class MessageBody {
        String msg;

        public MessageBody(String msg) {
            this.msg = msg;
        }
    }

    @GET("Chats")
    Call<List<ChatDetails>> getChats(@Header("Authorization") String JWT);

    @POST("Chats")
    Call<ChatDetails> newChat(@Header("Authorization") String JWT, @Body Username username);

    @GET("Chats/{id}")
    Call<Chat> getChat(@Header("Authorization") String JWT, @Path("id") String chatId);

    @DELETE("Chats/{id}")
    Call<Void> deleteChat(@Header("Authorization") String JWT, @Path("id") String chatId);

    @GET("Chats/{id}/Messages")
    Call<List<Message>> getMessages(@Header("Authorization") String JWT, @Path("id") String chatId);

    @POST("Chats/{id}/Messages")
    Call<Message> postMessage(@Header("Authorization") String JWT, @Body MessageBody msg, @Path("id") String chatId);

    @POST("Tokens")
    Call<String> verify(@Body UsernamePassword usernamePassword);

    @GET("Users/{username}")
    Call<User> getUser(@Header("Authorization") String JWT, @Path("username") String username);

    @POST("Users")
    Call<User> postUser(@Body User user);
}
