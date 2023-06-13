package com.example.chatapp.Dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities={Chat.class},version=1,exportSchema = false)
@TypeConverters({com.example.chatapp.Dao.TypeConverters.MessagesConverter.class,com.example.chatapp.Dao.TypeConverters.UsersConverter.class})
public abstract class ChatsDB extends RoomDatabase {
    public abstract ChatDao chatsDao();
}
