package com.example.happyhomes.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happyhomes.DatabaseHelper;
import com.example.happyhomes.LoginActivity;
import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityProfileCustomerBinding;

import java.util.List;

public class ProfileCustomerActivity extends AppCompatActivity {

    ActivityProfileCustomerBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);
        loadData();
        addEvent();
    }

    private void addEvent() {
        binding.txtDangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  =new Intent(ProfileCustomerActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        // Back button event
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go back to the previous screen
                onBackPressed();
            }
        });
    }

    private void loadData() {
        Intent intent = getIntent();
        int cusId = intent.getIntExtra("CusId", -1);
        String Phone = intent.getStringExtra("phone");
        String cusName = intent.getStringExtra("Cusname");
        if (cusId != -1) {
            List<Schedule> scheduleList = databaseHelper.getSchedulesByCusId(cusId);

            if (scheduleList != null && !scheduleList.isEmpty()) {
                ScheduleAdapter adapter = new ScheduleAdapter(this, scheduleList);
                ListView listView = binding.lvLichSu;
                binding.txtName.setText(cusName);
                binding.txtPhoneNumber.setText(Phone);
                listView.setAdapter(adapter);
            } else {
                Log.d("ProfileCustomerActivity", "No schedules found for cusId: " + cusId);
            }
        } else {
            Log.d("ProfileCustomerActivity", "Invalid cusId received");
        }
    }
}
