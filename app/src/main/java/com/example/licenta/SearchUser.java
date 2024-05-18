package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;

import com.example.licenta.adapter.SearchUserAdapter;
import com.example.licenta.item.SearchUserRecyclerViewItem;
import com.example.licenta.model.Users;

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
                if (usernamePattern.length() < 3)
                {
                    usernameEdit.setError("Utilizatorul nu exista");
                }
                else
                {
                    Users userInfo = dbHelper.extractUsername(usernamePattern);
                    List<String> usernames = userInfo.getUsernames();
                    List<String> emails = userInfo.getEmails();
                    List<SearchUserRecyclerViewItem> users = new ArrayList<SearchUserRecyclerViewItem>();
                    for (int i = 0; i < usernames.size(); i++)
                    {
                        users.add(new SearchUserRecyclerViewItem(usernames.get(i), R.drawable.profile_pic,emails.get(i) ));
                    }
                    usersView.setLayoutManager(new LinearLayoutManager(SearchUser.this));
                    usersView.setAdapter(new SearchUserAdapter(getApplicationContext(), users));
                }


            }
        });
    }


}
