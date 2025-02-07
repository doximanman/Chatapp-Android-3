package com.example.chatapp.Register;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import java.util.regex.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatapp.Chat.fragments.Settings;
import com.example.chatapp.R;
import com.example.chatapp.database.api.UserAPI;
import com.example.chatapp.databinding.ActivityRegisterBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Register extends AppCompatActivity implements Settings.SettingsListener {
    private UserAPI userAPI;
    private Bitmap profilePicBitmap;
    private MutableLiveData<String> postUserRes;
    private final String passwordRegExp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
    private final String usernameRegExp = "^[a-zA-Z0-9-_!.]{4,20}$";
    private final Pattern usernamePattern = Pattern.compile(usernameRegExp);
    private final Pattern passwordPattern = Pattern.compile(passwordRegExp);
    private Matcher matcher;

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            profilePicBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private String imageToString(Bitmap bm) {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
        byte[] imageInByArray = byteArrayStream.toByteArray();
        return "data:image/png;base64," + Base64.encodeToString(imageInByArray, Base64.DEFAULT);
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profilePicBitmap = null;
        postUserRes = new MutableLiveData<>("");

        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userAPI = new UserAPI(prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""), postUserRes);

        // open dialog for setting
        binding.settingsBtn.setOnClickListener(view -> {
            DialogFragment dialog = new Settings();
            dialog.show(getSupportFragmentManager(), "Settings");
        });

        EditText userNameEditText = findViewById(R.id.userName);
        EditText passwordEditText = findViewById(R.id.Password);
        EditText repeatPasswordEditText = findViewById(R.id.RepeatPassword);
        EditText displayNameEditText = findViewById(R.id.displayName);
        TextView wrongMsg = findViewById(R.id.error_register);

        Button upload_btn = findViewById(R.id.btnUpload);
        upload_btn.setOnClickListener(v -> imageChooser());

        Button register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(view -> {
            matcher = usernamePattern.matcher(userNameEditText.getText().toString());
            if (!matcher.matches()) {
                wrongMsg.setText(R.string.wrong_username);
                return;
            }
            if (!passwordEditText.getText().toString().equals(repeatPasswordEditText.getText().toString())) {
                wrongMsg.setText(R.string.wrong_repeat);
                return;
            }
            matcher = passwordPattern.matcher(passwordEditText.getText().toString());
            if (!matcher.matches()) {
                wrongMsg.setText(R.string.wrong_password);
                return;
            }
            if (displayNameEditText.getText().toString().equals("")) {
                wrongMsg.setText(R.string.wrong_displayName);
                return;
            }
            if (displayNameEditText.getText().toString().equals(userNameEditText.getText().toString())) {
                wrongMsg.setText(R.string.similar_display_name);
                return;
            }
            if (profilePicBitmap == null) {
                wrongMsg.setText(R.string.wrong_profilePic);
                return;
            }

            // Check image size
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            profilePicBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            int imageSize = outputStream.toByteArray().length / 1024; // Image size in KB
            if (imageSize > 990) {
                wrongMsg.setText(R.string.big_profile_pic);
                return;
            }
            userAPI.setServerUrl(prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""));
            userAPI.postUser(userNameEditText.getText().toString(), passwordEditText.getText().toString(), displayNameEditText.getText().toString(), imageToString(profilePicBitmap));
            postUserRes.observe(this, s -> {
                if (Objects.equals(s, "ErrorServer")) {
                    wrongMsg.setText(R.string.error_connecting_the_server);
                } else if (Objects.equals(postUserRes.getValue(), "OK")) {
                    finish();
                } else {
                    wrongMsg.setText(postUserRes.getValue());
                }
            });


        });
        Button already_registered = findViewById(R.id.already_registered);
        already_registered.setOnClickListener(view -> finish());
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