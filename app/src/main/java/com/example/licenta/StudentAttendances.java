package com.example.licenta;


import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.adapter.AttendanceAdapter;
import com.example.licenta.item.AttendanceRecyclerViewItem;
import com.example.licenta.item.StudentAttendancesRecyclerViewItem;
import com.example.licenta.adapter.StudentAttendancesAdapter;
import com.example.licenta.model.Attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StudentAttendances extends AppCompatActivity {

    Button goBackButton;
    RecyclerView attendanceRecyclerView;
    String idNumber, lastPage;
    private FirebaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_attendances);
        dbHelper = new FirebaseHelper();
        goBackButton = findViewById(R.id.goBackBtn);

        lastPage = getIntent().getStringExtra("lastPage");
        if(lastPage.equals("StudentHomePage"))
        {
            idNumber = getIntent().getStringExtra("idNumber");

            showAttendances();
        }
        else {
            showAttendancesForProf();
        }
        goBackButton.setOnClickListener(v ->{
            if(lastPage.equals("StudentHomePage"))
            {
                Intent intent = new Intent(StudentAttendances.this, StudentHomePage.class);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(StudentAttendances.this, ProfHomePage.class);
                startActivity(intent);
            }

        });
        }



    private void showAttendances()
    {
        attendanceRecyclerView = findViewById(R.id.attendances_viewer);
        dbHelper.retrieveAttendances(idNumber)
                .addOnSuccessListener(items -> {
                    if(items != null)
                    {
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

                });

    }
    private void showAttendancesForProf()
    {
        HashMap<String, String> nameHashMap = new HashMap<>();
        HashMap<String, Integer> courseAttendancesHashMap = new HashMap<>();
        HashMap<String, Integer> seminarAttendancesHashMap = new HashMap<>();
        List<String> idNumbers = new ArrayList<>();
        dbHelper.retrieveStudentAttendances()
                .addOnSuccessListener(items -> {
                    if(items != null)
                    {
                        List<String> name = items.getName();
                        List<String> ids = items.getIdNumber();
                        List<String> classType = items.getClassType();
                        for(int i = 0; i< name.size(); i++)
                        {
                            String id = ids.get(i);
                            if(! idNumbers.contains(id))
                                idNumbers.add(id);
                            if(!nameHashMap.containsKey(id))
                                nameHashMap.put(id, name.get(i));
                            if(classType.get(i).toLowerCase().equals("curs")) {
                                if (courseAttendancesHashMap.containsKey(id))
                                    courseAttendancesHashMap.put(id, courseAttendancesHashMap.get(id) + 1);
                                else courseAttendancesHashMap.put(id, 1);
                            }
                            else
                            {
                                if(seminarAttendancesHashMap.containsKey(id))
                                    seminarAttendancesHashMap.put(id, seminarAttendancesHashMap.get(id)+1);
                                else seminarAttendancesHashMap.put(id, 1);

                            }
                        }
                        show(ids, nameHashMap, courseAttendancesHashMap, seminarAttendancesHashMap);

                    }

                });

    }
    private void show(List<String> ids, HashMap<String, String> names,
                      HashMap<String, Integer> course, HashMap<String, Integer> seminar) {
        attendanceRecyclerView = findViewById(R.id.attendances_viewer);
        List<StudentAttendancesRecyclerViewItem> attendances = new ArrayList<>();
        for (String id : ids) {
            String name = names.get(id);
            int courseAttendances = course.getOrDefault(id, 0);
            int seminarAttendances = seminar.getOrDefault(id, 0);
            attendances.add(new StudentAttendancesRecyclerViewItem(name, id, courseAttendances, seminarAttendances));
        }
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(StudentAttendances.this));
        attendanceRecyclerView.setAdapter(new StudentAttendancesAdapter(getApplicationContext(), attendances));
    }

}

