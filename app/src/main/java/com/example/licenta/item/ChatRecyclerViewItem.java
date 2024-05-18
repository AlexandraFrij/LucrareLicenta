package com.example.licenta.item;

public class ChatRecyclerViewItem
{
    String message;
    String sender;

    public ChatRecyclerViewItem(String message, String sender)
    {

        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
