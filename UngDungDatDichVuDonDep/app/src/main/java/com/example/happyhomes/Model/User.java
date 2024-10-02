package com.example.happyhomes.Model;

public class User {
    public String userId;
    public String name;
    public String email;
    public String phoneNumber; // New field

    // Default constructor required for Firebase
    public User() {
    }

    public User(String userId, String name, String email, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}