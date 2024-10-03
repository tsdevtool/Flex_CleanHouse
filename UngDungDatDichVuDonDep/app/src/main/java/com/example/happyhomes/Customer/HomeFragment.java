package com.example.happyhomes.Customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.happyhomes.R;
import com.example.happyhomes.databinding.FragmentHomeBinding;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private String cusID;
    private FragmentHomeBinding binding;
    private BanerAdapter bannerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Load initial content
        loadActivity();
        addEvent();
        setupBanner();
        return binding.getRoot();

    }

    private void addEvent() {
        // Handle click events for imgClean and imgProfile
        binding.imgClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapCustomerActivity.class);
                intent.putExtra("CusId", cusID);  // Truyền CusId
                intent.putExtra("Cusname", binding.txtUserName.getText().toString().replace("Xin chào, ", ""));  // Truyền tên người dùng
                startActivity(intent);
            }
        });
    }

    public void loadActivity() {
        // Get the customer data from Main_CustomerActivity
        if (getActivity().getIntent() != null) {
            Intent intent = getActivity().getIntent();
            String cusName = intent.getStringExtra("Cusname");
            cusID = intent.getStringExtra("CusId");  // Retrieve CusId
            if (cusName != null && cusID != null) {
                binding.txtUserName.setText(String.format("Xin chào, %s", cusName));
            } else {
                // Log or show a message if the data is null
                Log.e("HomeFragment", "Cusname or CusId is null");
            }
        }
    }
    private void setupBanner() {
        List<Integer> bannerList = Arrays.asList(R.drawable.banner, R.drawable.banner, R.drawable.banner);
        bannerAdapter = new BanerAdapter(getActivity(), bannerList);
        binding.viewPager.setAdapter(bannerAdapter);

        // Auto-slide logic
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                int currentPage = binding.viewPager.getCurrentItem();
                if (currentPage == bannerList.size() - 1) {
                    binding.viewPager.setCurrentItem(0);
                } else {
                    binding.viewPager.setCurrentItem(currentPage + 1, true);
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000, 3000); // Slide every 3 seconds
    }
}
