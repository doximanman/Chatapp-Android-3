package com.example.chatapp.Register;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatapp.Chat.fragments.Settings;
import com.example.chatapp.Login.Login;
import com.example.chatapp.R;
import com.example.chatapp.database.api.UserAPI;
import com.example.chatapp.databinding.ActivityLoginBinding;
import com.example.chatapp.databinding.ActivityRegisterBinding;

import java.util.Objects;

public class Register extends AppCompatActivity implements Settings.SettingsListener {
    private static final int FILE_UPLOAD_REQUEST_CODE = 1;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    UserAPI userAPI;

    public void onUploadButtonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        someActivityResultLauncher.launch(intent);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == FILE_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK) {
//            // Retrieve the selected file URI
//            Uri fileUri = data.getData();
//
//            // Perform file upload operation using the selected file URI
//            // Add your code here to handle the file upload
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userAPI = new UserAPI(getApplication(), prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""));

        // open dialog for setting
        binding.settingsBtn.setOnClickListener(view -> {
            DialogFragment dialog = new Settings();
            dialog.show(getSupportFragmentManager(), "Settings");
        });

        EditText userNameEditText = findViewById(R.id.userName);
        EditText passwordEditText = findViewById(R.id.Password);
        EditText displayNameEditText = findViewById(R.id.displayName);
//        Image profilePic = findViewById(R.id.profilePic);

        Button register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(view -> {
            userAPI.setServerUrl(prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""));

        });
//        someActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            // There are no request codes
//                            Intent data = result.getData();
//                            Uri fileUri = data.getData();
//                        }
//                    }
//                });
        Button already_registered = findViewById(R.id.already_registered);
        already_registered.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSettingsApplyClick(DialogFragment dialog, String serverIP, String serverPort, boolean switch_theme) {
        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (!Objects.equals(serverIP, "")) {
            editor.putString("serverIP", serverIP);
            editor.apply();
        }
        if (!Objects.equals(serverPort, "")) {
            editor.putString("serverPort", serverPort);
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