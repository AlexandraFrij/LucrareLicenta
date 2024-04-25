package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Button forgotPassBtn = findViewById(R.id.forgotpassword);
        Button loginBtn = findViewById(R.id.loginBtn);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.loginBtn)
                {
                    Intent createAccountIntent = new Intent(LoginPage.this, HomePage.class);
                    startActivity(createAccountIntent);
                }
                else
                if (v.getId() == R.id.forgotpassword)
                {
                    Intent loginIntent = new Intent(LoginPage.this, ForgotPasswordPage.class);
                    startActivity(loginIntent);
                }
                else
                {
                    throw new IllegalArgumentException("Buton necunoscut");
                }
            }
        };

        forgotPassBtn.setOnClickListener(buttonClickListener);
        loginBtn.setOnClickListener(buttonClickListener);
    }
}
