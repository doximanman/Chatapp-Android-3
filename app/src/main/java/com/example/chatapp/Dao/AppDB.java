package com.example.chatapp.Dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={ChatDetails.class},version=1,exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract ChatDao chatDao();
}
