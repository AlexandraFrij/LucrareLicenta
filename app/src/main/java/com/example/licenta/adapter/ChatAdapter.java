package com.example.licenta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.item.ChatRecyclerViewItem;
import com.example.licenta.holder.ChatViewHolder;
import com.example.licenta.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>
{
    Context context;
    List<ChatRecyclerViewItem> items;
    String currentUser;


    public ChatAdapter(Context context, List<ChatRecyclerViewItem> items, String currentUser) {
        this.context = context;
        this.items = items;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ChatViewHolder((LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position)
    {
        ChatRecyclerViewItem chatItem = items.get(position);

        if (chatItem.getSender().equals(currentUser)) {
            holder.sentMessageLayout.setVisibility(View.VISIBLE);
            holder.receivedMessageLayout.setVisibility(View.GONE);
            holder.sentMessage.setText(chatItem.getMessage());
        } else {
            holder.sentMessageLayout.setVisibility(View.GONE);
            holder.receivedMessageLayout.setVisibility(View.VISIBLE);
            holder.receivedMessage.setText(chatItem.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
