package com.example.chatapp.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ListView;

import com.example.chatapp.Dao.ChatListDB;
import com.example.chatapp.Dao.ChatListDao;
import com.example.chatapp.Dao.ChatDetails;
import com.example.chatapp.Dao.User;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity implements AddChat.AddChatListener {

    private ChatListDB db;
    private ChatListDao chatListDao;
    private ChatListView chatListView;
    private ActivityChatBinding binding;

    private ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // hard-coded for now, until login is implemented.
        User currentUser=new User("bond","james bondddddddd",imageToString(R.drawable.doubt));

        setUser(currentUser);

        // Room
        db = Room.databaseBuilder(getApplicationContext(), ChatListDB.class, "ChatsDB").build();
        chatListDao = db.chatListDao();

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

        // open chat on click
        binding.lvChats.setOnItemClickListener((parent, view, position, id) -> {
            Intent chat=new Intent(this, ChatBody.class);
            chat.putExtra("id",id);
            startActivity(chat);
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
        return chatListDao.index();
    }

    private void setUser(User user){

        // decodes pfp from base64 to bitmap
        byte[] pfpToBytes=Base64.decode(user.getProfilePic(), Base64.DEFAULT);
        binding.userPFP.setImageBitmap(BitmapFactory.decodeByteArray(pfpToBytes,0,pfpToBytes.length));

        binding.userName.setText(user.getDisplayName());
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
        chatListDao.insert(cd);
    }

    private void remove(ChatDetails cd) {
        List<ChatDetails> chatList = chatListView.getChatList().getValue();
        if (chatList == null) {
            return;
        }

        chatList.remove(cd);
        chatListView.getChatList().postValue(chatList);
        chatListDao.delete(cd);
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
        Thread tr=new Thread(){
            @Override
            public void run() {
                String base64pfp = imageToString(R.drawable.mmmm);
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