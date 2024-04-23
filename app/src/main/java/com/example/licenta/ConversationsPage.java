package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConversationsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversations_page);

        BottomNavigationView bottomNavBar = findViewById(R.id.bottom_nav_bar);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onNavigationItemSelectedHandler(item);
            }
        });

        bottomNavBar.setSelectedItemId(R.id.messagesBtn);
    }
    protected boolean onNavigationItemSelectedHandler(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            startActivity(new Intent(this, HomePage.class));
            return true;

        } else if (item.getItemId() == R.id.messagesBtn) {
            return true;

        } else if (item.getItemId() == R.id.notificationsBtn) {
            startActivity(new Intent(this, NotificationsPage.class));
            return true;

        } else {
            return false;
        }
    }
}
