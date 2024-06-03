package com.example.licenta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.example.licenta.item.AttendanceRecyclerViewItem;

import java.util.List;
import com.example.licenta.holder.AttendanceHolder;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceHolder> {
    private final Context context;
    private final List<AttendanceRecyclerViewItem> items;

    public AttendanceAdapter(Context context, List<AttendanceRecyclerViewItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendanceHolder((LayoutInflater.from(context).inflate(R.layout.attendances_list, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceHolder holder, int position) {
        AttendanceRecyclerViewItem attendanceItem = items.get(position);
        String name = attendanceItem.getName();
        String date = attendanceItem.getDate();

        holder.nameTextView.setText(name);
        holder.dateTextView.setText(date);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
