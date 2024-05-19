package com.example.licenta.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
public class RecentChatsViewHolder extends RecyclerView.ViewHolder {
    public ImageView profilePicture;
    public TextView username;
    public TextView message;
    public TextView time;

    public RecentChatsViewHolder(@NonNull View itemView) {
        super(itemView);
        profilePicture = itemView.findViewById(R.id.profile_picture);
        username = itemView.findViewById(R.id.username);
        message = itemView.findViewById(R.id.last_message);
        time = itemView.findViewById(R.id.last_message_time);

    }
}
