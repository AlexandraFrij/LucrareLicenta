package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.adapter.NotificationAdapter;
import com.example.licenta.item.NotificationRecyclerViewItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;

public class NotificationsPage extends AppCompatActivity {
    private FirebaseHelper dbHelper;
    private String userEmail, userStatus;
    private EditText addAnnouncementText;
    private Button addAnnouncementBtn;
    private AlertDialogMessages alertDialogMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);

        dbHelper = new FirebaseHelper();
        alertDialogMessages = new AlertDialogMessages();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        userEmail = sp.getString("email", null);
        userStatus = sp.getString("status", null);
        addAnnouncementText = findViewById(R.id.addAnnouncement);
        addAnnouncementBtn = findViewById(R.id.addAnnouncementBtn);

        showNotifications();

        addAnnouncementBtn.setOnClickListener(v ->
        {
            String announcement = addAnnouncementText.getText().toString().trim();
            if(announcement.isEmpty())
            {
                String message = "Adaugati un text al anuntului!";
                showError(message);
            }
            else
            {
                dbHelper.insertNotification(announcement, userEmail, "announcement");
                showNotifications();
                addAnnouncementText.setText("");
                addAnnouncementText.setHint("Adauga un anunt!");
            }
        });

        if(userStatus.equals("student"))
        {
            addAnnouncementText.setVisibility(View.GONE);
            addAnnouncementBtn.setVisibility(View.GONE);

        }


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
    private void showNotifications() {
        RecyclerView notificationsRecyclerView = findViewById(R.id.notifications_viewer);
        dbHelper.retrieveNotifications()
                .addOnSuccessListener(notification -> {
                    if(notification != null)
                    {
                        List<String> contents = notification.getContent();
                        List<String> dates = notification.getDate();
                        List<String> types = notification.getType();
                        List<NotificationRecyclerViewItem> notifications = new ArrayList<>();

                        for (int i = 0; i < contents.size(); i++)
                        {
                            String notificationContent = contents.get(i);
                            String notificationDate = "Creat la data " + dates.get(i);
                            String notificationType = types.get(i);
                            if(notificationType.equals("calendarEvent"))
                                notifications.add(new NotificationRecyclerViewItem(notificationContent, notificationDate, R.drawable.baseline_calendar_month_24));
                            else
                                notifications.add(new NotificationRecyclerViewItem(notificationContent, notificationDate, R.drawable.baseline_notification_important_24));

                        }
                        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationsPage.this));
                        notificationsRecyclerView.setAdapter(new NotificationAdapter(getApplicationContext(), notifications));
                    }

                });

    }
    private void showError(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, message));
    }
}
