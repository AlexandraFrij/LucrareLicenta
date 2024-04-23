package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        Button logoffBtn = findViewById(R.id.logoffBtn);
        Button editProfileBtn = findViewById(R.id.editProfileBtn);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.logoffBtn)
                {
                    Intent createAccountIntent = new Intent(ProfilePage.this, SignInPage.class);
                    startActivity(createAccountIntent);
                }
                else
                if (v.getId() == R.id.editProfileBtn)
                {
                    Intent loginIntent = new Intent(ProfilePage.this, EditProfilePage.class);
                    startActivity(loginIntent);
                }
                else
                {
                    throw new IllegalArgumentException("Buton necunoscut");
                }
            }
        };

        logoffBtn.setOnClickListener(buttonClickListener);
        editProfileBtn.setOnClickListener(buttonClickListener);
    }
}
