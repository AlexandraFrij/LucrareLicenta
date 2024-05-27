package com.example.licenta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.example.licenta.holder.CalendarEventsHolder;
import com.example.licenta.item.CalendarEventsRecyclerViewerItem;

import java.util.List;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventsHolder> {
    Context context;
    List<CalendarEventsRecyclerViewerItem> items;

    public CalendarEventAdapter(Context context, List<CalendarEventsRecyclerViewerItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CalendarEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CalendarEventsHolder((LayoutInflater.from(context).inflate(R.layout.calendar_events_prof, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarEventsHolder holder, int position) {
        CalendarEventsRecyclerViewerItem calendarItem = items.get(position);
        holder.dateTextView.setText(calendarItem.getDate());
        holder.nameTextView.setText(calendarItem.getName());
        holder.timeTextView.setText(calendarItem.getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
