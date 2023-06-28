package com.example.chatapp.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatapp.Chat.Chat;
import com.example.chatapp.Chat.fragments.Settings;
import com.example.chatapp.R;
import com.example.chatapp.Register.Register;
import com.example.chatapp.database.api.UserAPI;
import com.example.chatapp.databinding.ActivityLoginBinding;

import java.util.Objects;

public class Login extends AppCompatActivity implements Settings.SettingsListener {
    private UserAPI userAPI;
    private MutableLiveData<String> jwt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);

        Intent chat = new Intent(getApplicationContext(), Chat.class);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // open dialog for setting
        binding.settingsBtn.setOnClickListener(view -> {
            DialogFragment dialog = new Settings();
            dialog.show(getSupportFragmentManager(), "Settings");
        });

        jwt = new MutableLiveData<>("");
        userAPI = new UserAPI(getApplication(), jwt, prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""));
        SharedPreferences.Editor editor = prefs.edit();
        EditText userNameEditText = findViewById(R.id.userName);
        EditText passwordEditText = findViewById(R.id.Password);

        Button not_register_button = findViewById(R.id.not_registered);
        Intent register = new Intent(this, Register.class);
        not_register_button.setOnClickListener(view -> startActivity(register));

        Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(view -> {
            jwt.setValue(prefs.getString("jwt", ""));
            userAPI.setServerUrl(prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""));
            userAPI.ValidateUser(userNameEditText.getText().toString(), passwordEditText.getText().toString());
            jwt.observe(this, s -> {
                TextView wrongMsg = findViewById(R.id.error_login);
                if (Objects.equals(s, "Failed")) {
                    wrongMsg.setText(R.string.wrong_credentials);
                } else if (Objects.equals(s, "ErrorServer")) {
                    wrongMsg.setText(R.string.error_connecting_the_server);
                } else if (!Objects.equals(jwt.getValue(), "")) {
                    editor.putString("jwt", jwt.getValue());
                    editor.putString("username", userNameEditText.getText().toString());
                    editor.apply();
                    chat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    wrongMsg.setText("");
                    startActivity(chat);
                }
            });
        });
    }

    @Override
    public void onSettingsApplyClick(DialogFragment dialog, String serverIP, String serverPort, boolean switch_theme) {
        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean connect_again = false;
        if (!Objects.equals(serverIP, "")) {
            editor.putString("serverIP", serverIP);
            editor.apply();
            connect_again = true;
        }
        if (!Objects.equals(serverPort, "")) {
            editor.putString("serverPort", serverPort);
            editor.apply();
            connect_again = true;
        }
        if (connect_again) {
            editor.putString("jwt", "");
            editor.apply();
        }
        if (switch_theme) {
            editor.apply();
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @Override
    public void onSettingsCancelClick(DialogFragment dialog) {
    }
}