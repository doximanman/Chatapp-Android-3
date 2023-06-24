package com.example.chatapp.Register;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.chatapp.Chat.fragments.Settings;
import com.example.chatapp.R;

public class Register extends AppCompatActivity implements Settings.SettingsListener{
    private static final int FILE_UPLOAD_REQUEST_CODE = 1;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

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
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri fileUri = data.getData();
                        }
                    }
                });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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