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
import com.example.chatapp.Chat.viewmodels.ChatListView;
import com.example.chatapp.database.entities.ChatDetails;
import com.example.chatapp.database.subentities.User;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Chat extends AppCompatActivity implements AddChat.AddChatListener {
    private ChatListView chatListView;
    private ActivityChatBinding binding;

    private ChatListAdapter adapter;
    static int counter=0;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // hard-coded for now, until login is implemented.
        currentUser=new User("bond","james bondddddddd",imageToString(R.drawable.doubt));

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
        binding.addChat.setOnClickListener(view->{
            DialogFragment dialog=new AddChat();
            dialog.show(getSupportFragmentManager(),"AddChat");
        });

        // open chat on click
        binding.lvChats.setOnItemClickListener((parent, view, position, id) -> {
            Intent chat=new Intent(this, ChatBody.class);
            chat.putExtra("Username",currentUser.getUsername());
            chat.putExtra("id",(int)id);
            startActivity(chat);
        });

        // long press removes the chat
        binding.lvChats.setOnItemLongClickListener(((parent, view, position, id) ->{
            Thread tr = new Thread() {
                public void run() {
                    chatListView.delete(position);
                }
            };
            tr.start();
            return true;
        }));

        // get chat list from room
        Thread tr = new Thread() {
            public void run() {
                chatListView.reload();
            }
        };
        tr.start();

    }

    private void setUser(User user){

        // decodes pfp from base64 to bitmap
        byte[] pfpToBytes=Base64.decode(user.getProfilePic(), Base64.DEFAULT);
        binding.userPFP.setImageBitmap(BitmapFactory.decodeByteArray(pfpToBytes,0,pfpToBytes.length));

        binding.userName.setText(user.getDisplayName());
    }

    private String imageToString(int source){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), source);
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
        byte[] imageInByArray = byteArrayStream.toByteArray();
        return Base64.encodeToString(imageInByArray, Base64.DEFAULT);
    }

    @Override
    public void onAddClick(DialogFragment dialog, String name) {

        if(name==null || name.equals(""))
            return;
        String base64pfp = imageToString(R.drawable.mmmm);
        ChatDetails chat1 = new ChatDetails(counter++,
                new User(name, name, base64pfp),
                null);
        chatListView.add(currentUser,chat1);
    }

    @Override
    public void onCloseClick(DialogFragment dialog) {
        // don't care!
    }
}