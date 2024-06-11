package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.adapter.NotificationAdapter;
import com.example.licenta.item.NotificationRecyclerViewItem;
import com.example.licenta.model.Notification;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
public class NotificationsPage extends AppCompatActivity {
    private FirebaseHelper dbHelper;
    private String userEmail, userStatus;
    private EditText addAnnouncementText;
    private Button addAnnouncementBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);

        dbHelper = new FirebaseHelper();
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        userEmail = sp.getString("email", null);
        userStatus = sp.getString("status", null);
        addAnnouncementText = findViewById(R.id.addAnnouncement);
        addAnnouncementBtn = findViewById(R.id.addAnnouncementBtn);
        if(userStatus.equals("student"))
        {
            addAnnouncementText.setVisibility(View.GONE);
            addAnnouncementBtn.setVisibility(View.GONE);

        }
        showNotifications(userEmail);

        BottomNavigationView bottomNavBar = findViewById(R.id.bottom_nav_bar);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onNavigationItemSelectedHandler(item);
            }
        });

        bottomNavBar.setSelectedItemId(R.id.notificationsBtn);
    }
    protected boolean onNavigationItemSelectedHandler(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            if(userStatus.equals("professor"))
            {
                startActivity(new Intent(this, ProfHomePage.class));
                return true;
            }
            else
            {
                startActivity(new Intent(this, StudentHomePage.class));
                return true;
            }

        } else if (item.getItemId() == R.id.messagesBtn) {
            startActivity(new Intent(this, ConversationsPage.class));
            return true;

        } else if (item.getItemId() == R.id.notificationsBtn) {
            return true;

        } else {
            return false;
        }
    }
    private void showNotifications(String userEmail) {
        RecyclerView notificationsRecyclerView = findViewById(R.id.notifications_viewer);
        dbHelper.retrieveNotifications(userEmail)
                .addOnSuccessListener(notification -> {
                    if(notification != null)
                    {
                        List<String> contents = notification.getContent();
                        List<String> dates = notification.getDate();
                        List<NotificationRecyclerViewItem> notifications = new ArrayList<>();

                        for (int i = 0; i < contents.size(); i++)
                        {
                            String notificationContent = "Un nou " + contents.get(i) +" a fost adaugat in calendar!";
                            String notificationDate = "Creat la data " + dates.get(i);
                            notifications.add(new NotificationRecyclerViewItem(notificationContent, notificationDate, R.drawable.baseline_calendar_month_24));
                        }
                        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationsPage.this));
                        notificationsRecyclerView.setAdapter(new NotificationAdapter(getApplicationContext(), notifications));
                    }

                });


    }
}
