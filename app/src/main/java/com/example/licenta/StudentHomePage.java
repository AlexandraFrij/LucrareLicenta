package com.example.licenta;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.adapter.CalendarEventAdapter;
import com.example.licenta.item.CalendarEventsRecyclerViewerItem;
import com.example.licenta.model.CalendarEvent;
import com.example.licenta.util.AndroidUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class StudentHomePage extends AppCompatActivity {
    TextView usernameText;
    TextView idNumberText;
    CalendarView calendar;
    Button attendancesButton;
    ImageView profileView;
    String selectedDate;
    String userEmail;

    private FirebaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home_page);
        dbHelper = new FirebaseHelper();
        usernameText = findViewById(R.id.username);
        idNumberText = findViewById(R.id.idNumber);
        calendar = findViewById(R.id.calendar);
        attendancesButton = findViewById(R.id.attendancesBtn);
        profileView = findViewById(R.id.profileImage);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userEmail = sp.getString("email", null);
        dbHelper.retrieveDataWithEmail(userEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String[] info = task.getResult();
                        if (info != null) {
                            usernameText.setText(info[1]);
                        }

                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
        FirebaseHelper.getProfilePicture(userEmail).getDownloadUrl()
                .addOnCompleteListener( task ->
                {
                    if(task.isSuccessful())
                    {
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilePicture(getApplicationContext(), uri, profileView);
                    }

                });
        String[] idNumber = new String[]{""};
        dbHelper.retrieveIdNumber(userEmail)
                .addOnSuccessListener(idNb -> {
                    if(idNb != null)
                    {
                        idNumberText.setText(idNb);
                        idNumber[0] = idNb;
                    }
                });
        idNumberText.setText(idNumber[0]);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(new Date(calendar.getDate()));
        showEvents();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                showEvents();
            }
        });

        attendancesButton.setOnClickListener( v->
        {
            Intent intent = new Intent(StudentHomePage.this, StudentAttendances.class);
            intent.putExtra("idNumber", idNumber[0]);
            intent.putExtra("lastPage", "StudentHomePage");
            startActivity(intent);

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
                Intent profile = new Intent(StudentHomePage.this, ProfilePage.class);
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

    private void showEvents() {
        RecyclerView calendarView = findViewById(R.id.events_viewer);
        dbHelper.extractCalendarEvents(selectedDate)
                .addOnSuccessListener(calendarList -> {
                    if (calendarList != null) {
                        List<CalendarEventsRecyclerViewerItem> events = new ArrayList<>();
                        for (CalendarEvent event : calendarList) {
                            events.add(new CalendarEventsRecyclerViewerItem(
                                    event.getName(),
                                    event.getDate(),
                                    event.getTime(),
                                    event.getRoom()
                            ));
                        }
                        calendarView.setLayoutManager(new LinearLayoutManager(StudentHomePage.this));
                        calendarView.setAdapter(new CalendarEventAdapter(getApplicationContext(), events, userEmail));
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

}

