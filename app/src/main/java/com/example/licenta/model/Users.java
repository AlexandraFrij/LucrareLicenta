package com.example.licenta.model;

import java.util.ArrayList;
import java.util.List;

public class Users
{
    private List<String> usernames;
    private List<String> emails;

    public Users()
    {
        usernames = new ArrayList<String>();
        emails = new ArrayList<String>();
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public List<String> getEmails() {
        return emails;
    }
    public void addUsername(String username)
    {
        usernames.add(username);
    }
    public void addEmail(String email)
    {
        emails.add(email);
    }
}
