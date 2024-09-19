package com.example.happyhomes.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happyhomes.DatabaseHelper;
import com.example.happyhomes.Model.Service;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityServiceBinding;
import java.io.IOException;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private ActivityServiceBinding binding;
    private DatabaseHelper databaseHelper;
    private List<Service> serviceList;
    private static final String TAG = "ServiceActivity";
    private int cusID;
    private String cusName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        try {
            // Initialize and copy database if needed
            databaseHelper.createDatabase();
            Log.d(TAG, "Database created successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Error initializing database", e);
            Toast.makeText(this, "Error initializing database", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        if (address != null) {
            binding.address.setText(address);
        } else {
            binding.address.setText("No address selected");
        }
        cusID = intent.getIntExtra("CusId",-1);
        cusName = intent.getStringExtra("Cusname");
        // Load services from database and display them in RadioButtons
        loadServicesIntoRadioButtons();
        addEvents();
    }

    private void loadServicesIntoRadioButtons() {
        try {
            databaseHelper.openDatabase();
            Log.d(TAG, "Database opened successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error opening database", e);
            Toast.makeText(this, "Error opening database", Toast.LENGTH_LONG).show();
            return;
        }

        serviceList = databaseHelper.getAllServices();

        if (serviceList == null || serviceList.isEmpty()) {
            Log.d(TAG, "No services available in the database.");
            Toast.makeText(this, "No services available", Toast.LENGTH_LONG).show();
            return;
        }

        if (serviceList.size() > 0) {
            String serviceInfo = serviceList.get(0).getServiceType();
            binding.radioTwoHours.setText(serviceInfo);
            Log.d(TAG, "Setting text for radioTwoHours: " + serviceInfo);
        }
        if (serviceList.size() > 1) {
            String serviceInfo = serviceList.get(1).getServiceType() ;
            binding.radioThreeHours.setText(serviceInfo);
            Log.d(TAG, "Setting text for radioThreeHours: " + serviceInfo);
        }
        if (serviceList.size() > 2) {
            String serviceInfo = serviceList.get(2).getServiceType();
            binding.radioFourHours.setText(serviceInfo);
            Log.d(TAG, "Setting text for radioFourHours: " + serviceInfo);
        }
        if (serviceList.size() > 3) {
            String serviceInfo = serviceList.get(3).getServiceType() ;
            binding.radioFiveHours.setText(serviceInfo);
            Log.d(TAG, "Setting text for radioFiveHours: " + serviceInfo);
        }
        if (serviceList.size() > 4) {
            String serviceInfo = serviceList.get(4).getServiceType() ;
            binding.radioSixHours.setText(serviceInfo);
            Log.d(TAG, "Setting text for radioSixHours: " + serviceInfo);
        }
        if (serviceList.size() > 5) {
            String serviceInfo = serviceList.get(5).getServiceType() ;
            binding.radioSevenHours.setText(serviceInfo);
            Log.d(TAG, "Setting text for radioSevenHours: " + serviceInfo);
        }

        // Automatically select the first RadioButton as checked
        binding.radioTwoHours.setChecked(true);
        updateTxtMoney(0);  // Update txtMoney with the initial service cost
        Log.d(TAG, "RadioTwoHours set to checked.");
    }

    private void addEvents() {
        binding.durationRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedIndex = -1;
            if (checkedId == R.id.radioTwoHours) {
                selectedIndex = 0;
            } else if (checkedId == R.id.radioThreeHours) {
                selectedIndex = 1;
            } else if (checkedId == R.id.radioFourHours) {
                selectedIndex = 2;
            } else if (checkedId == R.id.radioFiveHours) {
                selectedIndex = 3;
            } else if (checkedId == R.id.radioSixHours) {
                selectedIndex = 4;
            } else if (checkedId == R.id.radioSevenHours) {
                selectedIndex = 5;
            }

            if (selectedIndex != -1) {
                updateTxtMoney(selectedIndex);
            }
        });

        binding.nextButton.setOnClickListener(v -> {
            Intent selectTimeIntent = new Intent(ServiceActivity.this, SelectTimeCustomerActivity.class);

            String selectedServiceType = "";
            int selectedServiceId = -1;  // Default value if no service is selected
            double selectedServiceCost = 0; // Initialize selectedServiceCost

            if (binding.radioTwoHours.isChecked()) {
                selectedServiceType = binding.radioTwoHours.getText().toString();
                selectedServiceId = serviceList.get(0).getServiceId().intValue();
                selectedServiceCost = serviceList.get(0).getServiceCost();
            } else if (binding.radioThreeHours.isChecked()) {
                selectedServiceType = binding.radioThreeHours.getText().toString();
                selectedServiceId = serviceList.get(1).getServiceId().intValue();
                selectedServiceCost = serviceList.get(1).getServiceCost();
            } else if (binding.radioFourHours.isChecked()) {
                selectedServiceType = binding.radioFourHours.getText().toString();
                selectedServiceId = serviceList.get(2).getServiceId().intValue();
                selectedServiceCost = serviceList.get(2).getServiceCost();
            } else if (binding.radioFiveHours.isChecked()) {
                selectedServiceType = binding.radioFiveHours.getText().toString();
                selectedServiceId = serviceList.get(3).getServiceId().intValue();
                selectedServiceCost = serviceList.get(3).getServiceCost();
            } else if (binding.radioSixHours.isChecked()) {
                selectedServiceType = binding.radioSixHours.getText().toString();
                selectedServiceId = serviceList.get(4).getServiceId().intValue();
                selectedServiceCost = serviceList.get(4).getServiceCost();
            } else if (binding.radioSevenHours.isChecked()) {
                selectedServiceType = binding.radioSevenHours.getText().toString();
                selectedServiceId = serviceList.get(5).getServiceId().intValue();
                selectedServiceCost = serviceList.get(5).getServiceCost();
            }

            // Pass the selected service type, serviceId, and serviceCost to the next activity
            selectTimeIntent.putExtra("selectedServiceType", selectedServiceType);
            selectTimeIntent.putExtra("selectedServiceId", selectedServiceId);
            selectTimeIntent.putExtra("selectedServiceCost", selectedServiceCost);
            selectTimeIntent.putExtra("adress", binding.address.getText());
            selectTimeIntent.putExtra("CusId",cusID);
            selectTimeIntent.putExtra("Cusname",cusName);
            startActivity(selectTimeIntent);
        });
    }


    private void updateTxtMoney(int index) {
        if (serviceList != null && !serviceList.isEmpty() && index >= 0 && index < serviceList.size()) {
            String moneyText = serviceList.get(index).getServiceCost() + " VND/" + serviceList.get(index).getServiceType();
            binding.txtMoney.setText(moneyText);
            Log.d(TAG, "txtMoney updated: " + moneyText);
        }
    }
}