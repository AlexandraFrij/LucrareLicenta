package com.example.licenta;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class AddCalendarEvent extends AppCompatActivity {
    private static final String TAG = "AddCalendarEvent";

    EditText eventNameView;
    TextView eventDateView, startHourView, endHourView;
    EditText eventRoomView;
    Button addEventBtn, giveUpBtn;
    int startHour, startMinute, endHour, endMinute;
    String selectedDate, email;

    private FirebaseHelper dbHelper;
    private AlertDialogMessages alertDialogMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_calendar_event);
        dbHelper = new FirebaseHelper();
        alertDialogMessages = new AlertDialogMessages();

        eventNameView = findViewById(R.id.eventName);
        eventDateView = findViewById(R.id.date);
        startHourView = findViewById(R.id.startHour);
        endHourView = findViewById(R.id.endHour);
        addEventBtn = findViewById(R.id.addEventBtn);
        giveUpBtn = findViewById(R.id.giveUpBtn);
        eventRoomView = findViewById(R.id.eventRoom);

        selectedDate = getIntent().getStringExtra("date");
        email = getIntent().getStringExtra("userEmail");
        eventDateView.setText(selectedDate);
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

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = eventNameView.getText().toString().trim();
                String start = startHourView.getText().toString().trim();
                String end = endHourView.getText().toString().trim();
                String room = eventRoomView.getText().toString().trim();

                if (event.isEmpty()) {
                    showError("Introduceti denumire eveniment");
                } else if (start.contains("Ora de inceput") || end.contains("Ora de incheiere")) {
                    showError("Alegeti o ora");
                } else if (room.isEmpty()) {
                    showError("Alegeti o sala");
                } else {
                    verifyTimeInterval(startHour, startMinute, endHour, endMinute, room, new OnTimeAvailabilityChecked() {
                        @Override
                        public void onChecked(String message) {
                            Log.d(TAG, "Verificarea timpului completata: " + message);
                            if (!message.equals("ok")) {
                                showError(message);
                            } else {
                                String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
                                String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
                                dbHelper.addEvent(event, selectedDate, startTime, endTime, email, room);
                                String content = "Un nou "+ event + " " + selectedDate +" a fost adaugat in calendar!";
                                dbHelper.insertNotification(content, email, "calendarEvent");
                                eventNameView.setText("");
                                eventNameView.setHint("Descriere eveniment");
                                startHourView.setText("Ora de inceput");
                                endHourView.setText("Ora de incheiere");
                                eventRoomView.setText("Sala eveniment");
                                showSuccess("Eveniment adaugat cu succes");
                            }
                        }
                    });
                }
            }
        });

        giveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCalendarEvent.this, ProfHomePage.class);
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

    private void verifyTimeInterval(int startHour, int startMinute, int endHour, int endMinute, String room, OnTimeAvailabilityChecked callback) {
        Log.d(TAG, "Incepe verificarea intervalului de timp");

        if (startHour < 7) {
            callback.onChecked("Ora de inceput invalida! ");
            return;
        } else if (endHour > 21) {
            callback.onChecked("Ora de incheiere invalida! ");
            return;
        } else if (endHour < startHour) {
            callback.onChecked("Interval invalid! ");
            return;
        } else if (endHour == startHour && endMinute <= startMinute) {
            callback.onChecked("Interval invalid! ");
            return;
        }

        String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
        String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
        Log.d(TAG, "Verificarea disponibilitatii timpului in Firebase: " + startTime + " - " + endTime);

        dbHelper.timeIsAvailable(selectedDate, room, startTime, endTime).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isAvailable = task.getResult();
                if (!isAvailable) {
                    callback.onChecked("Sala este rezervata pentru intervalul ales!");
                } else {
                    callback.onChecked("ok");
                }
            } else {
                callback.onChecked("Eroare la verificarea disponibilitatii!");
                Log.e(TAG, "Firebase task failed: ", task.getException());
            }
        });
    }

    private void showError(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, message));
    }
    private void showSuccess(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showSuccessDialog(this, message));
    }

    interface OnTimeAvailabilityChecked {
        void onChecked(String message);
    }
}
