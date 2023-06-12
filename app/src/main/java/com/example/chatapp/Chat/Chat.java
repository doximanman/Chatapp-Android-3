package com.example.chatapp.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.example.chatapp.Dao.AppDB;
import com.example.chatapp.Dao.ChatDao;
import com.example.chatapp.Dao.ChatDetails;
import com.example.chatapp.Dao.Message;
import com.example.chatapp.Dao.User;
import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity implements AddChat.AddChatListener {

    private AppDB db;
    private ChatDao chatDao;
    private ChatListView chatListView;
    private ActivityChatBinding binding;

    private ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Room
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatsDB").build();
        chatDao = db.chatDao();

        // empty list of chats (for now)
        chatListView = new ViewModelProvider(this).get(ChatListView.class);

        // adapter between chats and the proper view
        adapter = new ChatListAdapter(new ArrayList<>());
        ListView lvChats = binding.lvChats;
        lvChats.setAdapter(adapter);


        // whenever chats change - notify the adapter.
        chatListView.getChatList().observe(this, newChats -> {
            adapter.setChatList(newChats);
        });

        // open dialog for add button
        binding.addChat.setOnClickListener(view->{
            DialogFragment dialog=new AddChat();
            dialog.show(getSupportFragmentManager(),"AddChat");
        });

        // long press removes the chat
        binding.lvChats.setOnItemLongClickListener(((parent, view, position, id) ->{
            Thread tr = new Thread() {
                public void run() {
                    remove(chatListView.getChatList().getValue().get(position));
                }
            };
            tr.start();
            return true;
        }));

        // get chat list from room
        Thread tr = new Thread() {
            public void run() {
                chatListView.getChatList().postValue(getChats());
            }
        };
        tr.start();

    }

    private List<ChatDetails> getChats() {
        return chatDao.index();
    }

    private void generateChats() {
        /*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.mmmm);
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
        byte[] imageInByArray = byteArrayStream.toByteArray();
        String base64pfp = Base64.encodeToString(imageInByArray, Base64.DEFAULT);
        ChatDetails chat1 = new ChatDetails(0,
                new User("hello", "hello world", base64pfp),
                new Message(0, "2023-06-11T19:42:27.5871162", "world!!"));
        insert(chat1);

        ChatDetails chat2=new ChatDetails(0,
                new User("hello2","hello world",base64pfp),
                new Message(0,"2024-06-11T19:42:27.5871162","world!!"));
        insert(chat2);*/
    }

    private void insert(ChatDetails cd) {
        List<ChatDetails> chatList = chatListView.getChatList().getValue();
        if (chatList == null) {
            List<ChatDetails> newList = new ArrayList<>();
            newList.add(cd);
            chatListView.getChatList().postValue(newList);
            return;
        }
        chatList.add(cd);
        chatListView.getChatList().postValue(chatList);
        chatDao.insert(cd);
    }

    private void remove(ChatDetails cd) {
        List<ChatDetails> chatList = chatListView.getChatList().getValue();
        if (chatList == null) {
            return;
        }

        chatList.remove(cd);
        chatListView.getChatList().postValue(chatList);
        chatDao.delete(cd);
    }

    @Override
    public void onAddClick(DialogFragment dialog, String name) {
        if(name==null || name.equals(""))
            return;
        Thread tr=new Thread(){
            @Override
            public void run() {
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.mmmm);
                ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
                byte[] imageInByArray = byteArrayStream.toByteArray();
                String base64pfp = Base64.encodeToString(imageInByArray, Base64.DEFAULT);
                ChatDetails chat1 = new ChatDetails(0,
                        new User(name, name, base64pfp),
                        null);
                insert(chat1);
            }
        };
        tr.start();
    }

    @Override
    public void onCloseClick(DialogFragment dialog) {
        // don't care!
    }
}