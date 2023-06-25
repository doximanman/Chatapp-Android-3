package com.example.chatapp.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatapp.Chat.Chat;
import com.example.chatapp.Chat.fragments.Settings;
import com.example.chatapp.R;
import com.example.chatapp.database.api.UserAPI;
import com.example.chatapp.databinding.ActivityLoginBinding;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class Login extends AppCompatActivity implements Settings.SettingsListener {
    private UserAPI userAPI;
    private MutableLiveData<String> jwt;

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
        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // open dialog for setting
        binding.settingsBtn.setOnClickListener(view -> {
            DialogFragment dialog = new Settings();
            dialog.show(getSupportFragmentManager(), "Settings");
        });

//        setContentView(R.layout.activity_login);
        jwt = new MutableLiveData<String>("");
//        userAPI = new UserAPI(getApplication(), jwt, "10.100.102.20:5000");
        userAPI = new UserAPI(getApplication(), jwt, prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""));
        SharedPreferences.Editor editor = prefs.edit();
        EditText userNameEditText = findViewById(R.id.userName);
        EditText passwordEditText = findViewById(R.id.Password);
        Button login_btn = findViewById(R.id.login_btn);
        Intent chat = new Intent(this, Chat.class);
        login_btn.setOnClickListener(view -> {
            userAPI.ValidateUser(userNameEditText.getText().toString(), passwordEditText.getText().toString());
            jwt.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    TextView wrongMsg = findViewById(R.id.error_login);
                    wrongMsg.setGravity(Gravity.CENTER);
                    if (Objects.equals(s, "Failed")) {
                        wrongMsg.setText(R.string.wrong_credentials);
                    } else if (Objects.equals(s, "ErrorServer")) {
                        wrongMsg.setText(R.string.error_connecting_the_server);
                    } else if (!Objects.equals(jwt.getValue(), "")) {
                        startActivity(chat);
                    }
                }
            });
        });
    }

    @Override
    public void onSettingsApplyClick(DialogFragment dialog, String serverIP, String serverPort, boolean switch_theme) {
        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean connect_again = false;
        if (switch_theme) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if (!Objects.equals(serverIP, "")) {
            editor.putString("serverIP", serverIP);
            editor.apply();
//            connect_again = true;
        }
        if (!Objects.equals(serverPort, "")) {
            editor.putString("serverPort", serverPort);
            editor.apply();
            connect_again = true;
        }
        if (connect_again) {
            userAPI = new UserAPI(getApplication(), jwt, prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""));

        }
    }

    @Override
    public void onSettingsCancelClick(DialogFragment dialog) {

    }
}