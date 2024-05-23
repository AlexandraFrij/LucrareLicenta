package com.example.licenta;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class HomePage extends AppCompatActivity
{
    TextView usernameText;
    TextView emailText;
    RelativeLayout calendarEvent;
    CalendarView calendar;
    EditText addedEvent;
    EditText addedEventHour;
    int hour, minute;

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        dbHelper = new DatabaseHelper(this);
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);
        calendarEvent = findViewById(R.id.calendar_events);
        calendar = findViewById(R.id.calendar);
        addedEvent = findViewById(R.id.added_event);
        addedEventHour = findViewById(R.id.added_event_hour);

        calendarEvent.setVisibility(View.GONE);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String email = sp.getString("email", null);
        String[] info = dbHelper.retrieveDataWithEmail(email);
        usernameText.setText(info[1]);
        emailText.setText(email);

        addedEventHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogue();

            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendarEvent.setVisibility(View.VISIBLE);
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
    private void timePickerDialogue()
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                addedEventHour.setText(String.format(Locale.getDefault(),"%02d:%02d", selectedHour, selectedMinute));

            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog= new TimePickerDialog(this,style,  onTimeSetListener,hour, minute, true );
        timePickerDialog.show();
    }
}
