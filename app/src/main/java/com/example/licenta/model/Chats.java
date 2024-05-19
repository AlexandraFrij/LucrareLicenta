package com.example.licenta.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Chats
{

    List<String> messages;
    List<String> usernames;
    List<Timestamp>sentTime;

    public Chats()
    {
        messages = new ArrayList<String>();
        usernames = new ArrayList<String>();
        sentTime = new ArrayList<Timestamp>();
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> getSender() {
        return usernames;
    }

    public List<Timestamp> getSentTime() {
        return sentTime;
    }
    public void addMessage(String message, String sender, Timestamp time)
    {
        messages.add(message);
        this.usernames.add(sender);
        sentTime.add(time);
    }

}
