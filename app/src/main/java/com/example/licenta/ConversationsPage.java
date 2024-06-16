package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class ConversationsPage extends AppCompatActivity {
    private FirebaseHelper dbHelper;
    private String userEmail, userStatus;
    private static final String TAG = "ConversationsPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new FirebaseHelper();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversations_page);

        Button search = findViewById(R.id.search);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userEmail = sp.getString("email", null);
        userStatus = sp.getString("status", null);
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
            public void onClick(View v) {
                Intent intent = new Intent(ConversationsPage.this, SearchUser.class);
                startActivity(intent);
            }
        });
    }

    protected boolean onNavigationItemSelectedHandler(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            if (userStatus.equals("professor")) {
                startActivity(new Intent(this, ProfHomePage.class));
                return true;
            } else {
                startActivity(new Intent(this, StudentHomePage.class));
                return true;
            }
        } else if (item.getItemId() == R.id.messagesBtn) {
            return true;
        } else if (item.getItemId() == R.id.notificationsBtn) {
            startActivity(new Intent(this, NotificationsPage.class));
            return true;
        } else {
            return false;
        }
    }

    public void showRecentChats(String userEmail) {
        RecyclerView recentChats = findViewById(R.id.recent_chats_viewer);
        dbHelper.extractChatRoom(userEmail)
                .addOnSuccessListener(chatRoom -> {
                    if (chatRoom != null) {
                        List<String> chatRoomIds = chatRoom.getChatRoomId();
                        List<String> user1Email = chatRoom.getUserEmail();
                        List<String> user2Email = chatRoom.getOtherUserEmail();
                        List<RecentChatsRecyclerViewItem> items = new ArrayList<>();

                        for (int i = 0; i < chatRoomIds.size(); i++) {
                            String chatRoomId = chatRoomIds.get(i);
                            String otherUserEmail = user1Email.get(i).equals(userEmail) ? user2Email.get(i) : user1Email.get(i);

                            dbHelper.getRecentMessage(chatRoomId)
                                    .addOnSuccessListener(recentMessage -> {
                                        dbHelper.retrieveDataWithEmail(otherUserEmail)
                                                .addOnSuccessListener(userInfo -> {
                                                    if (userInfo != null && recentMessage != null) {
                                                        String username = userInfo[0] + " " + userInfo[1];
                                                        String recentMessageContent = recentMessage[0];
                                                        String recentMessageTime = recentMessage[1];
                                                        items.add(new RecentChatsRecyclerViewItem(username, recentMessageContent, otherUserEmail, R.drawable.profile_pic, recentMessageTime));

                                                        if (items.size() == chatRoomIds.size()) {
                                                            recentChats.setLayoutManager(new LinearLayoutManager(ConversationsPage.this));
                                                            recentChats.setAdapter(new RecentChatsAdapter(getApplicationContext(), items));
                                                        }
                                                    }
                                                });
                                    });
                        }
                    }
                });
    }

}
