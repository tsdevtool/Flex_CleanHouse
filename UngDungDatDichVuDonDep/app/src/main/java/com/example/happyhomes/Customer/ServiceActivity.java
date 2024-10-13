package com.example.happyhomes.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happyhomes.Model.Service;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityServiceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private ActivityServiceBinding binding;
    private List<Service> serviceList = new ArrayList<>();
    private static final String TAG = "ServiceActivity";
    private String cusID;
    private String cusName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        if (address != null) {
            binding.address.setText(address);
        } else {
            binding.address.setText("No address selected");
        }
        cusID = intent.getStringExtra("CusId");
        cusName = intent.getStringExtra("Cusname");

        // Load services from Firebase and display them in RadioButtons
        loadServicesFromFirebase();
        addEvents();
    }

    private void loadServicesFromFirebase() {
        DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference("services");

        serviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    if (service != null) {
                        serviceList.add(service);
                    }
                }

                // Populate the RadioButtons with the retrieved service data
                populateRadioButtons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error loading services: " + databaseError.getMessage());
                Toast.makeText(ServiceActivity.this, "Error loading services", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateRadioButtons() {
        if (serviceList.isEmpty()) {
            Log.d(TAG, "No services available in Firebase.");
            Toast.makeText(this, "No services available", Toast.LENGTH_LONG).show();
            return;
        }

        if (serviceList.size() > 0) {
            binding.radioTwoHours.setText(serviceList.get(0).getServiceType());
            Log.d(TAG, "Setting text for radioTwoHours: " + serviceList.get(0).getServiceType());
        }
        if (serviceList.size() > 1) {
            binding.radioThreeHours.setText(serviceList.get(1).getServiceType());
            Log.d(TAG, "Setting text for radioThreeHours: " + serviceList.get(1).getServiceType());
        }
        if (serviceList.size() > 2) {
            binding.radioFourHours.setText(serviceList.get(2).getServiceType());
            Log.d(TAG, "Setting text for radioFourHours: " + serviceList.get(2).getServiceType());
        }
        if (serviceList.size() > 3) {
            binding.radioFiveHours.setText(serviceList.get(3).getServiceType());
            Log.d(TAG, "Setting text for radioFiveHours: " + serviceList.get(3).getServiceType());
        }
        if (serviceList.size() > 4) {
            binding.radioSixHours.setText(serviceList.get(4).getServiceType());
            Log.d(TAG, "Setting text for radioSixHours: " + serviceList.get(4).getServiceType());
        }
        if (serviceList.size() > 5) {
            binding.radioSevenHours.setText(serviceList.get(5).getServiceType());
            Log.d(TAG, "Setting text for radioSevenHours: " + serviceList.get(5).getServiceType());
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
            selectTimeIntent.putExtra("CusId", cusID);
            selectTimeIntent.putExtra("Cusname", cusName);
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
