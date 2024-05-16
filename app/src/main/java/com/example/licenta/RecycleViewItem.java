package com.example.licenta;

public class RecycleViewItem
{
    String username;
    int image;

    public RecycleViewItem(String username,int image) {
        this.username = username;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
