package com.example.chatapp.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ListView;

import com.example.chatapp.Chat.adapters.ChatListAdapter;
import com.example.chatapp.Chat.fragments.AddChat;
import com.example.chatapp.Chat.fragments.Settings;
import com.example.chatapp.Chat.viewmodels.ChatListView;
import com.example.chatapp.Login.Login;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.User;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Chat extends AppCompatActivity implements AddChat.AddChatListener, Settings.SettingsListener {
    private ChatListView chatListView;
    private ActivityChatBinding binding;
    private ChatListAdapter adapter;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // todo: implement login and provide the real user here,
        //  with the username and JWT in shared storage.
        currentUser = new User("hello", "james bondddddddd", imageToString(R.drawable.doubt));

        setUser(currentUser);

        // ViewModel
        chatListView = new ViewModelProvider(this).get(ChatListView.class);

        // adapter between chats and the proper view
        adapter = new ChatListAdapter(new ArrayList<>());
        ListView lvChats = binding.lvChats;
        lvChats.setAdapter(adapter);


        // whenever chats change - notify the adapter.
        chatListView.get().observe(this, newChats -> {
            adapter.setChatList(newChats);
        });

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

        // get chat list from room
        new Thread(() -> {
            chatListView.reload();
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(() -> {
            chatListView.reload();
        }).start();
    }

    private void setUser(User user) {

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
    public void onSettingsClick(DialogFragment dialog, String serverIP, String serverPort, boolean darkMode) {
        if
    }

    @Override
    public void onSettingsCloseClick(DialogFragment dialog) {
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }


    @Override
    public void onCloseClick(DialogFragment dialog) {
        // don't care!
    }
}