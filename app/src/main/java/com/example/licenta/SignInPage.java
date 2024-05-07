package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SignInPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page);
        Button createAccBtn = findViewById(R.id.createAccountBtn);
        Button loginBtn = findViewById(R.id.loginBtn);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.createAccountBtn)
                {
                    Intent createAccountIntent = new Intent(SignInPage.this, CreateAccountPage.class);
                    startActivity(createAccountIntent);
                }
                else
                    if (v.getId() == R.id.loginBtn)
                {
                    Intent loginIntent = new Intent(SignInPage.this, LoginPage.class);
                    startActivity(loginIntent);
                }
                    else
                    {
                    throw new IllegalArgumentException("Unknown button");
                }
            }
        };

        createAccBtn.setOnClickListener(buttonClickListener);
        loginBtn.setOnClickListener(buttonClickListener);
    }
}
