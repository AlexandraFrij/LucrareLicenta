package com.example.licenta;

import android.content.Context;
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
        holder.username.setText(items.get(position).getUsername());
        holder.profilePicture.setImageResource(items.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
