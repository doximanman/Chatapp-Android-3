package com.example.chatapp.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.chatapp.Chat.Chat;

import java.util.List;

@Dao
public interface ChatDao {
    @Query("SELECT * FROM chatdetails")
    List<ChatDetails> index();

    @Query("SELECT * FROM chatdetails WHERE id = :id")
    ChatDetails get(int id);

    @Query("DELETE FROM chatdetails")
    void deleteAll();

    @Insert
    void insert(ChatDetails... Chats);

    @Update
    void update(ChatDetails... Chats);

    @Delete
    void delete(ChatDetails... Chats);
}
