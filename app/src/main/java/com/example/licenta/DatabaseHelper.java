package com.example.licenta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.licenta.model.Messages;
import com.example.licenta.model.Users;

import java.sql.Timestamp;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "licenta.db";
    private static final int DATABASE_VERSION = 8;

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

    private static final String CREATE_TABLE_CHAT_ROOMS = "CREATE TABLE IF NOT EXISTS CHAT_ROOMS (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user1_email TEXT NOT NULL, " +
            "user2_email TEXT NOT NULL, " +
            "created_at TIMESTAMP" +
            ")";
    private static final String CREATE_TABLE_CHAT_MESSAGES = "CREATE TABLE IF NOT EXISTS CHAT_MESSAGES (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "chat_id INTEGER, " +
            "sender_email TEXT NOT NULL, " +
            "message_content TEXT NOT NULL, " +
            "sent_at TIMESTAMP, " +
            "FOREIGN KEY (chat_id) REFERENCES CHAT_ROOMS(id) " +
            ")";
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_STUDENT_DATA);
        db.execSQL(CREATE_TABLE_PROF_DATA);
        db.execSQL(CREATE_TABLE_CHAT_ROOMS);
        db.execSQL(CREATE_TABLE_CHAT_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS STUDENT_DATA");
        db.execSQL("DROP TABLE IF EXISTS PROF_DATA");
        db.execSQL("DROP TABLE IF EXISTS CHAT_ROOMS");
        db.execSQL("DROP TABLE IF EXISTS CHAT_MESSAGES");
        onCreate(db);
    }

    public void updateLastname(String lastname, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateStudentsData = "UPDATE STUDENT_DATA SET last_name = ? WHERE email = ?";
        db.execSQL(updateStudentsData, new String[]{lastname, email});
        String updateProfsData = "UPDATE PROF_DATA SET last_name = ? WHERE email = ?";
        db.execSQL(updateProfsData, new String[]{lastname, email});
    }

    public void updateFirstname(String firstname, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateStudentsData = "UPDATE STUDENT_DATA SET first_name = ? WHERE email = ?";
        db.execSQL(updateStudentsData, new String[]{firstname, email});
        String updateProfsData = "UPDATE PROF_DATA SET first_name = ? WHERE email = ?";
        db.execSQL(updateProfsData, new String[]{firstname, email});
    }

    public void updateEmail(String newEmail, String oldEmail)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateStudentsData = "UPDATE STUDENT_DATA SET email = ? WHERE email = ?";
        db.execSQL(updateStudentsData, new String[]{newEmail, oldEmail});
        String updateProfsData = "UPDATE PROF_DATA SET email = ? WHERE email = ?";
        db.execSQL(updateProfsData, new String[]{newEmail, oldEmail});
        String updateChatRoomsDataUser1 = "UPDATE CHAT_ROOMS SET user1_email = ? WHERE user1_email = ?";
        db.execSQL(updateChatRoomsDataUser1, new String[]{newEmail, oldEmail});
        String updateChatRoomsDataUser2 = "UPDATE CHAT_ROOMS SET user2_email = ? WHERE user2_email = ?";
        db.execSQL(updateChatRoomsDataUser2, new String[]{newEmail, oldEmail});
        String updateChatMessages = "UPDATE CHAT_MESSAGES SET sender_email = ? WHERE sender_email = ?";
        db.execSQL(updateChatMessages, new String[]{newEmail, oldEmail});
    }

    public void updatePassword(String password, String email)
    {
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
    public void insertChatRoom(String user1Email, String user2Email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.execSQL(
                    "INSERT INTO CHAT_ROOMS (user1_email, user2_email, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)",
                    new Object[]{user1Email, user2Email}
            );
        }
        finally
        {
            db.close();
        }
    }
    public boolean chatRoomExists(String user1Email, String user2Email)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String query = "SELECT id FROM CHAT_ROOMS WHERE ( user1_email = ? AND user2_email = ?) " +
                    "OR " +
                    "( user1_email = ? AND user2_email = ? )";

            Cursor cursor = db.rawQuery(query, new String[]{user1Email, user2Email, user2Email, user1Email});

            if (cursor.moveToFirst()) {
                return true;
            }

            return false;
        } finally {
            db.close();
        }
    }
    public void insertChatMessage(int chatId, String senderEmail, String messageContent)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.execSQL(
                    "INSERT INTO CHAT_MESSAGES (chat_id, sender_email, message_content, sent_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)",
                    new Object[]{chatId, senderEmail, messageContent}
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
    public Users extractUsername(String pattern) {
        Users users = new Users();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String query = "SELECT last_name, first_name, email FROM STUDENT_DATA WHERE LOWER(last_name) LIKE LOWER(?) OR LOWER(first_name) LIKE LOWER(?) " +
                    "UNION " +
                    "SELECT last_name, first_name, email FROM PROF_DATA WHERE LOWER(last_name) LIKE LOWER(?) OR LOWER(first_name) LIKE LOWER(?)";

            String searchPattern = "%" + pattern.toLowerCase() + "%";
            Cursor cursor = db.rawQuery(query, new String[]{searchPattern, searchPattern, searchPattern, searchPattern});

            if (cursor.moveToFirst()) {
                do {
                    String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                    String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    users.addUsername(lastName + " " + firstName);
                    users.addEmail(email);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } finally {
            db.close();
        }

        return users;
    }
    public int retrieveChatRoomId(String user1Email, String user2Email)
    {
        int id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM CHAT_ROOMS WHERE (user1_email = ? AND user2_email = ?)" +
                " OR" +
                " (user1_email = ? AND user2_email = ?) ";
        Cursor cursor = db.rawQuery(query, new String[]{user1Email, user2Email, user2Email, user1Email});

        if (cursor.moveToFirst())
        {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        return id;
    }
    public Messages extractMessages(int chat_id) {
        Messages messages = new Messages();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT message_content, sender_email, sent_at FROM CHAT_MESSAGES " +
                "WHERE chat_id = ? " +
                "ORDER BY sent_at ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(chat_id)});

        if (cursor.moveToFirst()) {
            do {
                String messageContent = cursor.getString(cursor.getColumnIndexOrThrow("message_content"));
                String senderEmail = cursor.getString(cursor.getColumnIndexOrThrow("sender_email"));
                Timestamp sentAt = Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("sent_at")));

                messages.addMessage(messageContent, senderEmail, sentAt);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return messages;
    }

}
