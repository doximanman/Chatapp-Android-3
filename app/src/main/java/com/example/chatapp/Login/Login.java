package com.example.chatapp.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.chatapp.Chat.Chat;
import com.example.chatapp.R;
import com.example.chatapp.database.api.UserAPI;
import com.example.chatapp.database.api.WebServiceAPI;
import com.example.chatapp.database.subentities.User;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private UserAPI userAPI;

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
        MutableLiveData<String> jwt = new MutableLiveData<String>("");
        String serverUrl = getString(R.string.serverip);
        userAPI = new UserAPI(getApplication(), jwt, serverUrl);

//        FrameLayout frameLayout = findViewById(R.id.frame);
//        // Apply background blur settings
//        // Assuming you have a reference to the current window
//        // Assuming you have a reference to the current window
//        Window window = getWindow();
//        window.setBackgroundDrawableResource(android.R.color.transparent); // Use a transparent color as the window background


//        window.setBackgroundDrawableResource(android.R.color.transparent);

        EditText userNameEditText = findViewById(R.id.userName);
        EditText passwordEditText = findViewById(R.id.Password);
        Button login_btn = findViewById(R.id.button);
        Intent chat = new Intent(this, Chat.class);
        login_btn.setOnClickListener(view -> {
            userAPI.ValidateUser(userNameEditText.getText().toString(), passwordEditText.getText().toString());
            jwt.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (!(Objects.equals(s, "") || Objects.equals(s, "Failed"))) {
                        startActivity(chat);
                    } else if (Objects.equals(s, "Failed")) {
                        TextView wrongMsg = findViewById(R.id.textView2);
                        wrongMsg.setText(R.string.wrong_credentials);
                    }
                }
            });
        });
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