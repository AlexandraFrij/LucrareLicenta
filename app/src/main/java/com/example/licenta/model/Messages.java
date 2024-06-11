package com.example.licenta.model;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Messages
{
    List<String> messages;
    List<String> sender;
    List<Timestamp>sentTime;

    public Messages()
    {
        messages = new ArrayList<String>();
        sender = new ArrayList<String>();
        sentTime = new ArrayList<Timestamp>();
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> getSender() {
        return sender;
    }

    public List<Timestamp> getSentTime() {
        return sentTime;
    }
    public void addMessage(String message, String sender, Timestamp time)
    {
        messages.add(message);
        this.sender.add(sender);
        sentTime.add(time);
    }
}
