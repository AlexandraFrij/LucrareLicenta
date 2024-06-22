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
import com.example.licenta.util.AlertDialogMessages;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import android.os.Environment;

import android.os.Looper;
import android.os.Handler;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




public class StudentAttendances extends AppCompatActivity {

    Button goBackButton, downloadButton;
    RecyclerView attendanceRecyclerView;
    String idNumber, lastPage;
    private FirebaseHelper dbHelper;
    private AlertDialogMessages alertDialogMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_attendances);
        dbHelper = new FirebaseHelper();
        goBackButton = findViewById(R.id.goBackBtn);
        downloadButton = findViewById(R.id.downloadBtn);
        alertDialogMessages = new AlertDialogMessages();

        lastPage = getIntent().getStringExtra("lastPage");
        if(lastPage.equals("StudentHomePage"))
        {
            idNumber = getIntent().getStringExtra("idNumber");

            showAttendancesForStudent();
        }
        else {
            showAttendancesForProf();
        }
        downloadButton.setOnClickListener(v ->{
            if(lastPage.equals("StudentHomePage"))
                prepareAndDownloadDataForStudent();
            else prepareAndDownloadDataForProf();
        });
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



    private void showAttendancesForStudent()
    {
        attendanceRecyclerView = findViewById(R.id.attendances_viewer);
        dbHelper.retrieveAttendances(idNumber)
                .addOnSuccessListener(items -> {
                    if(items != null)
                    {
                        List<AttendanceRecyclerViewItem> attendances = new ArrayList<>();
                        for( Attendance attendance : items)
                        {
                            attendances.add(new AttendanceRecyclerViewItem(attendance.getClassType(), attendance.getDate()));
                        }
                        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(StudentAttendances.this));
                        attendanceRecyclerView.setAdapter(new AttendanceAdapter(getApplicationContext(), attendances));

                    }

                });

    }
    private void showAttendancesForProf() {
        HashMap<String, String> nameHashMap = new HashMap<>();
        HashMap<String, Integer> courseAttendancesHashMap = new HashMap<>();
        HashMap<String, Integer> seminarAttendancesHashMap = new HashMap<>();

        dbHelper.retrieveStudentAttendances()
                .addOnSuccessListener(items -> {
                    if (items != null) {

                        for (Attendance attendance: items) {
                            String id = attendance.getIdNumber();
                            if (!nameHashMap.containsKey(id)) {
                                nameHashMap.put(id, attendance.getName());
                            }
                            if (attendance.getClassType().equalsIgnoreCase("curs")) {
                                courseAttendancesHashMap.put(id, courseAttendancesHashMap.getOrDefault(id, 0) + 1);
                            } else {
                                seminarAttendancesHashMap.put(id, seminarAttendancesHashMap.getOrDefault(id, 0) + 1);
                            }
                        }

                        List<StudentAttendancesRecyclerViewItem> attendances = new ArrayList<>();
                        for (String id : nameHashMap.keySet()) {
                            String studentName = nameHashMap.get(id);
                            int courseAttendances = courseAttendancesHashMap.getOrDefault(id, 0);
                            int seminarAttendances = seminarAttendancesHashMap.getOrDefault(id, 0);
                            attendances.add(new StudentAttendancesRecyclerViewItem(studentName, id, courseAttendances, seminarAttendances));
                        }

                        attendanceRecyclerView = findViewById(R.id.attendances_viewer);
                        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(StudentAttendances.this));
                        attendanceRecyclerView.setAdapter(new StudentAttendancesAdapter(getApplicationContext(), attendances));
                    }
                });
    }

    public void prepareAndDownloadDataForProf() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Attendances");
        HSSFRow hssfRow = hssfSheet.createRow(0);
        String[] columns = {"Numar matricol", "Nume student", "Eveniment", "Data"};
        final int[] rowIndex = {1};

        for (int i = 0; i < columns.length; i++) {
            HSSFCell hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(columns[i]);
        }

        dbHelper.retrieveAllStudentAttendances().addOnSuccessListener(items -> {
            if (items != null) {
                for (Attendance attendance : items) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex[0]++);

                    HSSFCell idCell = dataRow.createCell(0);
                    idCell.setCellValue(attendance.getIdNumber());

                    HSSFCell nameCell = dataRow.createCell(1);
                    nameCell.setCellValue(attendance.getName());

                    HSSFCell classTypeCell = dataRow.createCell(2);
                    classTypeCell.setCellValue(attendance.getClassType());

                    HSSFCell dateCell = dataRow.createCell(3);
                    dateCell.setCellValue(attendance.getDate());
                }
                saveWorkbook(hssfWorkbook);
            }
        }).addOnFailureListener(e -> {
            new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, "Eroare la descarcarea datelor!"));
        });
    }
    public void prepareAndDownloadDataForStudent() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Attendances");
        HSSFRow hssfRow = hssfSheet.createRow(0);
        String[] columns = {"Eveniment", "Data"};
        final int[] rowIndex = {1};

        for (int i = 0; i < columns.length; i++) {
            HSSFCell hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(columns[i]);
        }

        dbHelper.retrieveAttendances(idNumber).addOnSuccessListener(items -> {
            if (items != null) {

                for (Attendance attendance : items) {
                    HSSFRow dataRow = hssfSheet.createRow(rowIndex[0]++);

                    HSSFCell classTypeCell = dataRow.createCell(0);
                    classTypeCell.setCellValue(attendance.getClassType());

                    HSSFCell dateCell = dataRow.createCell(1);
                    dateCell.setCellValue(attendance.getDate());
                }
                saveWorkbook(hssfWorkbook);
            }
        }).addOnFailureListener(e -> {
            new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, "Eroare la descarcarea datelor!"));
        });
    }

    private void saveWorkbook(HSSFWorkbook hssfWorkbook) {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File fileOutput = new File(downloadsDir, "attendances.xls");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showSuccessDialog(this, "Date descarcate cu succes!"));
        } catch (Exception e) {
            new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, "Eroare la descarcarea datelor!"));
            throw new RuntimeException(e);
        }
    }

}
