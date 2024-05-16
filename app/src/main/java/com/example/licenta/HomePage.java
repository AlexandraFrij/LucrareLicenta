package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity
{

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        dbHelper = new DatabaseHelper(this);
        TextView usernameText = findViewById(R.id.username);
        TextView emailText = findViewById(R.id.email);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String email = sp.getString("email", null);
        String[] info = dbHelper.retrieveDataWithEmail(email);
        usernameText.setText(info[1]);
        emailText.setText(email);

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
}
