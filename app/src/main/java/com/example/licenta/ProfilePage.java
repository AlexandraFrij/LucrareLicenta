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
        Button goBackBtn = findViewById(R.id.goBackBtn);
        View.OnClickListener buttonClickListener = v -> {
            if (v.getId() == R.id.logoffBtn)
            {
                Intent signin = new Intent(ProfilePage.this, SignInPage.class);
                startActivity(signin);
            }
            else
            if (v.getId() == R.id.editProfileBtn)
            {
                Intent edit = new Intent(ProfilePage.this, EditProfilePage.class);
                startActivity(edit);
            }
            else
            if (v.getId() == R.id.goBackBtn)
            {
                Intent home = new Intent(ProfilePage.this, HomePage.class);
                startActivity(home);
            }
            else
            {
                throw new IllegalArgumentException("Unknown button");
            }
        };

        logoffBtn.setOnClickListener(buttonClickListener);
        editProfileBtn.setOnClickListener(buttonClickListener);
        goBackBtn.setOnClickListener(buttonClickListener);
    }
}
