package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.adapter.RecentChatsAdapter;
import com.example.licenta.item.RecentChatsRecyclerViewItem;
import com.example.licenta.model.ChatRoom;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ConversationsPage extends AppCompatActivity
{
    private DatabaseHelper dbHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversations_page);

        Button search = findViewById(R.id.search);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userEmail = sp.getString("email", null);
        showRecentChats(userEmail);

        BottomNavigationView bottomNavBar = findViewById(R.id.bottom_nav_bar);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onNavigationItemSelectedHandler(item);
            }
        });

        bottomNavBar.setSelectedItemId(R.id.messagesBtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ConversationsPage.this, SearchUser.class);
                startActivity(intent);

            }
        });
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
    public void showRecentChats(String userEmail)
    {
        RecyclerView recentChats = findViewById(R.id.recent_chats_viewer);
        ChatRoom chatroom = dbHelper.extractChatRoom(userEmail);
        List<RecentChatsRecyclerViewItem> items = new ArrayList<>();
        List<Integer> chatRoomIds = chatroom.getChatRoomId();
        List<String> user1Email = chatroom.getUserEmail();
        List<String> user2Email = chatroom.getOtherUserEmail();
        for( int i = 0; i < chatRoomIds.size(); i++)
        {
            String[] result = dbHelper.getRecentMessage(chatRoomIds.get(i));
            String otherUserEmail = user1Email.get(i).equals(userEmail) ? user2Email.get(i) : user1Email.get(i);
            String[] info = dbHelper.retrieveDataWithEmail(otherUserEmail);
            String username = info[0] + " " + info[1];
            items.add(new RecentChatsRecyclerViewItem(username, result[0], otherUserEmail, R.drawable.profile_pic,result[1] ));

        }
        recentChats.setLayoutManager(new LinearLayoutManager(ConversationsPage.this));
        recentChats.setAdapter(new RecentChatsAdapter(getApplicationContext(), items));

    }
}
