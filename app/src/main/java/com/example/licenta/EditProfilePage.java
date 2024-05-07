package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_page);
        Button saveBtn = findViewById(R.id.saveBtn);
        Button leaveBtn = findViewById(R.id.leaveBtn);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.saveBtn)
                {
                    Intent edit = new Intent(EditProfilePage.this, EditProfilePage.class);
                    startActivity(edit);
                }
                else
                if (v.getId() == R.id.leaveBtn)
                {
                    Intent profile = new Intent(EditProfilePage.this, ProfilePage.class);
                    startActivity(profile);
                }
                else
                {
                    throw new IllegalArgumentException("Unknown button");
                }
            }
        };

        saveBtn.setOnClickListener(buttonClickListener);
        leaveBtn.setOnClickListener(buttonClickListener);
    }
}
