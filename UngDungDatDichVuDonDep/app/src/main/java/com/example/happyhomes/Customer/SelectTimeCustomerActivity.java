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
    private double slectedServiceCode;
    private int cusId;
    private String cusName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectTimeCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Retrieve data from Intent

        // Log to verify date and time picker setup
        Log.d("SelectTimeCustomer", "Setting up Date and Time Pickers");
        // Set up date and time pickers
        setupDatePicker();
        setupTimePicker();
        Intent intent = getIntent();
        cusId = intent.getIntExtra("CusId",-1);
        cusName = intent.getStringExtra("Cusname");
        // Set up the Next button click listener
        binding.btnNext.setOnClickListener(view -> {
            // Retrieve values from the views
            String selectedDate = binding.dateTextView.getText().toString();
            String selectedHour = binding.hourPicker.getText().toString();
            String additionalRequest = binding.edtNote.getText().toString();
            // Log the values being sent to PayAndConfirmActivity
            Log.d("SelectTimeCustomer", "Next button clicked");
            Log.d("SelectTimeCustomer", "Selected Date: " + selectedDate);
            Log.d("SelectTimeCustomer", "Selected Hour: " + selectedHour);
            Log.d("SelectTimeCustomer", "Additional Request: " + additionalRequest);
            // Create an Intent to start PayAndConfirmActivity
            Intent payAndConfirmIntent = new Intent(SelectTimeCustomerActivity.this, PayAndConfirmActivity.class);
            // Add the retrieved values to the Intent as extras
            payAndConfirmIntent.putExtra("selectedServiceId", selectedServiceId);
            payAndConfirmIntent.putExtra("selectedDate", selectedDate);
            payAndConfirmIntent.putExtra("selectedHour", selectedHour);
            payAndConfirmIntent.putExtra("additionalRequest", additionalRequest);
            payAndConfirmIntent.putExtra("adress",binding.address.getText().toString());
            // Start the PayAndConfirmActivity
            payAndConfirmIntent.putExtra("cost",binding.txtCost.getText().toString());
            payAndConfirmIntent.putExtra("CusId",cusId);
            payAndConfirmIntent.putExtra("Cusname",cusName);
            startActivity(payAndConfirmIntent);
        });
        loadData();
        addEvent();
    }

    private void addEvent() {
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
        try {
            Intent incomingIntent = getIntent();
            selectedServiceId = incomingIntent.getIntExtra("selectedServiceId", -1);
            slectedServiceCode = incomingIntent.getDoubleExtra("selectedServiceCost", 0.0);
            String selectedAdress= incomingIntent.getStringExtra("adress");
            binding.txtCost.setText(String.format("%.0f", slectedServiceCode));
            binding.address.setText(selectedAdress);
            // Log to check the value of selectedServiceId
            Log.d("SelectTimeCustomer", "Received Service ID: " + selectedServiceId);

        } catch (Exception e) {
            // Log error and show a toast
            Log.e("SelectTimeCustomer", "Error retrieving selected service ID", e);
            Toast.makeText(this, "An error occurred while retrieving the service ID.", Toast.LENGTH_SHORT).show();
        }
    }


    // Date and Time Picker methods remain the same...
    private void setupDatePicker() {
        binding.dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = + selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                        binding.dateTextView.setText(date);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    private void setupTimePicker() {
        binding.timePickerLayout.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        binding.hourPicker.setText(time);
                    },
                    hour, minute, true
            );
            timePickerDialog.show();
        });
    }
}