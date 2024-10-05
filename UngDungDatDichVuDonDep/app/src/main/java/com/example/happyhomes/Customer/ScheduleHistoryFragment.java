package com.example.happyhomes.Customer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.databinding.FragmentScheduleHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScheduleHistoryFragment extends Fragment {

    private FragmentScheduleHistoryBinding binding;
    private DatabaseReference databaseReference;
    private String cusId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScheduleHistoryBinding.inflate(inflater, container, false);

        // Load data passed from Activity
        loadActivity();

        // Load data from Firebase to show history
        loadData();

        return binding.getRoot();
    }

    private void loadActivity() {
        if (getActivity() != null && getActivity().getIntent() != null) {
            cusId = getActivity().getIntent().getStringExtra("CusId");

            if (cusId == null || cusId.isEmpty()) {
                Log.e("ScheduleHistoryFragment", "CusId is missing");
            }
        }
    }

    private void loadData() {
        if (cusId != null && !cusId.isEmpty()) {
            // Initialize Firebase reference to 'jobs/schedules'
            databaseReference = FirebaseDatabase.getInstance().getReference("jobs").child("schedules");

            // Retrieve data from Firebase
            databaseReference.orderByChild("customerId").equalTo(cusId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<Schedule> scheduleList = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Schedule schedule = snapshot.getValue(Schedule.class);
                                if (schedule != null) {
                                    scheduleList.add(schedule);
                                    Log.d("ScheduleHistoryFragment", "Schedule retrieved: " + schedule.toString());
                                }
                            }

                            if (!scheduleList.isEmpty()) {
                                ScheduleAdapter adapter = new ScheduleAdapter(getContext(), scheduleList);
                                ListView listView = binding.lvLichSu;
                                listView.setAdapter(adapter);
                            } else {
                                Log.d("ScheduleHistoryFragment", "No schedules found for cusId: " + cusId);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("ScheduleHistoryFragment", "Failed to read data from Firebase", databaseError.toException());
                        }
                    });
        } else {
            Log.e("ScheduleHistoryFragment", "CusId is null or empty");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
