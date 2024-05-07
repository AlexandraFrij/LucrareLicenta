package com.example.licenta;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "licenta.db";
    private static final int DATABASE_VERSION = 1;

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENT_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS STUDENT_DATA");
        onCreate(db);
    }
}
