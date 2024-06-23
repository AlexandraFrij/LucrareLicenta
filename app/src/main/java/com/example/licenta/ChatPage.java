package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.adapter.ChatAdapter;
import com.example.licenta.item.ChatRecyclerViewItem;
import com.example.licenta.util.AndroidUtil;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class ChatPage extends AppCompatActivity {
    private FirebaseHelper dbHelper;
    private RecyclerView messagesView;
    private String currentUserEmail;
    private String otherUserEmail;

    private static final String TAG = "ChatPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        dbHelper = new FirebaseHelper();


        Button goBackBtn = findViewById(R.id.goBackBtn);
        ImageButton send = findViewById(R.id.sendBtn);
        EditText userMessage = findViewById(R.id.message_input);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        currentUserEmail = sp.getString("email", null);

        ImageView profilePicture = findViewById(R.id.profilePhoto);
        TextView otherUsername = findViewById(R.id.other_username);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        int photo = intent.getIntExtra("photo", -1);
        otherUserEmail = intent.getStringExtra("email");


        otherUsername.setText(username);
        profilePicture.setImageResource(photo);
        FirebaseHelper.getProfilePicture(otherUserEmail).getDownloadUrl()
                .addOnCompleteListener( task ->
                {
                    if(task.isSuccessful())
                    {
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilePicture(getApplicationContext(), uri, profilePicture);
                    }
                });

        messagesView = findViewById(R.id.messages);
        messagesView.setLayoutManager(new LinearLayoutManager(this));


        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conversation = new Intent(ChatPage.this, ConversationsPage.class);
                startActivity(conversation);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = userMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message, currentUserEmail, otherUserEmail);
                    userMessage.setText("");
                    displayMessages(currentUserEmail, otherUserEmail, messagesView);
                }
            }
        });

        displayMessages(currentUserEmail, otherUserEmail, messagesView);
    }

    public void createChatRoom(String user1Email, String user2Email) {
        dbHelper.chatRoomExists(user1Email, user2Email)
                        .addOnSuccessListener( task ->{
                            if(!task)
                                dbHelper.insertChatRoom(user1Email, user2Email);
                        });
    }

    public void sendMessage(String message, String senderEmail, String receiverEmail) {
        dbHelper.retrieveChatRoomId(senderEmail, receiverEmail)
                .addOnSuccessListener(chatRoomId -> {
                    if (chatRoomId != null) {
                        dbHelper.insertChatMessage(chatRoomId, senderEmail, message);
                        RecyclerView messagesView = findViewById(R.id.messages);
                        displayMessages(senderEmail, receiverEmail, messagesView);
                    }
                });
    }

    public void displayMessages(String currentUserEmail, String otherUserEmail, RecyclerView messagesView) {
        Log.d(TAG, "Inainte de retrieve chatRoomId");
        dbHelper.retrieveChatRoomId(currentUserEmail, otherUserEmail)
                .addOnSuccessListener(chatRoomId -> {
                    if (chatRoomId != null && !chatRoomId.isEmpty()) {
                        Log.d(TAG, "Inainte de retrieve displayChatMessages, "+ chatRoomId);
                        displayChatMessages(chatRoomId);
                    } else {
                        Log.d(TAG, "Inainte de retrieve createChatRoom");
                        createChatRoom(currentUserEmail, otherUserEmail);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error retrieving chat room ID", e));
    }

    private void displayChatMessages(String chatRoomId) {
        Log.d(TAG, "Inainte de extractMessages");
        dbHelper.extractMessages(chatRoomId)
                .addOnSuccessListener(messages -> {
                    if (messages != null) {
                        Log.d(TAG, "In if messages != null");
                        List<String> senderList = messages.getSender();
                        List<String> messageList = messages.getMessages();
                        List<ChatRecyclerViewItem> chatItems = new ArrayList<>();
                        for (int i = 0; i < senderList.size(); i++) {
                            String sender = senderList.get(i);
                            String message = messageList.get(i);
                            chatItems.add(new ChatRecyclerViewItem(message, sender));
                        }
                        if (chatItems.isEmpty()) {
                            Log.d(TAG, "In if (chatItems.isEmpty())");
//                            messagesView.setVisibility(View.GONE);
//                            noMessagesView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d(TAG, "In else dupa if (chatItems.isEmpty())");
//                            noMessagesView.setVisibility(View.GONE);
//                            messagesView.setVisibility(View.VISIBLE);
                            ChatAdapter chatAdapter = new ChatAdapter(this, chatItems, currentUserEmail);
                            messagesView.setLayoutManager(new LinearLayoutManager(ChatPage.this));
                            messagesView.setAdapter(chatAdapter);
                            messagesView.scrollToPosition(chatItems.size() - 1);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error extracting messages", e));
    }
}
