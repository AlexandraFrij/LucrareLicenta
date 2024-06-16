package com.example.licenta.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Attendance {
    List<String> name;
    List<String> idNumber;

    List<String> date;
    List<String> classType;

    public Attendance() {
        name = new ArrayList<String>();
        date = new ArrayList<String>();
        idNumber = new ArrayList<String>();
        classType = new ArrayList<String>();
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(List<String> idNumber) {
        this.idNumber = idNumber;
    }

    public List<String> getClassType() {
        return classType;
    }

    public void setClassType(List<String> classType) {
        this.classType = classType;
    }

    public void addAttendance(String name, String date)
    {
        this.name.add(name);
        this.date.add(date);
    }
    public void addStudentAttendance(String name, String idNumber, String classType)
    {
        this.name.add(name);
        this.idNumber.add(idNumber);
        this.classType.add(classType);
    }
    public void addStudentFullAttendance(String name, String idNumber, String classType, String date)
    {
        this.name.add(name);
        this.idNumber.add(idNumber);
        this.classType.add(classType);
        this.date.add(date);
    }
}
