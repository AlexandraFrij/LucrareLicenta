package com.example.licenta;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CreateAccountPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_page);
        Button createProfAccBtn = findViewById(R.id.createProfAccBtn);
        Button createStudAccBtn = findViewById(R.id.createStudAccBtn);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.createProfAccBtn)
                {
                    Intent createProfAccountIntent = new Intent(CreateAccountPage.this, CreateProfAccount.class);
                    startActivity(createProfAccountIntent);
                }
                else
                if (v.getId() == R.id.createStudAccBtn)
                {
                    Intent createStudAccountIntent = new Intent(CreateAccountPage.this, CreateStudentAccount.class);
                    startActivity(createStudAccountIntent);
                }
                else
                {
                    throw new IllegalArgumentException("Unknown button");
                }
            }
        };

        createProfAccBtn.setOnClickListener(buttonClickListener);
        createStudAccBtn.setOnClickListener(buttonClickListener);
    }
}
