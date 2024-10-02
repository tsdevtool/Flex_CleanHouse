package com.example.happyhomes.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityMainCustomerBinding;

public class Main_CustomerActivity extends AppCompatActivity {

    private String cusID;  // Sử dụng String để lưu trữ cusID thay vì int, vì userId trong Firebase thường là String

    ActivityMainCustomerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadActivity();
        addEvent();
    }

    private void addEvent() {
        binding.imgClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_CustomerActivity.this, MapCustomerActivity.class);
                intent.putExtra("CusId", cusID);  // Truyền CusId
                intent.putExtra("Cusname", binding.txtUserName.getText().toString().replace("Xin chào, ", ""));  // Truyền tên người dùng
                startActivity(intent);
            }
        });

        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_CustomerActivity.this, ProfileCustomerActivity.class);
                intent.putExtra("CusId", cusID);  // Truyền CusId
                intent.putExtra("Cusname", binding.txtUserName.getText().toString().replace("Xin chào, ", ""));  // Truyền tên người dùng
                intent.putExtra("phone", getIntent().getStringExtra("phone"));  // Truyền số điện thoại nếu có
                startActivity(intent);
            }
        });
    }

    public void loadActivity() {
        Intent intent = getIntent();
        if (intent != null) {
            String cusName = intent.getStringExtra("Cusname");
            cusID = intent.getStringExtra("CusId");  // Retrieve CusId

            if (cusName != null && cusID != null) {
                binding.txtUserName.setText(String.format("Xin chào, %s", cusName));
            } else {
                // Log or show a message if the data is null
                Log.e("Main_CustomerActivity", "Cusname or CusId is null");
            }
        }
    }
}