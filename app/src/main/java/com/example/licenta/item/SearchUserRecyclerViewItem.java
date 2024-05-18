package com.example.licenta.item;

public class SearchUserRecyclerViewItem
{
    String username;
    String email;
    int image;

    public SearchUserRecyclerViewItem(String username, int image, String email)
    {
        this.username = username;
        this.image = image;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
