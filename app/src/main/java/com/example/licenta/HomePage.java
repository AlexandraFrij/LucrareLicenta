package com.example.licenta;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.adapter.CalendarEventAdapter;
import com.example.licenta.adapter.RecentChatsAdapter;
import com.example.licenta.item.CalendarEventsRecyclerViewerItem;
import com.example.licenta.model.CalendarEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomePage extends AppCompatActivity
{
    TextView usernameText;
    TextView emailText;
    RelativeLayout calendarEvent;
    CalendarView calendar;
    EditText addedEvent;
    TextView startHourView, endHourView;
    Button addButton;
    int startHour, startMinute, endHour, endMinute;
    String selectedDate;

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        dbHelper = new DatabaseHelper(this);
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);
        calendarEvent = findViewById(R.id.add_calendar_events);
        calendar = findViewById(R.id.calendar);
        addedEvent = findViewById(R.id.added_event);
        startHourView = findViewById(R.id.start_time);
        endHourView = findViewById(R.id.end_time);
        addButton = findViewById(R.id.addBtn);

        calendarEvent.setVisibility(View.GONE);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String email = sp.getString("email", null);
        String[] info = dbHelper.retrieveDataWithEmail(email);
        usernameText.setText(info[1]);
        emailText.setText(email);

        startHourView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogue(startHourView, true);
            }
        });

        endHourView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogue(endHourView, false);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendarEvent.setVisibility(View.VISIBLE);
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                showEvents();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = addedEvent.getText().toString().trim();
                String start = startHourView.getText().toString().trim();
                String end = endHourView.getText().toString().trim();

                if (event.isEmpty()) {
                    String message = "Introduceti denumire eveniment";
                    Toast.makeText(HomePage.this, message, Toast.LENGTH_SHORT).show();
                } else if (start.contains("Alege ora") || end.contains("Alege ora")) {
                    String message = "Alegeti o ora";
                    Toast.makeText(HomePage.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = verifyTimeInterval(startHour, startMinute, endHour, endMinute);
                    if (!message.equals("ok")) {
                        Toast.makeText(HomePage.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
                        String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
                        dbHelper.addEvent(event,selectedDate,  startTime, endTime);
                        addedEvent.setText("");
                        addedEvent.setHint("Denumire eveniment");
                        startHourView.setText("Alege ora inceput");
                        endHourView.setText("Alege ora sfarsit");
                    }
                }
            }
        });

        BottomNavigationView bottomNavBar = findViewById(R.id.bottom_nav_bar);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onNavigationItemSelectedHandler(item);
            }
        });

        bottomNavBar.setSelectedItemId(R.id.homeBtn);
        ImageView imageView = findViewById(R.id.profileImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(HomePage.this, ProfilePage.class);
                startActivity(profile);
            }
        });
    }

    protected boolean onNavigationItemSelectedHandler(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            return true;
        } else if (item.getItemId() == R.id.messagesBtn) {
            startActivity(new Intent(this, ConversationsPage.class));
            return true;
        } else if (item.getItemId() == R.id.notificationsBtn) {
            startActivity(new Intent(this, NotificationsPage.class));
            return true;
        } else {
            return false;
        }
    }

    private void timePickerDialogue(TextView textView, boolean isStartTime) {
        final int[] hour = {isStartTime ? startHour : endHour};
        final int[] minute = {isStartTime ? startMinute : endMinute};

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour[0] = selectedHour;
                minute[0] = selectedMinute;
                textView.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));

                if (isStartTime) {
                    startHour = selectedHour;
                    startMinute = selectedMinute;
                } else {
                    endHour = selectedHour;
                    endMinute = selectedMinute;
                }
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour[0], minute[0], true);
        timePickerDialog.show();
    }

    private String verifyTimeInterval(int startHour, int startMinute, int endHour, int endMinute) {
        String message = "ok";
        if (startHour < 7)
            message = "Ora de inceput invalida!";
        if (endHour > 21)
            message = "Ora de incheiere invalida!";
        if (endHour < startHour)
            message = "Interval invalid!";
        if (endHour == startHour && endMinute <= startMinute)
            message = "Interval invalid!";

        if (message.equals("ok")) {
            String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
            String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
            if (!dbHelper.timeIsAvailable(selectedDate, startTime, endTime))
                message = "Intervalul de timp este deja rezervat!";
        }

        return message;
    }
    private void showEvents()
    {
        RecyclerView calendarView = findViewById(R.id.events_viewer);
        CalendarEvent items = dbHelper.extractCalendarEvents();
        List<String> name = items.getName();
        List<String> date = items.getDate();
        List<String> time = items.getTime();
        List<CalendarEventsRecyclerViewerItem> events = new ArrayList<>();
        for(int i = 0; i < name.size(); i++)
        {
            events.add(new CalendarEventsRecyclerViewerItem(name.get(i), date.get(i), time.get(i)));
        }
        calendarView.setLayoutManager(new LinearLayoutManager(HomePage.this));
        calendarView.setAdapter(new CalendarEventAdapter(getApplicationContext(), events));

    }

}
