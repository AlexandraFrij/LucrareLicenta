package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class EditProfilePage extends AppCompatActivity
{
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new DatabaseHelper(this);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_page);

        Button saveBtn = findViewById(R.id.saveBtn);
        Button leaveBtn = findViewById(R.id.leaveBtn);
        EditText editLastName = findViewById(R.id.lastname);
        EditText editFirstName = findViewById(R.id.firstname);
        EditText editEmail = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);

        String oldEmail = sp.getString("email", null);
        String[] info = dbHelper.retrieveDataWithEmail(oldEmail);
        editLastName.setHint(info[0]);
        editFirstName.setHint(info[1]);
        editEmail.setHint(oldEmail);
        password.setText(info[2]);

        ImageView seePassword = findViewById(R.id.seePassword);
        seePassword.setImageResource(R.drawable.see_password);
        seePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    seePassword.setImageResource(R.drawable.hide_password);
                }
                else
                {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    seePassword.setImageResource(R.drawable.see_password);
                }
            }
        });


        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.saveBtn)
                {
                    String lastName = editLastName.getText().toString().trim();
                    String firstName = editFirstName.getText().toString().trim();
                    String newEmail = editEmail.getText().toString().trim();
                    updateData(lastName, firstName, info[2], newEmail, oldEmail, sp);
                    String message = "Date salvate!";
                    Toast.makeText(EditProfilePage.this, message, Toast.LENGTH_SHORT).show();
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

    private void updateData(String lastname, String firstname, String password, String newEmail, String oldEmail, SharedPreferences sp)
    {
        if (! lastname.isEmpty())
            dbHelper.updateLastname(lastname, oldEmail);
        if (! firstname.isEmpty())
            dbHelper.updateFirstname(firstname, oldEmail);
        if (! newEmail.isEmpty())
            if (dbHelper.emailExists(newEmail))
            {
                String message = "Adresa de e-mail aflata in folosinta! ";
                Toast.makeText(EditProfilePage.this, message, Toast.LENGTH_SHORT).show();
            }
            else
            {
                dbHelper.updateEmail(newEmail, oldEmail);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("email", newEmail);
                editor.apply();
            }
    }

}
