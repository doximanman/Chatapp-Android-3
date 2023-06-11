package com.example.chatapp.Dao;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Chat {
    @PrimaryKey(autoGenerate = true)
    private int id;
}
