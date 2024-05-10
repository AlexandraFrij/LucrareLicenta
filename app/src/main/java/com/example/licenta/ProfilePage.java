package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilePage extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        Button logoffBtn = findViewById(R.id.logoffBtn);
        Button editProfileBtn = findViewById(R.id.editProfileBtn);
        Button goBackBtn = findViewById(R.id.goBackBtn);
        Button deleteAccBtn = findViewById(R.id.deleteAccBtn);
        TextView name = findViewById(R.id.profileName);
        TextView emailAddress = findViewById(R.id.profileEmail);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String email = sp.getString("email", null);
        String username = retrieveName(email);
        name.setText(username);
        emailAddress.setText(email);

        View.OnClickListener buttonClickListener = v -> {
            if (v.getId() == R.id.logoffBtn)
            {
                Intent signIn = new Intent(ProfilePage.this, SignInPage.class);
                startActivity(signIn);
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
            if (v.getId() == R.id.deleteAccBtn)
            {
                deleteAccount(email);
                Toast.makeText(ProfilePage.this, "Cont sters!", Toast.LENGTH_LONG).show();
                Intent signIn = new Intent(ProfilePage.this, SignInPage.class);
                startActivity(signIn);
            }
            else
            {
                throw new IllegalArgumentException("Unknown button");
            }
        };

        logoffBtn.setOnClickListener(buttonClickListener);
        editProfileBtn.setOnClickListener(buttonClickListener);
        goBackBtn.setOnClickListener(buttonClickListener);
        deleteAccBtn.setOnClickListener(buttonClickListener);
    }

    public String retrieveName(String email)
    {
        String username = "";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT first_name, last_name FROM STUDENT_DATA WHERE email = ?"+
                       "UNION " +
                       "SELECT first_name, last_name FROM PROF_DATA WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, email});

        if (cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            username += firstName;
            username += " ";
            username += lastName;
        }

        cursor.close();
        return username;
    }
    private void deleteAccount(String email)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteStudent = "DELETE FROM STUDENT_DATA WHERE email = ?";
        String deleteProf    = "DELETE FROM PROF_DATA WHERE email = ?";
        db.execSQL(deleteStudent, new String[]{email});
        db.execSQL(deleteProf, new String[]{email});
    }

}
