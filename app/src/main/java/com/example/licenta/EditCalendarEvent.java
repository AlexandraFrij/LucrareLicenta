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
    String[] id = {"0"};

    private FirebaseHelper dbHelper;
    private AlertDialogMessages alertDialogMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_calendar_event);
        dbHelper = new FirebaseHelper();
        alertDialogMessages = new AlertDialogMessages();

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

        String[] start = currentStartHour.split(":");
        startHour = Integer.parseInt(start[0]);
        startMinute = Integer.parseInt(start[1]);

        String[] end = currentEndHour.split(":");
        endHour = Integer.parseInt(end[0]);
        endMinute = Integer.parseInt(end[1]);

        selectedDate = currentDate;

        eventDateView.setText(currentDate);
        eventNameView.setText(currentName);
        startHourView.setText(currentStartHour);
        endHourView.setText(currentEndHour);
        eventRoomView.setText(currentRoom);

        eventDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
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
                String room = eventRoomView.getText().toString().trim();

                if (event.isEmpty()) {
                    String message = "Introduceti denumirea evenimentului";
                    showError(message);
                } else if (start.contains("Ora de inceput") || end.contains("Ora de incheiere")) {
                    String message = "Selectati o ora";
                    showError(message);
                } else if (room.isEmpty()) {
                    String message = "Selectati o sala";
                    showError(message);
                } else {
                    modifyEvent(event, room);
                    modifyEventBtn.setEnabled(false);
                    modifyEventBtn.setAlpha(0.5f);
                }
            }
        });

        giveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditCalendarEvent.this, ProfHomePage.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog() {
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
        Log.d(TAG, "Verifying time interval");

        if (startHour < 7) {
            callback.onChecked("Ora de inceput invalida!");
            return;
        } else if (endHour > 21) {
            callback.onChecked("Ora de incheiere invalida!");
            return;
        } else if (endHour < startHour) {
            callback.onChecked("Interval invalid! Ora de inceput mai mare decat ora de incheiere!");
            return;
        } else if (endHour == startHour && endMinute <= startMinute) {
            callback.onChecked("Interval invalid! Minutele de incheiere trebuie sa fie mai mari decat minutele de inceput!");
            return;
        }

        String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
        String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
        Log.d(TAG, "Checking availability in Firebase: " + startTime + " - " + endTime);

        dbHelper.extractEventId(currentName, currentDate, currentStartHour, currentEndHour, currentRoom, email)
                .addOnSuccessListener(databaseId -> {
                    Log.d(TAG, "Inainte de  if (databaseId != null && !databaseId.isEmpty())"+ databaseId);
                    if (databaseId != null && !databaseId.isEmpty()) {
                        id[0] = databaseId;
                        Log.d(TAG, "In  if (databaseId != null && !databaseId.isEmpty())");
                        dbHelper.timeIsAvailableForModify(selectedDate, room, startTime, endTime, databaseId).addOnCompleteListener(task -> {
                            Log.d(TAG, "Inainte de if (task.isSuccessful())");
                            if (task.isSuccessful()) {
                                boolean isAvailable = task.getResult();
                                if (!isAvailable) {
                                    callback.onChecked("Sala este deja rezervata pentru intervalul ales!");
                                } else {
                                    callback.onChecked("ok");
                                }
                            } else {
                                callback.onChecked("Eroare la verificarea disponibilitatii!");
                                Log.e(TAG, "Task failed: ", task.getException());
                            }
                        });
                    }
                });
    }

    private void modifyEvent(String event, String room) {
        verifyTimeInterval(startHour, startMinute, endHour, endMinute, room, new AddCalendarEvent.OnTimeAvailabilityChecked() {
            @Override
            public void onChecked(String message) {
                Log.d(TAG, "Verificarea timpului completata: " + message);
                if (!message.equals("ok")) {
                    showError(message);
                } else {
                    String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
                    String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
                    dbHelper.modifyEvent(id[0], event, selectedDate, startTime, endTime, room);
                    String content = "Un nou " + event + " pe data de " + selectedDate + " a fost modificat în calendar!";
                    dbHelper.insertNotification(content, email, "calendarEvent");
                    showSuccess("Eveniment modificat cu succes");
                }
            }
        });
    }

    private void showError(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, message));
    }

    private void showSuccess(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showSuccessDialog(this, message));
    }
}
