package com.example.licenta.model;

import java.util.ArrayList;
import java.util.List;

public class CalendarEvent {
    List<String> name;
    List<String> date;
    List<String> time;

    public CalendarEvent() {
        name = new ArrayList<String>();
        date = new ArrayList<String>();
        time = new ArrayList<String>();
    }

    public List<String> getName() {
        return name;
    }

    public List<String> getDate() {
        return date;
    }

    public List<String> getTime() {
        return time;
    }

    public void addEvent(String name, String date, String time)
    {
        this.name.add(name);
        this.date.add(date);
        this.time.add(time);
    }
}
