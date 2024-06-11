package com.example.licenta.item;

public class StudentAttendancesRecyclerViewItem {
    String name;
    String idNumber;
    int courseAttendances;
    int seminarAttendances;

    public StudentAttendancesRecyclerViewItem(String name, String idNumber, int courseAttendances, int seminarAttendances) {
        this.name = name;
        this.idNumber = idNumber;
        this.courseAttendances = courseAttendances;
        this.seminarAttendances = seminarAttendances;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public int getCourseAttendances() {
        return courseAttendances;
    }

    public void setCourseAttendances(int courseAttendances) {
        this.courseAttendances = courseAttendances;
    }

    public int getSeminarAttendances() {
        return seminarAttendances;
    }

    public void setSeminarAttendances(int seminarAttendances) {
        this.seminarAttendances = seminarAttendances;
    }
}
