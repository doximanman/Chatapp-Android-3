package com.example.chatapp.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chat WHERE id = :id")
    Chat get(int id);

    @Query("DELETE FROM chat")
    void deleteAll();

    @Insert
    void insert(Chat... Chats);

    @Update
    void update(Chat... Chats);

    @Delete
    void delete(Chat... Chats);
}
