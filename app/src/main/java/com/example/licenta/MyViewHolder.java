package com.example.licenta;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder
{
    ImageView profilePicture;
    TextView username;
    public MyViewHolder(@NonNull View itemView)
    {
        super(itemView);
        profilePicture = itemView.findViewById(R.id.profile_picture);
        username = itemView.findViewById(R.id.username);

    }
}
