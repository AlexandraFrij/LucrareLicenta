package com.example.licenta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.example.licenta.holder.AttendanceHolder;
import com.example.licenta.holder.NotificationHolder;
import com.example.licenta.item.AttendanceRecyclerViewItem;
import com.example.licenta.item.NotificationRecyclerViewItem;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {
    private final Context context;
    private final List<NotificationRecyclerViewItem> items;

    public NotificationAdapter(Context context, List<NotificationRecyclerViewItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder((LayoutInflater.from(context).inflate(R.layout.notification, parent, false)));
    }


    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        NotificationRecyclerViewItem notificationItem = items.get(position);
        String content = notificationItem.getContent();
        String date = notificationItem.getAddedAt();
        int image = notificationItem.getImage();

        holder.contentTextView.setText(content);
        holder.addedAtTextView.setText(date);
        holder.imageView.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
