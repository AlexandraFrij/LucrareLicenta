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

public class ChatPage extends AppCompatActivity
{
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        Button goBackBtn = findViewById(R.id.goBackBtn);
        ImageButton send = findViewById(R.id.sendBtn);
        EditText userMessage = findViewById(R.id.message_input);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String currentUserEmail = sp.getString("email", null);

        ImageView profilePicture = findViewById(R.id.profilePhoto);
        TextView otherUsername = findViewById(R.id.other_username);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        int photo = intent.getIntExtra("photo", -1);
        String otherUserEmail = intent.getStringExtra("email");

        otherUsername.setText(username);
        profilePicture.setImageResource(photo);
        createChatRoom(currentUserEmail, otherUserEmail);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (v.getId() == R.id.goBackBtn)
                {
                    Intent conversation = new Intent(ChatPage.this, SearchUser.class);
                    startActivity(conversation );
                }
                else if(v.getId() == R.id.sendBtn)
                {
                    String message = userMessage.getText().toString().trim();
                    if (!message.isEmpty())
                    {
                        sendMessage(message, currentUserEmail, otherUserEmail);
                        userMessage.setText("");
                    }

                }
                else
                {
                    throw new IllegalArgumentException("Unknown button");
                }
            }
        };
        goBackBtn.setOnClickListener(buttonClickListener);
        send.setOnClickListener(buttonClickListener);

    }
    public void createChatRoom(String user1Email, String user2Email)
    {
        if (! dbHelper.chatRoomExists(user1Email, user2Email))
            dbHelper.insertChatRoom(user1Email, user2Email);

    }
    public void sendMessage(String message, String senderEmail, String receiverEmail)
    {
        int charRoomId = dbHelper.retrieveChatRoomId(senderEmail, receiverEmail);
        dbHelper.insertChatMessage(charRoomId, senderEmail,message);

    }
}
