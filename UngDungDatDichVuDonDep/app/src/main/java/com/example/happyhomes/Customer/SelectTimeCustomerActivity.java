package com.example.happyhomes.Customer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivitySelectTimeCustomerBinding;

import java.util.Calendar;

public class SelectTimeCustomerActivity extends AppCompatActivity {

    private ActivitySelectTimeCustomerBinding binding;
    private int selectedServiceId;
    private double selectedServiceCode;
    private String cusId;
    private String cusName;
    private String selectedDate;  // Lưu trữ ngày đã chọn
    private String selectedTime;  // Lưu trữ giờ đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectTimeCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve data from Intent
        Intent intent = getIntent();
        cusId = intent.getStringExtra("CusId");
        cusName = intent.getStringExtra("Cusname");

        // Set up the Next button click listener
        binding.btnNext.setOnClickListener(view -> {
            // Kiểm tra xem ngày và giờ có được chọn không
            if (selectedDate == null || selectedTime == null) {
                Toast.makeText(SelectTimeCustomerActivity.this, "Vui lòng chọn ngày và giờ.", Toast.LENGTH_SHORT).show();
                return;
            }

            String additionalRequest = binding.edtNote.getText().toString();

            // Tạo Intent để bắt đầu PayAndConfirmActivity
            Intent payAndConfirmIntent = new Intent(SelectTimeCustomerActivity.this, PayAndConfirmActivity.class);
            // Truyền giá trị ngày và giờ riêng biệt qua Intent
            payAndConfirmIntent.putExtra("selectedServiceId", selectedServiceId);
            payAndConfirmIntent.putExtra("selectedDate", selectedDate); // Truyền ngày
            payAndConfirmIntent.putExtra("selectedTime", selectedTime); // Truyền giờ
            payAndConfirmIntent.putExtra("additionalRequest", additionalRequest);
            payAndConfirmIntent.putExtra("adress", binding.address.getText().toString());
            payAndConfirmIntent.putExtra("cost", binding.txtCost.getText().toString());
            payAndConfirmIntent.putExtra("CusId", cusId);
            payAndConfirmIntent.putExtra("Cusname", cusName);
            startActivity(payAndConfirmIntent);
        });

        // Load data and set up events
        loadData();
        setupDateTimePicker();
        addEvent();
    }

    private void setupDateTimePicker() {
        binding.btnSelectDateTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            // Open DatePickerDialog first
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SelectTimeCustomerActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        // When date is set, open TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                SelectTimeCustomerActivity.this,
                                (timeView, hourOfDay, minute) -> {
                                    // Lưu ngày và giờ riêng biệt
                                    selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                                    selectedTime = String.format("%02d:%02d", hourOfDay, minute);

                                    // Hiển thị ngày và giờ đã chọn trên TextView
                                    binding.dateTimeTextView.setText("Ngày đã chọn: " + selectedDate + " Giờ: " + selectedTime);

                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        );
                        timePickerDialog.show();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }


    private void loadData() {
        try {
            Intent incomingIntent = getIntent();
            selectedServiceId = incomingIntent.getIntExtra("selectedServiceId", -1);
            selectedServiceCode = incomingIntent.getDoubleExtra("selectedServiceCost", 0.0);
            String selectedAddress = incomingIntent.getStringExtra("adress");
            binding.txtCost.setText(String.format("%.0f", selectedServiceCode));
            binding.address.setText(selectedAddress);

            Log.d("SelectTimeCustomer", "Received Service ID: " + selectedServiceId);
        } catch (Exception e) {
            Log.e("SelectTimeCustomer", "Error retrieving selected service ID", e);
            Toast.makeText(this, "An error occurred while retrieving the service ID.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addEvent() {
        binding.btnback.setOnClickListener(view -> onBackPressed());
    }
}
