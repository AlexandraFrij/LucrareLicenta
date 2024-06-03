package com.example.licenta.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.licenta.R;

public class AttendanceHolder extends RecyclerView.ViewHolder {
    public TextView nameTextView;
    public TextView dateTextView;

    public AttendanceHolder(@NonNull View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.event_name);
        dateTextView = itemView.findViewById(R.id.event_date);
    }
}
