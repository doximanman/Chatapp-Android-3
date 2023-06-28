package com.example.chatapp.database.api;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.entities.User;
import com.example.chatapp.database.subentities.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatListAPI {
    private final MutableLiveData<List<ChatDetails>> chatListData;
    private final ChatDao chatDao;
    String JWT;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    User oldUser;

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
        new Thread(()->oldUser=chatDao.getUser()).start();
    }

    public User getUser(String username) {
        Call<User> call = webServiceAPI.getUser("Bearer " + JWT, username);
        try {
            Response<User> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            else if(response.code()==403){
                return new User("","","unauthorized");
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public void getChats() {
        Call<List<ChatDetails>> call = webServiceAPI.getChats("Bearer " + JWT);
        call.enqueue(new Callback<List<ChatDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChatDetails>> call, @NonNull Response<List<ChatDetails>> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {

                        User currentUser=chatDao.getUser();

                        if(currentUser!=null&&oldUser!=null&&!currentUser.getUsername().equals(oldUser.getUsername())){
                            // clear database
                            chatDao.deleteChats();
                            chatDao.deletePreviews();
                            oldUser=currentUser;
                        }
                        if(oldUser==null&&currentUser!=null){
                            oldUser=currentUser;
                        }

                        // converts list to array to be able to use insert(ChatDetails... chats)
                        List<ChatDetails> chatList = response.body();
                        assert chatList != null;
                        ChatDetails[] chatArray = chatList.toArray(new ChatDetails[0]);

                        // inserts all chats (placeholder chat for the inner chats)
                        chatDao.upsert(chatArray);
                        chatDao.upsert(chatList.stream().map(cd -> {
                            Chat oldChat=chatDao.getChat(cd.getId());
                            if(oldChat!=null)
                                return oldChat;

                            List<User> users = new ArrayList<>();
                            users.add(cd.getUser());
                            List<Message> messageList=new ArrayList<>();
                            if(cd.getLastMessage()!=null)
                                messageList.add(cd.getLastMessage());
                            return new Chat(cd.getId(), users, messageList);
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

    public void registerFirebaseToken(String username, String token) {
        WebServiceAPI.UsernameToken userToken = new WebServiceAPI.UsernameToken(username, token);
        Call<Void> call = webServiceAPI.registerFirebaseToken("Bearer " + JWT, userToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }


    public void unregisterFirebaseToken(String username, String token) {
        WebServiceAPI.UsernameToken userToken = new WebServiceAPI.UsernameToken(username, token);
        Call<Void> call = webServiceAPI.unregisterFirebaseToken("Bearer " + JWT, userToken);
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
