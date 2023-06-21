package com.example.chatapp.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatapp.Chat.Chat;
import com.example.chatapp.R;
import com.example.chatapp.database.api.WebServiceAPI;
import com.example.chatapp.database.subentities.User;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {




    private String imageToString(int source) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), source);
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
        byte[] imageInByArray = byteArrayStream.toByteArray();
        return Base64.encodeToString(imageInByArray, Base64.DEFAULT);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login_btn = findViewById(R.id.button);
//        login_btn.setOnClickListener(view -> {
//            db = Room.databaseBuilder(getApplicationContext(),
//                            UserDB.class, "UserDB")
//                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
//                    .build();
//            userDao = db.userDao();
//            EditText userNameEditText = findViewById(R.id.userName);
//            EditText passwordEditText = findViewById(R.id.Password);
////            userDao.insert(new User("hello1", "world1", "Hello", imageToString(R.drawable.doubt)));
//            boolean is_user_exist = isUserExist(userNameEditText.getText().toString(), passwordEditText.getText().toString());
//            if (is_user_exist) {
//                Intent chat = new Intent(this, Chat.class);
//                chat.putExtra("username", userNameEditText.getText().toString());
//
//                WebServiceAPI.UsernamePassword usernamePassword = new WebServiceAPI.UsernamePassword(userNameEditText.toString(), passwordEditText.toString());
//                Call<String> call = new Retrofit.Builder()
//                        .baseUrl("http://192.168.1.143:5000/api/")
//                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
//                        .build().create(WebServiceAPI.class).verify(usernamePassword);
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.isSuccessful()) {
//                            String jwtString = response.body(); // Get the JWT string from the response
//                            chat.putExtra("JWT", jwtString); // Pass the JWT string to the 'chat' intent
//                            // Start the 'chat' activity or perform any other desired operations
//                        } else {
//                            // Handle unsuccessful response
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        // Handle failure
//                    }
//                });
//                startActivity(chat);
//            } else {
//                TextView wrongMsg = findViewById(R.id.textView2);
//                wrongMsg.setText("Wrong username or password, please try again.");
//            }
//        });


    }
}