package com.example.licenta.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;

public class SearchUserHolder extends RecyclerView.ViewHolder
{
    public ImageView profilePicture;
    public TextView username;
    public SearchUserHolder(@NonNull View itemView)
    {
        super(itemView);
        profilePicture = itemView.findViewById(R.id.profile_picture);
        username = itemView.findViewById(R.id.username);

    }
}
