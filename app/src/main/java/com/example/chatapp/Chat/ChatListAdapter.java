package com.example.chatapp.Chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.Dao.ChatDetails;
import com.example.chatapp.Dao.Message;
import com.example.chatapp.R;

import java.util.Arrays;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {
    List<ChatDetails> chatList;

    private class ViewHolder {
        TextView name;
        TextView lastMessage;
        TextView lastMessageDate;
        ImageView profilePic;
    }

    public ChatListAdapter(List<ChatDetails> chatList) {
        this.chatList = chatList;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return chatList.get(position).getId();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_preview_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.displayName);
            viewHolder.lastMessage = convertView.findViewById(R.id.lastMessage);
            viewHolder.profilePic = convertView.findViewById(R.id.profilePic);
            viewHolder.lastMessageDate = convertView.findViewById(R.id.lastMessageDate);

            convertView.setTag(viewHolder);
        }

        ChatDetails cd = chatList.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setText(cd.getUser().getDisplayName());

        if (cd.getLastMessage() == null) {
            viewHolder.lastMessage.setText("");
            viewHolder.lastMessageDate.setText("");
        } else {
            viewHolder.lastMessage.setText(cd.getLastMessage().getContent());

            // date is in the format: "dateTtime.millisecondsS". converts it to "date time"
            String fullDate = cd.getLastMessage().getCreated();
            StringBuilder readableDate = new StringBuilder();
            String[] splitDate = fullDate.split("T");
            readableDate.append(splitDate[0]);
            readableDate.append(" ");
            readableDate.append(splitDate[1].split("\\.")[0]);

            viewHolder.lastMessageDate.setText(readableDate.toString());
        }

        // converts image from base64 format to bitmap image
        byte[] decodedString = Base64.decode(cd.getUser().getProfilePic(), Base64.DEFAULT);
        Bitmap decoded = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        viewHolder.profilePic.setImageBitmap(decoded);

        return convertView;
    }

    public void setChatList(List<ChatDetails> chatList) {
        this.chatList = chatList;
        notifyDataSetChanged();
    }
}
