package com.example.licenta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "licenta.db";
    private static final int DATABASE_VERSION = 4;

    private static final String CREATE_TABLE_STUDENT_DATA = "CREATE TABLE IF NOT EXISTS STUDENT_DATA (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "last_name TEXT, " +
            "first_name TEXT, " +
            "email TEXT UNIQUE, " +
            "id_number TEXT UNIQUE, " +
            "year TEXT, " +
            "class TEXT, " +
            "password TEXT" +
            ")";
    private static final String CREATE_TABLE_PROF_DATA = "CREATE TABLE IF NOT EXISTS PROF_DATA (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "last_name TEXT, " +
            "first_name TEXT, " +
            "email TEXT UNIQUE, " +
            "password TEXT" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENT_DATA);
        db.execSQL(CREATE_TABLE_PROF_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS STUDENT_DATA");
        db.execSQL("DROP TABLE IF EXISTS PROF_DATA");
        onCreate(db);
    }

    public void updateLastname(String lastname, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateStudentsData = "UPDATE STUDENT_DATA SET last_name = ? WHERE email = ?";
        db.execSQL(updateStudentsData, new String[]{lastname, email});
        String updateProfsData = "UPDATE PROF_DATA SET last_name = ? WHERE email = ?";
        db.execSQL(updateProfsData, new String[]{lastname, email});
    }

    public void updateFirstname(String firstname, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateStudentsData = "UPDATE STUDENT_DATA SET first_name = ? WHERE email = ?";
        db.execSQL(updateStudentsData, new String[]{firstname, email});
        String updateProfsData = "UPDATE PROF_DATA SET first_name = ? WHERE email = ?";
        db.execSQL(updateProfsData, new String[]{firstname, email});
    }

    public void updateEmail(String newEmail, String oldEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateStudentsData = "UPDATE STUDENT_DATA SET email = ? WHERE email = ?";
        db.execSQL(updateStudentsData, new String[]{newEmail, oldEmail});
        String updateProfsData = "UPDATE PROF_DATA SET email = ? WHERE email = ?";
        db.execSQL(updateProfsData, new String[]{newEmail, oldEmail});
    }

    public void updatePassword(String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateStudentsData = "UPDATE STUDENT_DATA SET password = ? WHERE email = ?";
        db.execSQL(updateStudentsData, new String[]{password, email});
        String updateProfsData = "UPDATE PROF_DATA SET password = ? WHERE email = ?";
        db.execSQL(updateProfsData, new String[]{password, email});
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String query = "SELECT last_name FROM STUDENT_DATA WHERE email = ? " +
                    "UNION " +
                    "SELECT last_name FROM PROF_DATA WHERE email = ?";

            Cursor cursor = db.rawQuery(query, new String[]{email, email});

            if (cursor.moveToFirst()) {
                return true;
            }

            return false;
        } finally {
            db.close();
        }
    }
    public boolean idNumberExists(String idNumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM STUDENT_DATA WHERE id_number = ?", new String[]{idNumber});
            if (cursor.moveToFirst())
            {
                int count = cursor.getInt(0);
                return count > 0;
            }
            return false;
        }
        finally
        {
            db.close();
        }
    }
    public void insertProfData(String lastName, String firstName, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

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
    public void insertStudentData(String lastName, String firstName, String email, String idNumber, String year, String group, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            db.execSQL(
                    "INSERT INTO STUDENT_DATA (last_name, first_name, email, id_number, year, class, password) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{lastName, firstName, email, idNumber, year, group, password}
            );
        }
        finally
        {
            db.close();
        }
    }
    public void deleteAccount(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteStudent = "DELETE FROM STUDENT_DATA WHERE email = ?";
        String deleteProf    = "DELETE FROM PROF_DATA WHERE email = ?";
        db.execSQL(deleteStudent, new String[]{email});
        db.execSQL(deleteProf, new String[]{email});
    }
    public String[] retrieveDataWithEmail(String email)
    {
        String[] info = new String[4];
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT first_name, last_name, password FROM STUDENT_DATA WHERE email = ?"+
                "UNION " +
                "SELECT first_name, last_name, password FROM PROF_DATA WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, email});

        if (cursor.moveToFirst())
        {
            info[0] = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            info[1] = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            info[2] = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        }
        return info;
    }
    public String extractPasswordUsingEmail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
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
}
