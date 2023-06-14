package com.example.chatapp.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ListView;

import com.example.chatapp.Chat.adapters.MessageListAdapter;
import com.example.chatapp.Chat.viewmodels.MessageListView;
import com.example.chatapp.R;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;
import com.example.chatapp.databinding.ActivityChatBodyBinding;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ChatBody extends AppCompatActivity {

    MessageListView messageListView;

    private ActivityChatBodyBinding binding;

    private MessageListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityChatBodyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gets intent extras (username of user and chatid)
        Intent chat=getIntent();
        String username=chat.getStringExtra("Username");
        int id=chat.getIntExtra("id",0);

        messageListView=new ViewModelProvider(this).get(MessageListView.class);
        messageListView.setChatID(id);

        // adapter to display messages properly
        adapter=new MessageListAdapter(new ArrayList<>(),username);
        ListView lvMessages=binding.lvMessages;
        lvMessages.setAdapter(adapter);

        // whenever messages change - notify the adapter.
        messageListView.get().observe(this,newMessages->{
            adapter.setMsgList(newMessages);
        });

        // go back button
        binding.backBTN.setOnClickListener(view-> finish());

        // send message
        binding.sendButton.setOnClickListener(view->{
            if(!binding.messageInput.getText().toString().matches("/\\A\\s*\\z/")){
                String message=binding.messageInput.getText().toString();
                StringBuilder dateFormat=new StringBuilder();
                SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                dateFormat.append(date.format(new Date()));
                dateFormat.append("T");
                SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss.SSSSSSS",Locale.getDefault());
                dateFormat.append(time.format(new Date()));
                User currentUser=new User("bond","james bondddddddd","");
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.doubt);
                ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
                byte[] imageInByArray = byteArrayStream.toByteArray();
                currentUser.setProfilePic(Base64.encodeToString(imageInByArray, Base64.DEFAULT));
                Message newMessage=new Message(0,dateFormat.toString(),currentUser,message);
                messageListView.add(newMessage);
            }
        });

    }
}