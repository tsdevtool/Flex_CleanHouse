package com.example.happyhomes.Customer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

        // Set up Bottom Navigation
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
                    selectedFragment = new ActivityFragment();
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
