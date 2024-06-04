package com.example.licenta.item;

public class NotificationRecyclerViewItem {
    String content;
    String addedAt;
    int image;

    public NotificationRecyclerViewItem(String content, String addedAt, int image) {
        this.content = content;
        this.addedAt = addedAt;
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
