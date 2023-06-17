package com.example.chatapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.chatapp.database.subentities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> index();

    @Query("SELECT * FROM user WHERE username = :username")
    User get(String username);

    @Query("SELECT * FROM user WHERE username = :username AND password =:password")
    User get(String username, String password);

    @Insert
    void insert(User... User);
}
