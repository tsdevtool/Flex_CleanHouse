package com.example.happyhomes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.happyhomes.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    public static final String DB_NAME="data_app_cleaning.db";
    public static final String TBL_NAME = "CUSTOMER";

    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        login();
        openDB();
        eventRegister();
    }

    private void openDB() {
        db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
    }
    private void eventRegister() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEmail.getText().toString();
                String name = binding.txtFullName.getText().toString();
                String pass = binding.txtPassword.getText().toString();

                /* Cursor c = db.rawQuery("INSERT INTO "+ TBL_NAME+" (EMAIL, CUSNAME, PASSWORD) VALUES ( " + email +","+name+","+pass+")", null);*/
                String sql = "INSERT INTO " + TBL_NAME + " (EMAIL, CUSNAME, PASSWORD) VALUES ('" + email + "','" + name + "','" + pass + "')";

                try {
                    db.execSQL(sql);
                    gotoLogin();
                    Toast.makeText(RegisterActivity.this, "Registration Successful.\n You can login now!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void gotoLogin()
    {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void login() {
        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLogin();
            }
        });
    }
}