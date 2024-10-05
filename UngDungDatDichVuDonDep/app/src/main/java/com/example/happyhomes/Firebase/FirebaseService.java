package com.example.happyhomes.Firebase;

import com.example.happyhomes.Model.Service;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class FirebaseService {
    public static void saveServiceToFirebase(Service service) {
        // Get the Firebase Realtime Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference serviceRef = database.getReference("services");
        // Generate a new unique key for the service
        String serviceKey = serviceRef.push().getKey();
        // Set the value in Firebase (serviceKey ensures uniqueness)
        if (serviceKey != null) {
            serviceRef.child(serviceKey).setValue(service)
                    .addOnSuccessListener(aVoid -> {
                        // Success callback
                        System.out.println("Service successfully saved to Firebase.");
                    })
                    .addOnFailureListener(e -> {
                        // Failure callback
                        System.out.println("Failed to save service: " + e.getMessage());
                    });
        }
    }
}

