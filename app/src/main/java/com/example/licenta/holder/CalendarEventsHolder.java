package com.example.licenta.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.licenta.R;

public class CalendarEventsHolder extends RecyclerView.ViewHolder {
    public TextView nameTextView;
    public TextView dateTextView;
    public TextView timeTextView;

    public CalendarEventsHolder(@NonNull View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.event_name);
        dateTextView = itemView.findViewById(R.id.event_date);
        timeTextView = itemView.findViewById(R.id.event_time);
    }
}
