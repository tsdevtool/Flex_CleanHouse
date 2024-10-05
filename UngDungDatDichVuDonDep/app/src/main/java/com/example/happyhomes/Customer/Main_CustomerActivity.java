package com.example.happyhomes.Customer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.happyhomes.Firebase.FirebaseService;
import com.example.happyhomes.Model.Service;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityMainCustomerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main_CustomerActivity extends AppCompatActivity {

    ActivityMainCustomerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        // Set up Bottom Navigation
//        Service service1 = new Service(1L, "Dọn dẹp nhỏ - Dưới 50m²", 200000.0);
//        Service service2 = new Service(2L, "Dọn dẹp nhỏ - 50m² đến 100m²", 300000.0);
//        Service service3 = new Service(3L, "Dọn dẹp nhỏ - Trên 100m²", 400000.0);
//        Service service4 = new Service(4L, "Dọn dẹp lớn - Dưới 100m²", 375000.0);
//        Service service5 = new Service(5L, "Dọn dẹp lớn - 100m² đến 200m²", 450000.0);
//        Service service6 = new Service(6L, "Dọn dẹp lớn - Trên 200m²", 500000.0);
//
//        // Saving each service to Firebase
//        FirebaseService.saveServiceToFirebase(service1);
//        FirebaseService.saveServiceToFirebase(service2);
//        FirebaseService.saveServiceToFirebase(service3);
//        FirebaseService.saveServiceToFirebase(service4);
//        FirebaseService.saveServiceToFirebase(service5);
//        FirebaseService.saveServiceToFirebase(service6);
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_activity) {
                    selectedFragment = new ScheduleHistoryFragment();
                } else if (itemId == R.id.nav_message) {
                    selectedFragment = new MessageFragment();
                } else if (itemId == R.id.nav_account) {
                    selectedFragment = new ProfileFragment();
                }

                return loadFragment(selectedFragment);
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.nav_home);  // Ensure default home selection is handled
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
