package com.example.chatapp.Register;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.chatapp.R;

public class Register extends AppCompatActivity {
    private static final int FILE_UPLOAD_REQUEST_CODE = 1;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    //    public void onUploadButtonClick(View view) {
//        // Open a file chooser or gallery intent to let the user select a file
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(intent, FILE_UPLOAD_REQUEST_CODE);
//    }
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
}