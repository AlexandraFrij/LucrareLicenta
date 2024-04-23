package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ChatPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        Button goBackBtn = findViewById(R.id.goBackBtn);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.goBackBtn)
                {
                    Intent createAccountIntent = new Intent(ChatPage.this, ConversationsPage.class);
                    startActivity(createAccountIntent);
                }
                else
                {
                    throw new IllegalArgumentException("Buton necunoscut");
                }
            }
        };

        goBackBtn.setOnClickListener(buttonClickListener);
    }
}
