package com.example.licenta.item;

import java.sql.Timestamp;

public class RecentChatsRecyclerViewItem {
    String username;
    String message;
    String email;
    int image;
    String timestamp;

    public RecentChatsRecyclerViewItem(String username, String message, String email, int image, String timestamp) {
        this.username = username;
        this.message = message;
        this.email = email;
        this.image = image;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public int getImage() {
        return image;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
