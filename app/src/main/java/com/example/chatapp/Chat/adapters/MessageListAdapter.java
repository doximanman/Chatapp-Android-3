package com.example.chatapp.Chat.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;
import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    List<Message> msgList;
    private LayoutInflater inflater;
    private final String username;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView time;
        LinearLayout messageRoot;
        TextView messageBubble;
        RelativeLayout messageBody;


        ViewHolder(View msgView) {
            super(msgView);
            message = msgView.findViewById(R.id.messageTXT);
            time = msgView.findViewById(R.id.messageTime);
            messageRoot=msgView.findViewById(R.id.messageRoot);
            messageBubble=msgView.findViewById(R.id.textBubble);
            messageBody=msgView.findViewById(R.id.mainMessage);
        }
    }


    public MessageListAdapter(Context context, String username) {
        inflater=LayoutInflater.from(context);
        this.msgList=new ArrayList<>();
        this.username = username;
    }

    @Override
    public long getItemId(int position) {
        return msgList.size()-position;
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.message_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message msg = msgList.get(position);

        if (msg.isSender(username)) {
            // change the look of sender messages
            holder.messageRoot.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            holder.messageBubble.setTextColor(ColorStateList.valueOf(Color.parseColor("#82D0E3")));
            holder.messageBody.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#82D0E3")));
        }
        else{
            holder.messageRoot.setLayoutDirection(View.LAYOUT_DIRECTION_INHERIT);
            holder.messageBubble.setTextColor(ColorStateList.valueOf(Color.parseColor("#CAE4EF")));
            holder.messageBody.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CAE4EF")));
        }

        holder.message.setText(msg.getContent());

        // convert dateThh:mm:ss.millisecondsS to hh:mm
        String fullDate = msg.getCreated();
        StringBuilder time = new StringBuilder();
        String hhmmss = fullDate.split("T")[1].split("\\.")[0];
        String[] colonSplit = hhmmss.split(":");
        time.append(colonSplit[0]);
        time.append(":");
        time.append(colonSplit[1]);

        holder.time.setText(time.toString());
    }

    public void setMsgList(List<Message> msgList) {
        this.msgList = msgList;

        notifyDataSetChanged();
    }
}
