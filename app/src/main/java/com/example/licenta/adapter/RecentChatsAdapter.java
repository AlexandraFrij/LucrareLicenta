package com.example.licenta.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.ChatPage;
import com.example.licenta.FirebaseHelper;
import com.example.licenta.R;
import com.example.licenta.holder.RecentChatsViewHolder;
import com.example.licenta.item.RecentChatsRecyclerViewItem;
import com.example.licenta.util.AndroidUtil;

import java.sql.Timestamp;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecentChatsAdapter extends RecyclerView.Adapter<RecentChatsViewHolder> {

    Context context;
    List<RecentChatsRecyclerViewItem> items;

    public RecentChatsAdapter(Context context, List<RecentChatsRecyclerViewItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecentChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentChatsViewHolder(LayoutInflater.from(context).inflate(R.layout.recent_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatsViewHolder holder, int position) {
        String username = items.get(position).getUsername();
        int photo = items.get(position).getImage();
        String email = items.get(position).getEmail();
        String message = items.get(position).getMessage();
        String timestamp = items.get(position).getTimestamp();

        holder.username.setText(username);
        holder.profilePicture.setImageResource(photo);
        holder.message.setText(message);
        holder.time.setText(timestamp);
        FirebaseHelper.getProfilePicture(email).getDownloadUrl()
                .addOnCompleteListener( task ->
                {
                    if(task.isSuccessful())
                    {
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilePicture(context, uri, holder.profilePicture);
                    }
                });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatPage.class);
            intent.putExtra("username", username);
            intent.putExtra("photo", photo);
            intent.putExtra("email", email);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount()
        {
            return items.size();
        }
}
