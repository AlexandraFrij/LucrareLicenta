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
                    Intent createAccountIntent = new Intent(EditProfilePage.this, EditProfilePage.class);
                    startActivity(createAccountIntent);
                }
                else
                if (v.getId() == R.id.leaveBtn)
                {
                    Intent loginIntent = new Intent(EditProfilePage.this, ProfilePage.class);
                    startActivity(loginIntent);
                }
                else
                {
                    throw new IllegalArgumentException("Buton necunoscut");
                }
            }
        };

        saveBtn.setOnClickListener(buttonClickListener);
        leaveBtn.setOnClickListener(buttonClickListener);
    }
}
