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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class ProfHomePage extends AppCompatActivity {
    TextView usernameText;
    TextView emailText;
    CalendarView calendar;
    Button addEventButton, seeAttendancesList;
    ImageView profileView;
    String selectedDate;
    String userEmail;

    private FirebaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_home_page);
        dbHelper = new FirebaseHelper();
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);
        calendar = findViewById(R.id.calendar);
        addEventButton = findViewById(R.id.addEventBtn);
        seeAttendancesList = findViewById(R.id.seeAttendancesList);
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
        emailText.setText(userEmail);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(new Date(calendar.getDate()));
        showEvents();
        addEventButton.setText("Adauga eveniment pentru " + selectedDate);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                checkDateAndDisableButton();
                showEvents();
                addEventButton.setText("Adauga eveniment pentru " + selectedDate);
            }
        });
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfHomePage.this, AddCalendarEvent.class);
                intent.putExtra("date", selectedDate);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
        });
        seeAttendancesList.setOnClickListener(v ->
        {
            Intent intent = new Intent(ProfHomePage.this, StudentAttendances.class);
            intent.putExtra("lastPage", "ProfHomePage");
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
                Intent profile = new Intent(ProfHomePage.this, ProfilePage.class);
                startActivity(profile);
            }
        });

        checkDateAndDisableButton();
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
                        calendarView.setLayoutManager(new LinearLayoutManager(ProfHomePage.this));
                        calendarView.setAdapter(new CalendarEventAdapter(getApplicationContext(), events, userEmail));
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }


    private void checkDateAndDisableButton() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date selected = sdf.parse(selectedDate);
            if (selected.before(today.getTime())) {
                addEventButton.setEnabled(false);
                addEventButton.setAlpha(0.5f);
            } else {
                addEventButton.setEnabled(true);
                addEventButton.setAlpha(1.0f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
