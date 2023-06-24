package com.example.chatapp.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
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

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // open dialog for setting
        binding.settingsBtn.setOnClickListener(view -> {
            DialogFragment dialog = new Settings();
            dialog.show(getSupportFragmentManager(), "Settings");
        });

//        setContentView(R.layout.activity_login);
        MutableLiveData<String> jwt = new MutableLiveData<String>("");
        String serverUrl = getString(R.string.serverip);
        userAPI = new UserAPI(getApplication(), jwt, serverUrl);

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
                    } else if (!Objects.equals(jwt.getValue(), "")){
                        startActivity(chat);
                    }
                }
            });
        });
    }

    @Override
    public void onSettingsApplyClick(DialogFragment dialog, String serverIP, String serverPort, boolean switch_theme) {
        if (switch_theme) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
//        if (!serverIP.equals("") || !serverPort.equals("")) {
//            Intent login = new Intent(this, Login.class);
//            startActivity(login);
//        }
    }

    @Override
    public void onSettingsCancelClick(DialogFragment dialog) {

    }
}