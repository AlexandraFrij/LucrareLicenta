package com.example.licenta.item;

public class CalendarEventsRecyclerViewerItem {
    String name;
    String date;
    String time;
    String room;

    public CalendarEventsRecyclerViewerItem(String name, String date, String time, String room) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.room = room;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
