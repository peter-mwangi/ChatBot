package com.example.chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter
{
    private ArrayList<ChatsModel> chatsModelArrayList;
    private Context context;

    public ChatAdapter(ArrayList<ChatsModel> chatsModelArrayList, Context context)
    {
        this.chatsModelArrayList = chatsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //We have two view types
        switch (viewType)
        {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.user_msg_layout, parent, false);
                return new UserViewHolder(view);

            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.bot_message_layout, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        //Check who is the sender
        ChatsModel chatsModel = chatsModelArrayList.get(position);
        switch (chatsModel.getSender())
        {
            case "user":
                ((UserViewHolder)holder).userMsg.setText(chatsModel.getMessage());
                break;
            case "bot":
                ((BotViewHolder)holder).chatBotMsg.setText(chatsModel.getMessage());
                break;

        }

    }

    @Override
    public int getItemViewType(int position)
    {
        switch (chatsModelArrayList.get(position).getSender())
        {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount()
    {
        return chatsModelArrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        private TextView userMsg;

        public UserViewHolder(@NonNull View itemView)
        {
            super(itemView);
            userMsg = itemView.findViewById(R.id.user_msg);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder
    {
        private TextView chatBotMsg;

        public BotViewHolder(@NonNull View itemView)
        {
            super(itemView);
            chatBotMsg = itemView.findViewById(R.id.chatbot_msg);
        }
    }
}
