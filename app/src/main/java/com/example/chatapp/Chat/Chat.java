package com.example.chatapp.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ListView;

import com.example.chatapp.Dao.ChatDetails;
import com.example.chatapp.Dao.Message;
import com.example.chatapp.Dao.User;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private ChatListView chatListView;
    private ActivityChatBinding binding;

    private ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // mutable list of chats
        chatListView = new ViewModelProvider(this).get(ChatListView.class);

        List<ChatDetails> initialList=getChats();

        // adapter between chats and the proper view
        adapter = new ChatListAdapter(initialList);
        ListView lvChats = binding.lvChats;
        lvChats.setAdapter(adapter);


        // whenever chats change - notify the adapter.
        chatListView.getChatList().observe(this, newChats -> {
            adapter.setChatList(newChats);
        });

        Thread tr=new Thread(){
            public void run(){
                generateChats();
            }
        };
        tr.start();

    }

    private List<ChatDetails> getChats(){
        return new ArrayList<>();
    }

    private void generateChats() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.mmmm);
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
        byte[] imageInByArray = byteArrayStream.toByteArray();
        String base64pfp = Base64.encodeToString(imageInByArray, Base64.DEFAULT);
        ChatDetails chat1 = new ChatDetails(0,
                new User("hello", "hello world", base64pfp),
                new Message(0, "2023-06-11T19:42:27.5871162", "world!!"));
        ArrayList<ChatDetails> newList=new ArrayList<>();
        newList.add(chat1);
        chatListView.getChatList().postValue(newList);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ChatDetails chat2 = new ChatDetails(0,
                new User("world", "hello world2", base64pfp),
                new Message(0, "2024-06-11T19:42:27.5871162", "worlddd!!"));
        newList.add(chat2);
        chatListView.getChatList().postValue(newList);
    }

}