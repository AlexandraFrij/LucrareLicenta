package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>
{
    Context context;
    List<RecycleViewItem> items;

    public MyAdapter(Context context, List<RecycleViewItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.searched_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        String username = items.get(position).getUsername();
        int photo = items.get(position).getImage();
        String email = items.get(position).getEmail();
        holder.username.setText(username);
        holder.profilePicture.setImageResource(photo);

        holder.itemView.setOnClickListener(v ->{
            Intent intent = new Intent(context, ChatPage.class);
            intent.putExtra("username", username);
            intent.putExtra("photo", photo);
            intent.putExtra("email", email);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
