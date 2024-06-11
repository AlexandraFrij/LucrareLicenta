package com.example.licenta.model;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    public List<String> otherUserEmail;
    public List<String> chatRoomId;
    public List<String> userEmail;

    public ChatRoom() {
        this.otherUserEmail = new ArrayList<String>();
        this.chatRoomId = new ArrayList<String>();
        userEmail = new ArrayList<String>();
    }

    public List<String> getOtherUserEmail() {
        return otherUserEmail;
    }

    public List<String> getChatRoomId() {
        return chatRoomId;
    }

    public List<String> getUserEmail() {
        return userEmail;
    }
    public void addChatRoom(String userEmail, String otherUserEmail, String id)
    {
        this.otherUserEmail.add(otherUserEmail);
        this.userEmail.add(userEmail);
        this.chatRoomId.add(id);

    }
}
