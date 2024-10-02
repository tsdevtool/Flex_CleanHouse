package com.example.happyhomes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.happyhomes.Customer.Main_CustomerActivity;
import com.example.happyhomes.Model.Customer;
import com.example.happyhomes.Model.Employee;
import com.example.happyhomes.Model.User;
import com.example.happyhomes.NhanVien.NhanVienActivity;
import com.example.happyhomes.databinding.ActivityLoginBinding;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        EventLogin();
        Register();
        handlePasswordVisibility();
    }

    private void EventLogin() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailLogin = binding.txtEmail.getText().toString();
                String passLogin = binding.txtPassword.getText().toString();

                // Sign in with Firebase Authentication
                mAuth.signInWithEmailAndPassword(emailLogin, passLogin)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();

                                // Retrieve user info from Realtime Database
                                mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User loggedInUser = dataSnapshot.getValue(User.class);
                                        if (loggedInUser != null) {
                                            Toast.makeText(LoginActivity.this, "Login SUCCESS", Toast.LENGTH_SHORT).show();

                                            // Pass phone number and other user details to the next activity
                                            Intent intent = new Intent(LoginActivity.this, Main_CustomerActivity.class);
                                            intent.putExtra("Cusname", loggedInUser.name); // This passes the username
                                            intent.putExtra("email", loggedInUser.email);
                                            intent.putExtra("CusId",loggedInUser.userId);
                                            intent.putExtra("phoneNumber", loggedInUser.phoneNumber); // Ensure this is correctly passed
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(LoginActivity.this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void Register() {
        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
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