package com.example.happyhomes.Model;

import java.util.Map;

public class User {
    public String userId;
    public String name;
    public String email;
    public String phoneNumber; // New field
    public Map<String, String> addresses; // Change from List to Map
    public String role; // New field for role-based access control
    // Default constructor required for Firebase
    public User() {
    }

    public User(String userId, String name, String email, String phoneNumber, Map<String, String> addresses, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.addresses = addresses;
        this.role = role;
    }
}
