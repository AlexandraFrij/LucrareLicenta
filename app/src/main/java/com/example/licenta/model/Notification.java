package com.example.licenta.model;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    List<String> content;
    List<String> date;
    List<String> type;

    public Notification() {
        this.content = new ArrayList<String>();
        this.date = new ArrayList<String>();
        this.type = new ArrayList<String>();
    }

    public List<String> getContent() {
        return content;
    }

    public List<String> getDate() {
        return date;
    }

    public List<String> getType() {
        return type;
    }

    public void addNotification(String content, String date, String type)
    {
        this.content.add(content);
        this.date.add(date);
        this.type.add(type);
    }
}
