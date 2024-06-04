package com.example.licenta.holder;

import com.example.licenta.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationHolder extends RecyclerView.ViewHolder {
    public TextView contentTextView;
    public TextView addedAtTextView;
    public ImageView imageView;

    public NotificationHolder(@NonNull View itemView) {
        super(itemView);
        contentTextView = itemView.findViewById(R.id.notificationMessage);
        addedAtTextView = itemView.findViewById(R.id.notificationTime);
        imageView = itemView.findViewById(R.id.profile_picture);
    }
}
