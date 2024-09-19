package com.example.happyhomes.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityMainCustomerBinding;

public class Main_CustomerActivity extends AppCompatActivity {

    private int cusID;

    ActivityMainCustomerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_customer);
        binding = ActivityMainCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadActivity();
        addEvent();

    }

    private void addEvent() {
        binding.imgClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                String cusName = intent1.getStringExtra("Cusname");
                Intent intent = new Intent(Main_CustomerActivity.this,MapCustomerActivity.class);
                intent.putExtra("CusId",cusID);
                intent.putExtra("Cusname",cusName);
                startActivity(intent);
            }
        });
        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                Intent intent = new Intent(Main_CustomerActivity.this,ProfileCustomerActivity.class);
                String cusName = intent1.getStringExtra("Cusname");
                String Phone = intent1.getStringExtra("phone");
                intent.putExtra("CusId",cusID);
                intent.putExtra("phone",Phone);
                intent.putExtra("Cusname",cusName);
                startActivity(intent);
            }
        });
    }


    public void loadActivity()
    {
        Intent intent = getIntent();
        if (intent != null) {
            String cusName = intent.getStringExtra("Cusname");
            int cusId = intent.getIntExtra("CusId",-1);

            if (cusName != null && cusId != -1) {
                binding.txtUserName.setText(String.format("Xin chào, "+ cusName));
                cusID = cusId;
            }
        }
    }

}