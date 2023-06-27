package com.example.chatapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.chatapp.database.dao.ChatDao;
import com.example.chatapp.database.entities.Chat;
import com.example.chatapp.database.entities.ChatDetails;

@Database(entities = {Chat.class, ChatDetails.class}, version = 1, exportSchema = false)
@TypeConverters({com.example.chatapp.database.subentities.TypeConverters.MessagesConverter.class, com.example.chatapp.database.subentities.TypeConverters.UsersConverter.class})
public abstract class ChatDB extends RoomDatabase {
    public abstract ChatDao chatDao();
}
