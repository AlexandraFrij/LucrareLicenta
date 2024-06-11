package com.example.licenta;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;


public class EditCalendarEvent extends AppCompatActivity {
    private static final String TAG = "EditCalendarEvent";
    EditText eventNameView;
    TextView eventDateView;
    TextView startHourView, endHourView;
    EditText eventRoomView;
    Button modifyEventBtn, giveUpBtn;
    int startHour, startMinute, endHour, endMinute;
    String selectedDate, currentDate, currentName, currentStartHour, currentEndHour, email, currentRoom;

    private FirebaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_calendar_event);
        dbHelper = new FirebaseHelper();

        eventNameView = findViewById(R.id.eventName);
        eventDateView = findViewById(R.id.date);
        startHourView = findViewById(R.id.startHour);
        endHourView = findViewById(R.id.endHour);
        modifyEventBtn = findViewById(R.id.modifyEventBtn);
        giveUpBtn = findViewById(R.id.giveUpBtn);
        eventRoomView = findViewById(R.id.eventRoom);

        currentName = getIntent().getStringExtra("eventName");
        currentDate = getIntent().getStringExtra("date");
        currentStartHour = getIntent().getStringExtra("startHour");
        currentEndHour = getIntent().getStringExtra("endHour");
        currentRoom = getIntent().getStringExtra("room");
        email = getIntent().getStringExtra("email");

        eventDateView.setText(currentDate);
        eventNameView.setText(currentName);
        startHourView.setText(currentStartHour);
        endHourView.setText(currentEndHour);
        eventRoomView.setText(currentRoom);

        selectedDate = currentDate;
        eventDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditCalendarEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth);
                                eventDateView.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        startHourView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogue(startHourView, true);
            }
        });

        endHourView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogue(endHourView, false);
            }
        });

        modifyEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = eventNameView.getText().toString().trim();
                String start = startHourView.getText().toString().trim();
                String end = endHourView.getText().toString().trim();
                String room  = eventRoomView.getText().toString().trim();

                if (event.isEmpty()) {
                    String message = "Introduceti denumire eveniment";
                    Toast.makeText(EditCalendarEvent.this, message, Toast.LENGTH_SHORT).show();
                } else if (start.contains("Ora de inceput") || end.contains("Ora de incheiere")) {
                    String message = "Alegeti o ora";
                    Toast.makeText(EditCalendarEvent.this, message, Toast.LENGTH_SHORT).show();
                }
                else if (room.isEmpty()) {
                    String message = "Alegeti o sala";
                    Toast.makeText(EditCalendarEvent.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "Incerc Stergerea eveniment" );
                    dbHelper.deleteEvent(currentName, currentDate, currentStartHour, currentEndHour, currentRoom)
                                    .addOnSuccessListener(aVoid ->{
                                        modifyEvent(event, room);
                                    });
                }
            }
        });

        giveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.addEvent(currentName, currentDate, currentStartHour, currentEndHour,email, currentRoom);
                Intent intent = new Intent(EditCalendarEvent.this, ProfHomePage.class);
                startActivity(intent);
            }
        });
    }

    private void timePickerDialogue(TextView textView, boolean isStartTime) {
        final int[] hour = {isStartTime ? startHour : endHour};
        final int[] minute = {isStartTime ? startMinute : endMinute};

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour[0] = selectedHour;
                minute[0] = selectedMinute;
                textView.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));

                if (isStartTime) {
                    startHour = selectedHour;
                    startMinute = selectedMinute;
                } else {
                    endHour = selectedHour;
                    endMinute = selectedMinute;
                }
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour[0], minute[0], true);
        timePickerDialog.show();
    }

    private void verifyTimeInterval(int startHour, int startMinute, int endHour, int endMinute, String room, AddCalendarEvent.OnTimeAvailabilityChecked callback) {
        Log.d(TAG, "Incepe verificarea intervalului de timp");

        if (startHour < 7) {
            callback.onChecked("Ora de inceput invalida! " + startHour);
            return;
        } else if (endHour > 21) {
            callback.onChecked("Ora de incheiere invalida! " + endHour);
            return;
        } else if (endHour < startHour) {
            callback.onChecked("Interval invalid! " + startHour + ":" + endHour);
            return;
        } else if (endHour == startHour && endMinute <= startMinute) {
            callback.onChecked("Interval invalid! " + startHour + ":" + startMinute + "-" + endHour + ":" + endMinute);
            return;
        }

        String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
        String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
        Log.d(TAG, "Verificarea disponibilitatii timpului in Firebase: " + startTime + " - " + endTime);

        dbHelper.timeIsAvailable(selectedDate, room, startTime, endTime).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isAvailable = task.getResult();
                if (!isAvailable) {
                    callback.onChecked("Intervalul de timp este deja rezervat!");
                } else {
                    callback.onChecked("ok");
                }
            } else {
                callback.onChecked("Eroare la verificarea disponibilitatii!");
                Log.e(TAG, "Firebase task failed: ", task.getException());
            }
        });
    }
    private void modifyEvent(String event, String room)
    {
        verifyTimeInterval(startHour, startMinute, endHour, endMinute, room, new AddCalendarEvent.OnTimeAvailabilityChecked() {
            @Override
            public void onChecked(String message) {
                Log.d(TAG, "Verificarea timpului completata: " + message);
                if (!message.equals("ok")) {
                    showToast(message);
                } else {
                    String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
                    String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
                    dbHelper.addEvent(event, selectedDate, startTime, endTime, email, room);
                    String content = event + " " + selectedDate;
                    dbHelper.insertNotification(content, email);
                    eventNameView.setText("");
                    eventNameView.setHint("Descriere eveniment");
                    startHourView.setText("Ora de inceput");
                    endHourView.setText("Ora de incheiere");
                    eventRoomView.setText("Sala eveniment");
                    showToast("Eveniment adaugat cu succes");
                }
            }
        });
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(EditCalendarEvent.this, message, Toast.LENGTH_SHORT).show());
    }


}
