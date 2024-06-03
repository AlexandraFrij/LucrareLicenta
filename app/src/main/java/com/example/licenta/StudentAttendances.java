package com.example.licenta;


import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.adapter.AttendanceAdapter;
import com.example.licenta.item.AttendanceRecyclerViewItem;
import com.example.licenta.model.Attendance;

import java.util.ArrayList;
import java.util.List;


public class StudentAttendances extends AppCompatActivity {

    Button goBackButton;
    RecyclerView attendanceRecyclerView;
    String idNumber;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_attendances);
        dbHelper = new DatabaseHelper(this);
        goBackButton = findViewById(R.id.goBackBtn);

        idNumber = getIntent().getStringExtra("idNumber");

        goBackButton.setOnClickListener(v ->{
            Intent intent = new Intent(StudentAttendances.this, StudentHomePage.class);
            startActivity(intent);
        });
        showAttendances();
    }
    private void showAttendances()
    {
        attendanceRecyclerView = findViewById(R.id.attendances_viewer);
        Attendance items = dbHelper.retrieveAttendances(idNumber);
        List<String> name = items.getName();
        List<String> date = items.getDate();
        List<AttendanceRecyclerViewItem> attendances = new ArrayList<>();
        for(int i = 0; i< name.size(); i++)
        {
            attendances.add(new AttendanceRecyclerViewItem(name.get(i), date.get(i)));
        }
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(StudentAttendances.this));
        attendanceRecyclerView.setAdapter(new AttendanceAdapter(getApplicationContext(), attendances));

    }
}

