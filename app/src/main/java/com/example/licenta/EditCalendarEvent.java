package com.example.licenta;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class EditCalendarEvent extends AppCompatActivity
{
    EditText eventNameView;
    TextView eventDateView;
    TextView startHourView, endHourView;
    Button modifyEventBtn, giveUpBtn;
    int startHour, startMinute, endHour, endMinute;
    String selectedDate, currentDate, currentName, currentStartHour, currentEndHour;

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_calendar_event);
        dbHelper = new DatabaseHelper(this);
        eventNameView = findViewById(R.id.eventName);
        eventDateView = findViewById(R.id.date);
        startHourView = findViewById(R.id.startHour);
        endHourView = findViewById(R.id.endHour);
        modifyEventBtn = findViewById(R.id.modifyEventBtn);
        giveUpBtn = findViewById(R.id.giveUpBtn);

        currentName = getIntent().getStringExtra("eventName");
        currentDate = getIntent().getStringExtra("date");
        currentStartHour = getIntent().getStringExtra("startHour");
        currentEndHour = getIntent().getStringExtra("endHour");

        eventDateView.setText(currentDate);
        eventNameView.setText(currentName);
        startHourView.setText(currentStartHour);
        endHourView.setText(currentEndHour);

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

                if (event.isEmpty()) {
                    String message = "Introduceti denumire eveniment";
                    Toast.makeText(EditCalendarEvent.this, message, Toast.LENGTH_SHORT).show();
                } else if (start.contains("Ora de inceput") || end.contains("Ora de incheiere")) {
                    String message = "Alegeti o ora";
                    Toast.makeText(EditCalendarEvent.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = verifyTimeInterval(startHour, startMinute, endHour, endMinute);
                    if (!message.equals("ok")) {
                        Toast.makeText(EditCalendarEvent.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
                        String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
                        dbHelper.modifyEvent(currentName, currentDate, currentStartHour, currentEndHour, event, selectedDate, startTime, endTime);
                        eventNameView.setText("");
                        eventNameView.setHint("Descriere eveniment");
                        startHourView.setText("Ora de inceput");
                        endHourView.setText("Ora de incheiere");
                    }
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

    private String verifyTimeInterval(int startHour, int startMinute, int endHour, int endMinute) {
        String message = "ok";
        if (startHour < 7)
            message = "Ora de inceput invalida!";
        if (endHour > 21)
            message = "Ora de incheiere invalida!";
        if (endHour < startHour)
            message = "Interval invalid!";
        if (endHour == startHour && endMinute <= startMinute)
            message = "Interval invalid!";

        if (message.equals("ok")) {
            String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
            String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
            if (!dbHelper.timeIsAvailable(selectedDate, startTime, endTime))
                message = "Intervalul de timp este deja rezervat!";
        }

        return message;
    }

}
