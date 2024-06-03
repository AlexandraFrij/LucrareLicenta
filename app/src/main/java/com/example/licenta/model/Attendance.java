package com.example.licenta.model;

import java.util.ArrayList;
import java.util.List;

public class Attendance {
    List<String> name;
    List<String> date;

    public Attendance() {
        name = new ArrayList<String>();
        date = new ArrayList<String>();
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
    public void addAttendance(String name, String date)
    {
        this.name.add(name);
        this.date.add(date);
    }
}
