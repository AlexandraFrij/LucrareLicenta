package com.example.licenta.model;

import java.util.ArrayList;
import java.util.List;

public class CalendarEvent {
    String name,date,time, room;

    public CalendarEvent(String name, String date, String time, String room)
    {
        this.name = name;
        this.date = date;
        this.time = time;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }


}
