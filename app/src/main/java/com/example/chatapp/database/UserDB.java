package com.example.chatapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.chatapp.database.dao.UserDao;
import com.example.chatapp.database.subentities.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDB extends RoomDatabase {
    public abstract UserDao userDao();
}
