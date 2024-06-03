package com.example.licenta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.DatabaseHelper;
import com.example.licenta.EditCalendarEvent;
import com.example.licenta.R;
import com.example.licenta.holder.CalendarEventsHolder;
import com.example.licenta.item.CalendarEventsRecyclerViewerItem;

import java.util.List;


public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventsHolder> {
    private final Context context;
    private final List<CalendarEventsRecyclerViewerItem> items;
    private final DatabaseHelper dbHelper;
    String currentUser;

    public CalendarEventAdapter(Context context, List<CalendarEventsRecyclerViewerItem> items, String currentUser) {
        this.context = context;
        this.items = items;
        this.currentUser =currentUser;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CalendarEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CalendarEventsHolder(LayoutInflater.from(context).inflate(R.layout.calendar_events, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarEventsHolder holder, int position) {
        CalendarEventsRecyclerViewerItem calendarItem = items.get(position);

        String name = calendarItem.getName();
        String date = calendarItem.getDate();
        String time = calendarItem.getTime();

        holder.dateTextView.setText(date);
        holder.nameTextView.setText(name);
        holder.timeTextView.setText(time);


        if(dbHelper.userIsStudent(currentUser))
        {
            holder.deleteButton.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.GONE);
            holder.attendanceButton.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.attendanceButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.editButton.setVisibility(View.VISIBLE);
        }
        holder.deleteButton.setOnClickListener(v -> {
            deleteEvent(name, date, time);
            items.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
        });
        holder.editButton.setOnClickListener(v->
        {
            Intent intent = new Intent(context, EditCalendarEvent.class);
            String[] parts = time.split(" - ");
            String startHour = parts[0];
            String endHour = parts[1];
            intent.putExtra("eventName", name);
            intent.putExtra("date", date);
            intent.putExtra("startHour", startHour);
            intent.putExtra("endHour", endHour);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });
        String lastName = dbHelper.retrieveLastName(currentUser);
        String firstName = dbHelper.retrieveFirstName(currentUser);
        String idNumber = dbHelper.retrieveIdNumber(currentUser);
        if(dbHelper.studentWasPresent(idNumber, date, name))
        {
            holder.attendanceButton.setEnabled(false);
            holder.attendanceButton.setText("Prezenta marcata");

        }
        holder.attendanceButton.setOnClickListener(v ->
        {
            dbHelper.insertStudentAttendance(lastName, firstName, idNumber, name, date);
            holder.attendanceButton.setEnabled(false);
            holder.attendanceButton.setText("Prezenta marcata");
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void deleteEvent(String name, String date, String time) {
        String[] parts = time.split(" - ");
        String startHour = parts[0];
        String endHour = parts[1];
        dbHelper.deleteEvent(name, date, startHour, endHour);
    }

}
