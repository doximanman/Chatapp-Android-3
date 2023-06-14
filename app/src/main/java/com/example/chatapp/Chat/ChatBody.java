package com.example.chatapp.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.example.chatapp.Chat.adapters.MessageListAdapter;
import com.example.chatapp.Chat.viewmodels.ChatView;
import com.example.chatapp.R;
import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;
import com.example.chatapp.databinding.ActivityChatBodyBinding;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ChatBody extends AppCompatActivity {

    ChatView chatView;

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
        String chatName=chat.getStringExtra("chatName");
        binding.chatName.setText(chatName);
        String id=chat.getStringExtra("id");

        // chat pic
        // get from file
        File file=new File(getCacheDir(),"profilePic.txt");
        String profilePic=null;
        try(BufferedReader reader=new BufferedReader(new FileReader(file))){
            profilePic=reader.readLine();
        }catch(IOException e){
            e.printStackTrace();
        }
        assert profilePic!=null;
        // decode base64 to bitmap
        byte[] decodedString=Base64.decode(profilePic,Base64.DEFAULT);
        Bitmap decoded=BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
        binding.chatPFP.setImageBitmap(decoded);

        chatView =new ViewModelProvider(this).get(ChatView.class);
        chatView.finishConstruction(id,username);

        // adapter to display messages properly
        adapter=new MessageListAdapter(new ArrayList<>(),username);
        ListView lvMessages=binding.lvMessages;
        lvMessages.setAdapter(adapter);

        // whenever messages change - notify the adapter.
        chatView.get().observe(this, newChat->{
            adapter.setMsgList(newChat.getMessages());
        });

        // go back button
        binding.backBTN.setOnClickListener(view-> finish());

        // send message
        binding.sendButton.setOnClickListener(view->{
            if(!binding.messageInput.getText().toString().matches("/\\A\\s*\\z/")){

                // content
                String message=binding.messageInput.getText().toString();

                // created
                StringBuilder dateFormat=new StringBuilder();
                SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                dateFormat.append(date.format(new Date()));
                dateFormat.append("T");
                SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss.SSSSSSS",Locale.getDefault());
                dateFormat.append(time.format(new Date()));
                String created=dateFormat.toString();

                // sender
                User currentUser=new User("hello","james bondddddddd","");
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.doubt);
                ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
                byte[] imageInByArray = byteArrayStream.toByteArray();
                currentUser.setProfilePic(Base64.encodeToString(imageInByArray, Base64.DEFAULT));
                Message newMessage=new Message("test",dateFormat.toString(),currentUser,message);

                // clear text
                binding.messageInput.setText("");
                // close keyboard
                InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                chatView.add(newMessage);
            }
        });

    }


}