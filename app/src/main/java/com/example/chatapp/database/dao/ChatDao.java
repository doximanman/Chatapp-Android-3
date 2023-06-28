package com.example.chatapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;

import java.util.List;

@Dao
public interface ChatDao {
    @Query("SELECT * FROM chatdetails")
    List<ChatDetails> getChats();

    @Query("SELECT * FROM chat WHERE id = :id")
    Chat getChat(String id);

    @Query("SELECT * FROM chatdetails WHERE id=:id")
    ChatDetails getChatDetails(String id);

    @Query("DELETE FROM chatdetails")
    void deletePreviews();

    @Query("DELETE FROM chat")
    void deleteChats();

    @Upsert
    void upsert(Chat... Chats);

    @Upsert
    void upsert(ChatDetails... chatDetails);

    @Delete
    void delete(Chat... Chats);

    @Delete
    void delete(ChatDetails... chatDetails);
}
