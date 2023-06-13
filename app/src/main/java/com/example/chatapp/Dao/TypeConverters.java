package com.example.chatapp.Dao;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TypeConverters {

    public static class UsersConverter {

        @TypeConverter
        public String fromUsersList(List<User> users) {
            if (users == null)
                return null;

            Gson gson = new Gson();
            Type type = new TypeToken<List<User>>() {
            }.getType();
            return gson.toJson(users, type);
        }

        @TypeConverter
        public List<User> toUsersList(String usersString) {
            if (usersString == null)
                return null;

            Gson gson = new Gson();
            Type type = new TypeToken<List<User>>() {
            }.getType();
            return gson.fromJson(usersString, type);
        }
    }

    public static class MessagesConverter {
        @TypeConverter
        public String fromMessagesList(List<Message> users) {
            if (users == null)
                return null;

            Gson gson = new Gson();
            Type type = new TypeToken<List<Message>>() {
            }.getType();
            return gson.toJson(users, type);
        }

        @TypeConverter
        public List<Message> toMessagesList(String usersString) {
            if (usersString == null)
                return null;

            Gson gson = new Gson();
            Type type = new TypeToken<List<Message>>() {
            }.getType();
            return gson.fromJson(usersString, type);
        }
    }
}
