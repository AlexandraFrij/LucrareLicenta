package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class SearchUser extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);

        Button goBackBtn = findViewById(R.id.goBackBtn);
        Button search = findViewById(R.id.searchBtn);
        EditText usernameEdit = findViewById(R.id.search_user);
        RecyclerView usersView = findViewById(R.id.user_viewer);

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchUser.this, ConversationsPage.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString().trim();
                if (username.length() < 3 || username.isEmpty() )
                {
                    usernameEdit.setError("Utilizatorul nu exista");
                    return;
                }

            }
        });
    }


}
