package com.example.licenta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.EditCalendarEvent;
import com.example.licenta.FirebaseHelper;

import com.example.licenta.R;

import com.example.licenta.holder.CalendarEventsHolder;
import com.example.licenta.item.CalendarEventsRecyclerViewerItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventsHolder> {
    private final Context context;
    private final List<CalendarEventsRecyclerViewerItem> items;
    private final FirebaseHelper dbHelper;
    String currentUser;

    public CalendarEventAdapter(Context context, List<CalendarEventsRecyclerViewerItem> items, String currentUser) {
        this.context = context;
        this.items = items;
        this.currentUser =currentUser;
        dbHelper = new FirebaseHelper();
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
        String room = calendarItem.getRoom();

        holder.dateTextView.setText(date);
        holder.nameTextView.setText(name);
        holder.timeTextView.setText(time);
        holder.roomTextView.setText(room);

        dbHelper.userIsStudent(currentUser)
                .addOnSuccessListener(bool -> {
                    if (bool) {
                        holder.deleteButton.setVisibility(View.GONE);
                        holder.editButton.setVisibility(View.GONE);
                        holder.attendanceButton.setVisibility(View.VISIBLE);
                        checkAttendanceStatus(holder, name, date, time);
                    } else {
                        holder.attendanceButton.setVisibility(View.GONE);
                        dbHelper.userAddedEvent(currentUser, name, date, time)
                                .addOnSuccessListener(b -> {
                                    if (b && dateAfterToday(date))
                                    {
                                        holder.deleteButton.setEnabled(true);
                                        holder.deleteButton.setAlpha(1.0f);
                                        holder.editButton.setEnabled(true);
                                        holder.editButton.setAlpha(1.0f);
                                    }
                                    else {
                                        holder.deleteButton.setEnabled(false);
                                        holder.deleteButton.setAlpha(0.5f);
                                        holder.editButton.setEnabled(false);
                                        holder.editButton.setAlpha(0.5f);
                                    }
                                });
                    }
                });


        holder.deleteButton.setOnClickListener(v -> {
            deleteEvent(name, date, time, room);
            items.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
        });
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditCalendarEvent.class);
            String[] parts = time.split(" - ");
            String startHour = parts[0];
            String endHour = parts[1];
            intent.putExtra("eventName", name);
            intent.putExtra("date", date);
            intent.putExtra("startHour", startHour);
            intent.putExtra("endHour", endHour);
            intent.putExtra("email", currentUser);
            intent.putExtra("room", room);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    private void checkAttendanceStatus(@NonNull CalendarEventsHolder holder, String name, String date, String time) {
        String[] lastName = new String[]{""};
        String[] firstName = new String[]{""};
        String[] idNumber = new String[]{""};

        dbHelper.retrieveLastName(currentUser)
                .addOnSuccessListener(n -> {
                    lastName[0] = n;
                    dbHelper.retrieveFirstName(currentUser)
                            .addOnSuccessListener(fn -> {
                                firstName[0] = fn;
                                dbHelper.retrieveIdNumber(currentUser)
                                        .addOnSuccessListener(idNb -> {
                                            idNumber[0] = idNb;
                                            dbHelper.studentWasPresent(idNumber[0], date, name)
                                                    .addOnSuccessListener(bool -> {
                                                        if (bool) {
                                                            holder.attendanceButton.setEnabled(false);
                                                            holder.attendanceButton.setText("Prezenta marcata");
                                                            holder.attendanceButton.setAlpha(0.5f);
                                                        } else if (dateAfterToday(date)) {
                                                            holder.attendanceButton.setEnabled(false);
                                                            holder.attendanceButton.setAlpha(0.5f);
                                                        } else {
                                                            holder.attendanceButton.setEnabled(true);
                                                            holder.attendanceButton.setAlpha(1.0f);
                                                            holder.attendanceButton.setText("Marcheaza prezenta");
                                                            holder.attendanceButton.setOnClickListener(v -> {
                                                                dbHelper.insertStudentAttendance(lastName[0], firstName[0], idNumber[0], name, date);
                                                                holder.attendanceButton.setEnabled(false);
                                                                holder.attendanceButton.setText("Prezenta marcata");
                                                                holder.attendanceButton.setAlpha(0.5f);
                                                            });
                                                        }
                                                    });
                                        });
                            });
                });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void deleteEvent(String name, String date, String time, String room) {
        String[] parts = time.split(" - ");
        String startHour = parts[0];
        String endHour = parts[1];
        dbHelper.deleteEvent(name, date, startHour, endHour, room);
    }

    private boolean dateAfterToday(String date) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date selected = sdf.parse(date);
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.setTime(selected);
            selectedDate.set(Calendar.HOUR_OF_DAY, 0);
            selectedDate.set(Calendar.MINUTE, 0);
            selectedDate.set(Calendar.SECOND, 0);
            selectedDate.set(Calendar.MILLISECOND, 0);

            if (selectedDate.equals(today) || selectedDate.after(today)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
