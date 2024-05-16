package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchUser extends AppCompatActivity
{
    private DatabaseHelper dbHelper;
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new DatabaseHelper(this);

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
            public void onClick(View v)
            {
                String usernamePattern = usernameEdit.getText().toString().trim();
                if (usernamePattern.length() < 3 || usernamePattern.isEmpty() )
                {
                    usernameEdit.setError("Utilizatorul nu exista");
                }
                else
                {
                    List<String> usernames = dbHelper.extractUsername(usernamePattern);
                    List<RecycleViewItem> users = new ArrayList<RecycleViewItem>();
                    for (int i = 0; i < usernames.size(); i++)
                    {
                        users.add(new RecycleViewItem(usernames.get(i), R.drawable.profile_pic));
                    }
                    usersView.setLayoutManager(new LinearLayoutManager(SearchUser.this));
                    usersView.setAdapter(new MyAdapter(getApplicationContext(), users));
                }


            }
        });
    }


}
