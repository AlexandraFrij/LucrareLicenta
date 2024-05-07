package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.SQLException;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.regex.Pattern;
import android.widget.Toast;

public class CreateStudentAccount extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_student_account);

        EditText editTextLastName = findViewById(R.id.lastname);
        EditText editTextFirstName = findViewById(R.id.firstname);
        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextIdNumber = findViewById(R.id.studIDNum);
        EditText editTextYear = findViewById(R.id.year);
        EditText editTextGroup = findViewById(R.id.group);
        EditText editTextPassword = findViewById(R.id.password);
        EditText editTextConfirmPassword = findViewById(R.id.confirmPassword);

        Button createAcc = findViewById(R.id.createStudAccBtn);
        createAcc.setOnClickListener(v ->
        {
            String lastName = editTextLastName.getText().toString().trim();
            String firstName = editTextFirstName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String idNumber = editTextIdNumber.getText().toString().trim();
            String year = editTextYear.getText().toString().trim();
            String group = editTextGroup.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String passwordConfirmation = editTextConfirmPassword.getText().toString().trim();

            String message;
            try {
                message = verifyData(lastName, firstName, email, idNumber, year, group, password, passwordConfirmation);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (!message.equals("ok")) {
                Toast.makeText(CreateStudentAccount.this, message, Toast.LENGTH_SHORT).show();
            } else {
                insertData(lastName, firstName, email, idNumber, year, group, password);
                Intent login = new Intent(CreateStudentAccount.this, LoginPage.class);
                startActivity(login);
            }
        });

    }

    public String verifyData(String lastName, String firstName, String email, String idNumber, String year, String group, String pass1, String pass2) throws SQLException {
        String s = verifyName(lastName, firstName);
        if (!s.equals("ok"))
            return s;
        s = verifyEmail(email);
        if (!s.equals("ok"))
            return s;
        s = verifyIdNumber(idNumber);
        if (!s.equals("ok"))
            return s;
        s = verifyYearAndGroup(year, group);
        if (!s.equals("ok"))
            return s;
        s = verifyPassword(pass1, pass2);
        if (!s.equals("ok"))
            return s;
        return "ok";
    }

    public String verifyPassword(String password, String passwordConf) {
        if (!password.equals(passwordConf))
            return "Parola reintrodusa este gresita!";
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%^&*()_+=-]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(password).matches())
            return "Parola invalida!";
        return "ok";
    }

    public String verifyName(String lastName, String firstName) {
        String regex = "^[A-Z][a-z]*(?:-[A-Z][a-z]*)*$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(lastName).matches())
            return "Nume invalid!";
        if (!pattern.matcher(firstName).matches())
            return "Prenume invalid!";
        return "ok";
    }

    public String verifyEmail(String email) throws SQLException {
        if (emailExists(email))
            return "E-mail aflat in folosinta!";
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email).matches())
            return "E-mail invalid!";
        return "ok";
    }

    public String verifyIdNumber(String idNumber) throws SQLException {
        if (idNumberExists(idNumber))
            return "Numar matricol aflat in folosinta!";
        String regex = "^310910401(RSL|ESL)\\d{6}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(idNumber).matches())
            return "Numar matricol invalid!";
        return "ok";
    }

    public String verifyYearAndGroup(String year, String group) {
        String regex1 = "^(L[1-3]|M[1-2])$";
        String regex2 = "^(A[1-9]|B[1-9]|E[1-9])$";
        Pattern pattern1 = Pattern.compile(regex1);
        if (!pattern1.matcher(year).matches())
            return "An de studii invalid!";
        Pattern pattern2 = Pattern.compile(regex2);
        if (!pattern2.matcher(group).matches())
            return "Grupa invalida!";
        return "ok";
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM STUDENT_DATA WHERE email = ?", new String[]{email});
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
            return false;
        } finally {
            db.close();
        }
    }

    public boolean idNumberExists(String idNumber) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM STUDENT_DATA WHERE id_number = ?", new String[]{idNumber});
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
            return false;
        } finally {
            db.close();
        }
    }

    public void insertData(String lastName, String firstName, String email, String idNumber, String year, String group, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL(
                    "INSERT INTO STUDENT_DATA (last_name, first_name, email, id_number, year, class, password) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{lastName, firstName, email, idNumber, year, group, password}
            );
        } finally {
            db.close();
        }
    }

}