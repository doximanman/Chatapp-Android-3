package com.example.chatapp.Chat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ListView;

import com.example.chatapp.Chat.adapters.ChatListAdapter;
import com.example.chatapp.Chat.fragments.AddChat;
import com.example.chatapp.Chat.fragments.Settings;
import com.example.chatapp.Chat.receivers.ChatListReceiver;
import com.example.chatapp.Chat.viewmodels.ChatListView;
import com.example.chatapp.Login.Login;
import com.example.chatapp.database.api.UserAPI;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.User;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chat extends AppCompatActivity implements AddChat.AddChatListener, Settings.SettingsListener {
    private ChatListView chatListView;
    private ActivityChatBinding binding;
    private ChatListAdapter adapter;
    private User currentUser = null;
    private String firebaseToken = null;
    private ChatListReceiver firebaseReceiver;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // todo: implement login and provide the real user here,
        //  with the username and JWT in shared storage.
        SharedPreferences prefs = getApplication().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String JWT = prefs.getString("jwt", "");
        UserAPI userAPI = new UserAPI(getApplication(), prefs.getString("serverIP", "") + ":" + prefs.getString("serverPort", ""));

//        currentUser = new User("hello", "james bondddddddd", imageToString(R.drawable.doubt));
        // ViewModel
        chatListView = new ViewModelProvider(this).get(ChatListView.class);

        // adapter between chats and the proper view
        adapter = new ChatListAdapter(new ArrayList<>());
        ListView lvChats = binding.lvChats;
        lvChats.setAdapter(adapter);

        // whenever chats change - notify the adapter.
        chatListView.get().observe(this, newChats -> {
            // update UI
            adapter.setChatList(newChats);
        });

        // start listening to firebase
        firebaseReceiver = new ChatListReceiver(chatListView, null);

        MutableLiveData<String> firebaseToken = new MutableLiveData<>(null);

        // send token to the server when the user is connected
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(firebaseToken::postValue);

        MutableLiveData<User> user = new MutableLiveData<>(null);

        // the first of the two to be triggered will not register the token
        // only the second one will (once both the token and the user are defined)
        firebaseToken.observe(this, newToken -> {
            this.firebaseToken = newToken;
            if (user.getValue() != null)
                chatListView.registerFirebaseToken(user.getValue().getUsername(), firebaseToken.getValue());
        });

        user.observe(this, newUser -> {
            if (newUser == null)
                return;
            setUser(newUser);
            if (firebaseToken.getValue() != null) {
                chatListView.registerFirebaseToken(newUser.getUsername(), firebaseToken.getValue());
            }
            chatListView.reload();
        });

        new Thread(() -> {
            User newUser = userAPI.getUser(JWT, prefs.getString("username", ""));
            user.postValue(newUser);
        }).start();

        // open dialog for add button
        binding.addChat.setOnClickListener(view -> {
            DialogFragment dialog = new AddChat();
            dialog.show(getSupportFragmentManager(), "AddChat");
        });
        // open dialog for setting
        binding.settingsBtn.setOnClickListener(view -> {
            DialogFragment dialog = new Settings();
            dialog.show(getSupportFragmentManager(), "Settings");
        });
        // open chat on click
        binding.lvChats.setOnItemClickListener((parent, view, position, id) -> {
            Intent chat = new Intent(this, ChatBody.class);
            ChatDetails clickedChat = chatListView.get().getValue().get(position);

            // pass profile pic in a file (too large to pass in intent)
            File file = new File(getCacheDir(), "profilePic.txt");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(clickedChat.getUser().getProfilePic());
            } catch (IOException e) {
                e.printStackTrace();
            }
            chat.putExtra("chatName", clickedChat.getUser().getDisplayName());
            chat.putExtra("Username", currentUser.getUsername());
            chat.putExtra("id", chatListView.get().getValue().get(position).getId());
            startActivity(chat);
        });

        binding.logoutBTN.setOnClickListener(view -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("jwt", "");
            editor.apply();
            finish();
        });

        askNotificationPermission();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // broadcast receiver to get notified by the server to update the chat list
        LocalBroadcastManager.getInstance(this).registerReceiver(firebaseReceiver,
                new IntentFilter("RECEIVE_MESSAGE"));

        // get chat list from room and load from server in the background
        new Thread(() -> chatListView.reload()).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // remove broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(firebaseReceiver);
    }

    @Override
    protected void onDestroy() {
        // unregister from server
        if (currentUser != null && firebaseToken != null) {
            chatListView.unregisterFirebaseToken(currentUser.getUsername(), firebaseToken);
        }
        super.onDestroy();

    }

    private void setUser(User user) {
        firebaseReceiver.setUsername(user.getUsername());
        currentUser = user;
        // decodes pfp from base64 to bitmap
        byte[] pfpToBytes = Base64.decode(user.getProfilePic(), Base64.DEFAULT);
        binding.userPFP.setImageBitmap(BitmapFactory.decodeByteArray(pfpToBytes, 0, pfpToBytes.length));
        binding.userName.setText(user.getDisplayName());
    }

    private String imageToString(int source) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), source);
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
        byte[] imageInByArray = byteArrayStream.toByteArray();
        return Base64.encodeToString(imageInByArray, Base64.DEFAULT);
    }

    @Override
    public void onAddClick(DialogFragment dialog, String name) {
        if (name == null || name.equals(""))
            return;
        chatListView.add(name);
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
//            editor.putString("jwt", "");
//            editor.apply();
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else if (connect_again) {
            startActivity(new Intent(this, Login.class));
        }
    }

    private void askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    public void onSettingsCancelClick(DialogFragment dialog) {

    }


    @Override
    public void onCloseClick(DialogFragment dialog) {
        // don't care!
    }
}