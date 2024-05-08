package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.regex.Pattern;
import android.widget.Toast;

public class CreateProfAccount extends AppCompatActivity
{

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_prof_account);

        EditText editTextLastName = findViewById(R.id.lastname);
        EditText editTextFirstName = findViewById(R.id.firstname);
        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextPassword = findViewById(R.id.password);
        EditText editTextConfirmPassword = findViewById(R.id.confirmPassword);

        Button createAcc = findViewById(R.id.createProfAccBtn);
        createAcc.setOnClickListener(v ->
        {
            String lastName = editTextLastName.getText().toString().trim();
            String firstName = editTextFirstName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String passwordConfirmation = editTextConfirmPassword.getText().toString().trim();

            String message;
            message = verifyData(lastName, firstName, email, password, passwordConfirmation);

            if (!message.equals("ok"))
            {
                Toast.makeText(CreateProfAccount.this, message, Toast.LENGTH_SHORT).show();
            } else
            {
                insertData(lastName, firstName, email, password);
                Intent login = new Intent(CreateProfAccount.this, LoginPage.class);
                startActivity(login);
            }
        });
    }
    public String verifyData(String lastName, String firstName, String email, String pass1, String pass2)
    {
        String s = verifyName(lastName, firstName);
        if (!s.equals("ok"))
            return s;
        s = verifyEmail(email);
        if (!s.equals("ok"))
            return s;
        s = verifyPassword(pass1, pass2);
        if (!s.equals("ok"))
            return s;
        return "ok";
    }

    public String verifyPassword(String password, String passwordConf)
    {
        if (!password.equals(passwordConf))
            return "Parola reintrodusa este gresita!";
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%^&*()_+=-]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(password).matches())
            return "Parola invalida!";
        return "ok";
    }

    public String verifyName(String lastName, String firstName)
    {
        String regex = "^[A-Z][a-z]*(?:-[A-Z][a-z]*)*$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(lastName).matches())
            return "Nume invalid!";
        if (!pattern.matcher(firstName).matches())
            return "Prenume invalid!";
        return "ok";
    }

    public String verifyEmail(String email)
    {
        if (emailExists(email))
            return "E-mail aflat in folosinta!";
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email).matches())
            return "E-mail invalid!";
        return "ok";
    }

    public boolean emailExists(String email)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try
        {
            String query = "SELECT last_name FROM STUDENT_DATA WHERE email = ? " +
                    "UNION " +
                    "SELECT last_name FROM PROF_DATA WHERE email = ?";

            Cursor cursor = db.rawQuery(query, new String[]{email, email});

            if (cursor.moveToFirst())
            {
                return true;
            }

            return false;
        }
        finally
        {
            db.close();
        }
    }


    public void insertData(String lastName, String firstName, String email, String password)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try
        {
            db.execSQL(
                    "INSERT INTO PROF_DATA (last_name, first_name, email, password) VALUES (?, ?, ?, ?)",
                    new Object[]{lastName, firstName, email, password}
            );
        }
        finally
        {
            db.close();
        }
    }

}