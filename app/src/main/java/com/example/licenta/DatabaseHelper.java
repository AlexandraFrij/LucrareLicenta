package com.example.licenta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.licenta.item.AttendanceRecyclerViewItem;
import com.example.licenta.item.CalendarEventsRecyclerViewerItem;
import com.example.licenta.model.Attendance;
import com.example.licenta.model.CalendarEvent;
import com.example.licenta.model.ChatRoom;
import com.example.licenta.model.Messages;
import com.example.licenta.model.Notification;
import com.example.licenta.model.Users;

import java.sql.Timestamp;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "licenta.db";
    private static final int DATABASE_VERSION = 13;

    private static final String CREATE_TABLE_STUDENT_DATA = "CREATE TABLE IF NOT EXISTS STUDENT_DATA (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "last_name TEXT, " +
            "first_name TEXT, " +
            "email TEXT UNIQUE, " +
            "id_number TEXT UNIQUE, " +
            "FOREIGN KEY (email) REFERENCES USERS(email) ON DELETE CASCADE" +
            ")";
    private static final String CREATE_TABLE_PROF_DATA = "CREATE TABLE IF NOT EXISTS PROF_DATA (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "last_name TEXT, " +
            "first_name TEXT, " +
            "email TEXT UNIQUE, " +
            "identification_number TEXT UNIQUE, " +
            "FOREIGN KEY (email) REFERENCES USERS(email) ON DELETE CASCADE" +
            ")";

    private static final String CREATE_TABLE_CHAT_ROOMS = "CREATE TABLE IF NOT EXISTS CHAT_ROOMS (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user1_email TEXT NOT NULL, " +
            "user2_email TEXT NOT NULL, " +
            "created_at TIMESTAMP, " +
            "FOREIGN KEY (user1_email) REFERENCES USERS(email) ON DELETE CASCADE, " +
            "FOREIGN KEY (user2_email) REFERENCES USERS(email)ON DELETE CASCADE " +
            ")";
    private static final String CREATE_TABLE_CHAT_MESSAGES = "CREATE TABLE IF NOT EXISTS CHAT_MESSAGES (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "chat_id INTEGER, " +
            "sender_email TEXT NOT NULL, " +
            "message_content TEXT NOT NULL, " +
            "sent_at TIMESTAMP, " +
            "FOREIGN KEY (chat_id) REFERENCES CHAT_ROOMS(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (sender_email) REFERENCES USERS(email) ON DELETE CASCADE " +
            ")";

    private static final String CREATE_TABLE_CALENDAR= "CREATE TABLE IF NOT EXISTS CALENDAR (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "date TEXT NOT NULL, " +
            "start_time TEXT NOT NULL, " +
            "end_time TEXT NOT NULL, " +
            "created_by TEXT NOT NULL, " +
            "FOREIGN KEY (created_by) REFERENCES USERS(email) ON DELETE CASCADE" +
            ")";
    private static final String CREATE_TABLE_USERS= "CREATE TABLE IF NOT EXISTS USERS (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "email TEXT UNIQUE, " +
            "password TEXT NOT NULL, " +
            "status TEXT NOT NULL " +
            ")";
    private static final String CREATE_TABLE_ATTENDANCES= "CREATE TABLE IF NOT EXISTS ATTENDANCES (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "last_name TEXT NOT NULL, " +
            "first_name TEXT NOT NULL, " +
            "id_number TEXT NOT NULL, " +
            "class_type TEXT NOT NULL, " +
            "date TEXT NOT NULL, " +
            "FOREIGN KEY (id_number) REFERENCES STUDENT_DATA(id_number) ON DELETE CASCADE" +
            ")";
    private static final String CREATE_TABLE_NOTIFICATIONS= "CREATE TABLE IF NOT EXISTS NOTIFICATIONS (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "content TEXT NOT NULL, " +
            "created_by TEXT NOT NULL, " +
            "created_at TIMESTAMP, " +
            "FOREIGN KEY (created_by) REFERENCES USERS(email) ON DELETE CASCADE" +
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
        db.execSQL(CREATE_TABLE_CALENDAR);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_ATTENDANCES);
        db.execSQL(CREATE_TABLE_NOTIFICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS STUDENT_DATA");
        db.execSQL("DROP TABLE IF EXISTS PROF_DATA");
        db.execSQL("DROP TABLE IF EXISTS CHAT_ROOMS");
        db.execSQL("DROP TABLE IF EXISTS CHAT_MESSAGES");
        db.execSQL("DROP TABLE IF EXISTS CALENDAR");
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS ATTENDANCES");
        db.execSQL("DROP TABLE IF EXISTS NOTIFICATIONS");
        onCreate(db);
    }

    public void updateLastname(String lastname, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateStudentsData = "UPDATE STUDENT_DATA SET last_name = ? WHERE email = ?";
        db.execSQL(updateStudentsData, new String[]{lastname, email});
        String updateProfsData = "UPDATE PROF_DATA SET last_name = ? WHERE email = ?";
        db.execSQL(updateProfsData, new String[]{lastname, email});
        String updateAttendancesData = "UPDATE ATTENDANCES SET last_name = ? WHERE email = ?";
        db.execSQL(updateAttendancesData, new String[]{lastname, email});
    }

    public void updateFirstname(String firstname, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateStudentsData = "UPDATE STUDENT_DATA SET first_name = ? WHERE email = ?";
        db.execSQL(updateStudentsData, new String[]{firstname, email});
        String updateProfsData = "UPDATE PROF_DATA SET first_name = ? WHERE email = ?";
        db.execSQL(updateProfsData, new String[]{firstname, email});
        String updateAttendancesData = "UPDATE ATTENDANCES SET last_name = ? WHERE email = ?";
        db.execSQL(updateAttendancesData, new String[]{firstname, email});
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
        String updateUsers = "UPDATE USERS SET email = ? WHERE email = ?";
        db.execSQL(updateUsers, new String[]{newEmail, oldEmail});
        String updateCalendar = "UPDATE CALENDAR SET created_by = ? WHERE created_by = ?";
        db.execSQL(updateCalendar, new String[]{newEmail, oldEmail});
        String updateNotifications = "UPDATE NOTIFICATIONS SET created_by = ? WHERE created_by = ?";
        db.execSQL(updateNotifications, new String[]{newEmail, oldEmail});
    }

    public void updatePassword(String password, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateData = "UPDATE USERS SET password = ? WHERE email = ?";
        db.execSQL(updateData, new String[]{password, email});
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String query = "SELECT id FROM USERS WHERE email = ? " ;

            Cursor cursor = db.rawQuery(query, new String[]{email});

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
    public void insertProfData(String lastName, String firstName, String email, String identificationNumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            db.execSQL(
                    "INSERT INTO PROF_DATA (last_name, first_name, email, identification_number) VALUES (?, ?, ?, ?)",
                    new Object[]{lastName, firstName, email, identificationNumber}
            );
        }
        finally
        {
            db.close();
        }
    }
    public void insertStudentData(String lastName, String firstName, String email, String idNumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            db.execSQL(
                    "INSERT INTO STUDENT_DATA (last_name, first_name, email, id_number) VALUES (?, ?, ?, ?)",
                    new Object[]{lastName, firstName, email, idNumber}
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
        String deleteUser   = "DELETE FROM USERS WHERE email = ?";
        db.execSQL(deleteUser, new String[]{email});
    }
    public String[] retrieveDataWithEmail(String email)
    {
        String[] info = new String[4];
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT first_name, last_name FROM STUDENT_DATA WHERE email = ?"+
                "UNION " +
                "SELECT first_name, last_name FROM PROF_DATA WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, email});

        if (cursor.moveToFirst())
        {
            info[0] = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            info[1] = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
        }
        return info;
    }
    public String extractPasswordUsingEmail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String password = null;

        try {
            String query = "SELECT password FROM USERS WHERE email = ? ";

            Cursor cursor = db.rawQuery(query, new String[]{email});
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
    public ChatRoom extractChatRoom(String email) {
        ChatRoom chatRoom = new ChatRoom();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id, user1_email, user2_email FROM CHAT_ROOMS " +
                "WHERE user1_email = ? " +
                "OR user2_email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, email});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String user1_email = cursor.getString(cursor.getColumnIndexOrThrow("user1_email"));
                String user2_email = cursor.getString(cursor.getColumnIndexOrThrow("user2_email"));

                chatRoom.addChatRoom(user1_email, user2_email, id);
            } while (cursor.moveToNext());
        }
        return chatRoom;
    }
    public String[] getRecentMessage(int chatRoomId)
    {
        String[] recentMessage = new String[2];
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT message_content, strftime('%H:%M', sent_at) as formatted_timestamp " +
                "FROM CHAT_MESSAGES " +
                "WHERE chat_id = ? " +
                "ORDER BY sent_at DESC " +
                "LIMIT 1";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(chatRoomId)});

        if (cursor.moveToFirst()) {
            recentMessage[0] = cursor.getString(cursor.getColumnIndexOrThrow("message_content"));
            recentMessage[1] = cursor.getString(cursor.getColumnIndexOrThrow("formatted_timestamp"));
        }

        cursor.close();
        db.close();
        return recentMessage;
    }
    public boolean timeIsAvailable(String date, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String query = "SELECT COUNT(*) FROM CALENDAR WHERE date = ? AND (start_time < ? AND end_time > ?)";

            Cursor cursor = db.rawQuery(query, new String[]{date, endTime, startTime});

            if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                return false;
            }

            return true;
        } finally {
            db.close();
        }
    }


    public void addEvent(String name, String date, String startTime, String endTime, String createdBy)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.execSQL(
                    "INSERT INTO CALENDAR (name, date, start_time, end_time, created_by) VALUES (?,?, ?, ?, ?)",
                    new Object[]{name, date, startTime, endTime, createdBy}
            );
        }
        finally
        {
            db.close();
        }
    }
    public CalendarEvent extractCalendarEvents(String day) {
        CalendarEvent calendarEvent = new CalendarEvent();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String query = "SELECT name, date, start_time, end_time FROM CALENDAR WHERE date = ?";
            Cursor cursor = db.rawQuery(query, new String[]{day});

            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    String start = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                    String end = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                    String time = start + " - " + end;
                    calendarEvent.addEvent(name, date, time);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } finally {
            db.close();
        }

        return calendarEvent;
    }

    public void deleteEvent(String name, String date, String startHour, String endHour)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteEvent = "DELETE FROM CALENDAR WHERE name = ? AND date = ? AND start_time = ? AND end_time = ? ";
        db.execSQL(deleteEvent, new String[]{name, date, startHour,endHour});

    }
    public void modifyEvent(String currentName, String currentDate, String currentStartTime, String currentEndTime, String newName, String newDate, String newStartTime, String newEndTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.execSQL(
                    "UPDATE CALENDAR SET name = ?, date = ?, start_time = ?, end_time = ? " +
                            "WHERE name = ? AND date = ? AND start_time = ? AND end_time = ?",
                    new Object[]{newName, newDate, newStartTime, newEndTime, currentName, currentDate, currentStartTime, currentEndTime}
            );
        }
        finally
        {
            db.close();
        }
    }

    public void insertUser(String email, String status, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            db.execSQL(
                    "INSERT INTO USERS (email, status, password) VALUES (?, ?, ?)",
                    new Object[]{email, status, password}
            );
        }
        finally
        {
            db.close();
        }
    }
    public void insertStudentAttendance(String last_name, String first_name, String id_number, String class_type,String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            db.execSQL(
                    "INSERT INTO ATTENDANCES (last_name, first_name, id_number, class_type, date) VALUES (?, ?, ?, ?, ?)",
                    new Object[]{last_name, first_name, id_number, class_type, date}
            );
        }
        finally
        {
            db.close();
        }
    }
    public boolean userIsStudent(String userEmail)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String query = "SELECT status FROM USERS WHERE  email = ? " ;

            Cursor cursor = db.rawQuery(query, new String[]{userEmail});

            if (cursor.moveToFirst()) {
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                if (status.equals("student"))
                    return true;
            }
            return false;
        } finally {
            db.close();
        }
    }
    public String retrieveIdNumber(String email)
    {
        String idNumber = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id_number FROM STUDENT_DATA WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst())
        {
            idNumber = cursor.getString(cursor.getColumnIndexOrThrow("id_number"));;
        }
        return idNumber;
    }
    public String retrieveLastName(String email)
    {
        String lastName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT last_name FROM STUDENT_DATA WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst())
        {
            lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));;
        }
        return lastName;
    }
    public String retrieveFirstName(String email)
    {
        String firstName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT first_name FROM STUDENT_DATA WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst())
        {
            firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));;
        }
        return firstName;
    }
    public boolean studentWasPresent(String idNumber, String date, String classType)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String query = "SELECT id FROM ATTENDANCES WHERE  id_number = ? AND date = ? AND class_type = ? " ;

            Cursor cursor = db.rawQuery(query, new String[]{idNumber, date,classType});

            if (cursor.moveToFirst()) {
                    return true;
            }
            return false;
        } finally {
            db.close();
        }
    }
    public Attendance retrieveAttendances(String idNumber)
    {
        Attendance attendance = new Attendance();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT class_type, date FROM ATTENDANCES WHERE id_number = ?";
        Cursor cursor = db.rawQuery(query, new String[]{idNumber});

        if (cursor.moveToFirst())
        {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("class_type"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            attendance.addAttendance(name, date);
        }
        return attendance;
    }
    public boolean userAddedEvent(String userEmail, String name, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] parts = time.split("-", 2);
        String startTime = parts[0].trim();
        String endTime = parts[1].trim();

        Cursor cursor = null;
        try {
            String query = "SELECT id FROM CALENDAR WHERE created_by = ? AND name = ? AND date = ? AND start_time = ? AND end_time = ?";
            cursor = db.rawQuery(query, new String[]{userEmail, name, date, startTime, endTime});

            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
    public void insertNotification(String content, String createdBy)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            db.execSQL(
                    "INSERT INTO NOTIFICATIONS (content, created_by, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)",
                    new Object[]{content, createdBy}
            );
        }
        finally
        {
            db.close();
        }
    }
    public Notification retrieveNotifications(String email)
    {
        Notification notification = new Notification();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT content, created_at FROM NOTIFICATIONS WHERE created_by !=  ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst())
        {
            String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
            notification.addNotification(content, date);
        }
        return notification;
    }

}
