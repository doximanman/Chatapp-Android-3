package com.example.chatapp.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatapp.Chat.Chat;
import com.example.chatapp.R;
import com.example.chatapp.database.dao.UserDao;
import com.example.chatapp.database.UserDB;
import com.example.chatapp.database.subentities.User;

import java.io.ByteArrayOutputStream;

public class Login extends AppCompatActivity {
    private UserDB db;
    private UserDao userDao;

    boolean isUserExist(String username, String password) {
        User user = this.userDao.get(username, password);
        System.out.println(user);
        return user != null;
    }

    private String imageToString(int source) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), source);
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
        byte[] imageInByArray = byteArrayStream.toByteArray();
        return Base64.encodeToString(imageInByArray, Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login_btn = findViewById(R.id.button);
        login_btn.setOnClickListener(view -> {
            db = Room.databaseBuilder(getApplicationContext(),
                            UserDB.class, "UserDB")
                    .allowMainThreadQueries().build();
            userDao = db.userDao();
            EditText userNameEditText = findViewById(R.id.userName);
            EditText passwordEditText = findViewById(R.id.Password);
            userDao.insert(new User("hello", "world", "Hello", imageToString(R.drawable.doubt)));
            boolean is_user_exist = isUserExist(userNameEditText.getText().toString(), passwordEditText.getText().toString());
            if (is_user_exist) {
                Intent chat = new Intent(this, Chat.class);
                chat.putExtra("username", userNameEditText.getText().toString());
                chat.putExtra("JWT", "aabbcc");
                startActivity(chat);
            }
//            else {
//
//            }
        });


    }
}