package com.example.chatapp.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;

import com.example.chatapp.Chat.adapters.MessageListAdapter;
import com.example.chatapp.Chat.receivers.ChatReceiver;
import com.example.chatapp.Chat.viewmodels.ChatView;
import com.example.chatapp.database.subentities.User;
import com.example.chatapp.databinding.ActivityChatBodyBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;


public class ChatBody extends AppCompatActivity {

    ChatView chatView;

    private ActivityChatBodyBinding binding;

    private MessageListAdapter adapter;
    ChatReceiver firebaseReceiver;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBodyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gets intent extras (username of user and chatid)
        Intent chat = getIntent();
        String username = chat.getStringExtra("Username");
        String chatName = chat.getStringExtra("chatName");
        binding.chatName.setText(chatName);
        String id = chat.getStringExtra("id");

        // chat pic
        // get from file
        File file = new File(getCacheDir(), "profilePic.txt");
        String profilePic = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            profilePic = reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert profilePic != null;
        // decode base64 to bitmap
        byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
        Bitmap decoded = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        binding.chatPFP.setImageBitmap(decoded);

        chatView = new ViewModelProvider(this).get(ChatView.class);
        chatView.finishConstruction(id, username);

        // adapter to display messages properly
        adapter = new MessageListAdapter(getApplicationContext(), username);
        RecyclerView rvMessages = binding.rvMessages;
        rvMessages.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setReverseLayout(true);
        layout.setStackFromEnd(true);
        rvMessages.setLayoutManager(layout);

        // start listening to firebase
        firebaseReceiver = new ChatReceiver(chatView, id);

        // auto scroll
        rvMessages.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom)
                rvMessages.smoothScrollToPosition(0);
        });

        // whenever messages change - notify the adapter.
        chatView.get().observe(this, newChat -> {
            // redo adapter - everything needs new view (all the messages were pushed up)
            adapter.setMsgList(newChat.getMessages());
            rvMessages.smoothScrollToPosition(0);
        });

        // go back button
        binding.backBTN.setOnClickListener(view -> finish()
        );

        // send message
        binding.sendButton.setOnClickListener(view -> {
            if (!binding.messageInput.getText().toString().matches("/\\A\\s*\\z/")) {

                chatView.add(binding.messageInput.getText().toString());

                // clear text
                binding.messageInput.setText("");
                // close keyboard
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // broadcast receiver to get notified by the server to update the chat list
        LocalBroadcastManager.getInstance(this).registerReceiver(firebaseReceiver,
                new IntentFilter("RECEIVE_MESSAGE"));
        new Thread(() -> chatView.reload()).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // remove broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(firebaseReceiver);
    }

}