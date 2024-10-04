package com.example.happyhomes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.happyhomes.Model.User;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.happyhomes.Model.User;
import com.example.happyhomes.databinding.ActivityRegisterBinding;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        Log.d(TAG, "Firebase Authentication and Database initialized.");

        eventRegister();
        handlePasswordVisibility();
    }

    private void eventRegister() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEmail.getText().toString();
                String name = binding.txtFullName.getText().toString();
                String pass = binding.txtPassword.getText().toString();
                String phoneNumber = binding.txtPhoneNumber.getText().toString(); // Retrieve phone number

                Log.d(TAG, "User clicked register. Email: " + email + ", Name: " + name + ", Phone Number: " + phoneNumber);

                // Register the user with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Firebase Authentication success.");

                                // Save user information to Realtime Database
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();
                                    Log.d(TAG, "User ID: " + userId);

                                    // Create a new user object with the phone number
                                    User newUser = new User(userId, name, email, phoneNumber,null);

                                    // Save user data to Realtime Database
                                    mDatabase.child(userId).setValue(newUser)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Log.d(TAG, "User info saved to Realtime Database successfully.");
                                                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                                    gotoLogin();
                                                } else {
                                                    Log.e(TAG, "Failed to save user info: " + task1.getException().getMessage());
                                                    Toast.makeText(RegisterActivity.this, "Failed to save user info: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Log.e(TAG, "FirebaseUser is null after successful registration.");
                                    Toast.makeText(RegisterActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e(TAG, "Firebase Authentication failed: " + task.getException().getMessage());
                                Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
    public void gotoLogin() {
        Log.d(TAG, "Navigating to LoginActivity.");
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void handlePasswordVisibility() {
        binding.togglePasswordVisibility.setTag(false);
        binding.togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPasswordVisible = (boolean) v.getTag();
                if (isPasswordVisible) {
                    binding.txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.togglePasswordVisibility.setImageResource(R.drawable.eye);
                } else {
                    binding.txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.togglePasswordVisibility.setImageResource(R.drawable.view);
                }
                v.setTag(!isPasswordVisible);
                binding.txtPassword.setSelection(binding.txtPassword.getText().length());
            }
        });
    }
}
