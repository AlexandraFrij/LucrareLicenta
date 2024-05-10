package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginPage extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new DatabaseHelper(this);
        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextPassword = findViewById(R.id.password);

        Button forgotPassBtn = findViewById(R.id.forgotpassword);
        Button loginBtn = findViewById(R.id.loginBtn);
        View.OnClickListener buttonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v.getId() == R.id.loginBtn)
                {
                    String email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();

                    String pass = extractPasswordUsingEmail(email);
                    String message = rightPasswordWasIntroduced(pass, password);
                    if (! message.equals("ok"))
                        Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT).show();
                    else
                    {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("email", email);
                        editor.commit();

                        Intent home = new Intent(LoginPage.this, HomePage.class);
                        startActivity(home);
                    }

                }
                else
                if (v.getId() == R.id.forgotpassword)
                {
                    Intent forgotPassword = new Intent(LoginPage.this, ForgotPasswordPage.class);
                    startActivity(forgotPassword);
                }
                else
                {
                    throw new IllegalArgumentException("Unknown button");
                }
            }
        };

        forgotPassBtn.setOnClickListener(buttonClickListener);
        loginBtn.setOnClickListener(buttonClickListener);
    }
    public String extractPasswordUsingEmail(String email)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String password = null;

        try {
            String query = "SELECT password FROM STUDENT_DATA WHERE email = ? " +
                    "UNION " +
                    "SELECT password FROM PROF_DATA WHERE email = ?";

            Cursor cursor = db.rawQuery(query, new String[]{email, email});
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("password");
                if (columnIndex >= 0) {
                    password = cursor.getString(columnIndex);
                }
            }

            cursor.close();
        }
        finally
        {
            db.close();
        }
        return password;
    }
    public String rightPasswordWasIntroduced(String pass1, String pass2)
    {
        if(pass1 == null)
            return "Adresa de e-mail nu exista!";
        if(! pass1.equals(pass2))
            return "Parola incorecta!";
        return "ok";
    }

}
