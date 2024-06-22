package com.example.licenta.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Attendance {
    String name, idNumber,date, classType;

    public Attendance() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public void addAttendance(String name, String date)
    {
        this.name = name;
        this.date = date;
    }
    public void addStudentAttendance(String name, String idNumber, String classType)
    {
        this.name =name;
        this.idNumber = idNumber;
        this.classType = classType;
    }
    public void addStudentFullAttendance(String name, String idNumber, String classType, String date)
    {
        this.name = name;
        this.idNumber = idNumber ;
        this.classType = classType;
        this.date = date;
    }
}
