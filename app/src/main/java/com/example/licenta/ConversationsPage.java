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
        Log.i(TAG, "inainte de dbHelper.extractChatRoom(userEmail)");
        dbHelper.extractChatRoom(userEmail)
                .addOnSuccessListener(c -> {
                    if (c != null) {
                        Log.i(TAG, "in if c!= null");
                        ChatRoom chatroom = c;
                        List<RecentChatsRecyclerViewItem> items = new ArrayList<>();
                        List<String> chatRoomIds = chatroom.getChatRoomId();
                        List<String> user1Email = chatroom.getUserEmail();
                        List<String> user2Email = chatroom.getOtherUserEmail();
                        for (int i = 0; i < chatRoomIds.size(); i++) {
                            int index = i;
                            Log.i(TAG, "inainte de dbHelper.getRecentMessage(chatRoomIds.get(index))");
                            dbHelper.getRecentMessage(chatRoomIds.get(index))
                                    .addOnSuccessListener(res -> {
                                        if (res != null) {
                                            String[] result = res;
                                            String otherUserEmail = user1Email.get(index).equals(userEmail) ? user2Email.get(index) : user1Email.get(index);
                                            Log.i(TAG, "inainte de dbHelper.retrieveDataWithEmail(otherUserEmail)");
                                            dbHelper.retrieveDataWithEmail(otherUserEmail)
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            String[] info = task.getResult();
                                                            if (info != null) {
                                                                String username = info[0] + " " + info[1];
                                                                items.add(new RecentChatsRecyclerViewItem(username, result[0], otherUserEmail, R.drawable.profile_pic, result[1]));

                                                                if (index == chatRoomIds.size() - 1) {
                                                                    recentChats.setLayoutManager(new LinearLayoutManager(ConversationsPage.this));
                                                                    recentChats.setAdapter(new RecentChatsAdapter(getApplicationContext(), items));
                                                                }
                                                            }
                                                        } else {
                                                            Exception e = task.getException();
                                                            if (e != null) {
                                                                Log.e(TAG, "Error retrieving data with email: ", e);
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });
    }
}
