package com.example.chatapp.Chat.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.chatapp.database.subentities.Message;
import com.example.chatapp.database.subentities.User;
import com.example.chatapp.R;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {
    List<Message> msgList;

    private final String username;

    private static class ViewHolder {
        TextView message;
        TextView time;
    }

    public MessageListAdapter(List<Message> msgList, String username) {
        this.msgList = msgList;
        this.username=username;
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View RealGetView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.message = convertView.findViewById(R.id.messageTXT);
            viewHolder.time=convertView.findViewById(R.id.messageTime);

            // differentiate between sender and receiver
            if(msgList.get(position).isSender(username)){
                convertView.findViewById(R.id.messageRoot).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                ((TextView)convertView.findViewById(R.id.textBubble)).setTextColor(ColorStateList.valueOf(Color.parseColor("#82D0E3")));
                convertView.findViewById(R.id.mainMessage).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#82D0E3")));
            }

            convertView.setTag(viewHolder);
        }

        Message msg = msgList.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.message.setText(msg.getContent());

        // convert dateThh:mm:ss.millisecondsS to hh:mm
        String fullDate=msg.getCreated();
        StringBuilder time=new StringBuilder();
        String hhmmss=fullDate.split("T")[1].split("\\.")[0];
        String[] colonSplit=hhmmss.split(":");
        time.append(colonSplit[0]);
        time.append(":");
        time.append(colonSplit[1]);

        viewHolder.time.setText(time.toString());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // flip order
        return RealGetView(msgList.size()-1-position,convertView,parent);
    }

    public void setMsgList(List<Message> msgList) {
        this.msgList = msgList;
        notifyDataSetChanged();
    }
}
