package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.licenta.model.Messages;

import java.util.ArrayList;
import java.util.List;

public class ChatPage extends AppCompatActivity
{
    private DatabaseHelper dbHelper;
    private RecyclerView messagesView;
    private String currentUserEmail;
    private String otherUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        dbHelper = new DatabaseHelper(this);

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
        createChatRoom(currentUserEmail, otherUserEmail);

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

    public void createChatRoom(String user1Email, String user2Email)
    {
        if (!dbHelper.chatRoomExists(user1Email, user2Email))
            dbHelper.insertChatRoom(user1Email, user2Email);
    }

    public void sendMessage(String message, String senderEmail, String receiverEmail) {
        int chatRoomId = dbHelper.retrieveChatRoomId(senderEmail, receiverEmail);
        dbHelper.insertChatMessage(chatRoomId, senderEmail, message);
        RecyclerView messagesView = findViewById(R.id.messages);
        displayMessages(senderEmail, receiverEmail, messagesView);
    }


    public void displayMessages(String currentUserEmail, String otherUserEmail, RecyclerView messagesView)
    {
        int chatRoomId = dbHelper.retrieveChatRoomId(currentUserEmail, otherUserEmail);
        Messages messages = dbHelper.extractMessages(chatRoomId);
        List<String> senderList = messages.getSender();
        List<String> messageList = messages.getMessages();
        List<ChatRecyclerViewItem> chatItems = new ArrayList<>();
        for (int i = 0; i < senderList.size(); i++) {
            String sender = senderList.get(i);
            String message = messageList.get(i);
            chatItems.add(new ChatRecyclerViewItem(message, sender));
        }
        ChatAdapter chatAdapter = new ChatAdapter(this, chatItems, currentUserEmail);
        messagesView.setLayoutManager(new LinearLayoutManager(ChatPage.this));
        messagesView.setAdapter(chatAdapter);

        if (chatItems.size() > 0) {
            messagesView.scrollToPosition(chatItems.size() - 1);
        }
    }

}
