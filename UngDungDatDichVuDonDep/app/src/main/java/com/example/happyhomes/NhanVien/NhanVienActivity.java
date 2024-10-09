package com.example.happyhomes.NhanVien;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happyhomes.Model.Employee;
import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityNhanVienBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NhanVienActivity extends AppCompatActivity {

    private ActivityNhanVienBinding binding;
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNhanVienBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận dữ liệu Employee từ Intent
        Intent intent = getIntent(); // Sử dụng trường Email lưu id
        employee = new Employee(null, intent.getStringExtra("EplName"), intent.getStringExtra("EplId"), null);
        binding.txtusername.setText("Xin chào, " + employee.getEmName());

        // Lấy dữ liệu từ Firebase
        getSchedulesFromFirebase();
    }

    private void getSchedulesFromFirebase() {

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("jobs").child("schedules");
        List<String> scheduleIdList = new ArrayList<>();
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })
       /* DatabaseReference schedulesRef = FirebaseDatabase.getInstance().getReference("jobs").child("schedules");

        Log.d("Firebase", "Đang bắt đầu lấy dữ liệu lịch làm việc");

        schedulesRef.orderByChild("status").equalTo("Đang chờ").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Firebase", "Nhận dữ liệu từ Firebase");

                if (dataSnapshot.exists()) {
                    Log.d("Firebase", "Dữ liệu tồn tại: " + dataSnapshot.getChildrenCount() + " bản ghi");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Schedule schedule = snapshot.getValue(Schedule.class);
                        if (schedule != null) {
                            Log.d("Firebase", "ID: " + snapshot.getKey() + " - Lịch: " + schedule.toString());
                        } else {
                            Log.d("Firebase", "Không thể ánh xạ dữ liệu lịch từ snapshot: " + snapshot.toString());
                        }
                    }
                } else {
                    Log.d("Firebase", "Không tìm thấy dữ liệu");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Lỗi khi lấy dữ liệu: " + databaseError.getMessage());
            }
        });*/

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("jobs/schedules");
       /* ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                    String location = scheduleSnapshot.child("location").getValue(String.class);
                    String date = scheduleSnapshot.child("dateString").getValue(String.class);
                    String startTime = scheduleSnapshot.child("startTimeString").getValue(String.class);
                    String status = scheduleSnapshot.child("status").getValue(String.class);

                    // Xử lý các giá trị ở đây, ví dụ như hiển thị trên UI
                    Log.d("ScheduleData", "Location: " + location + ", Date: " + date + ", Start Time: " + startTime + ", Status: " + status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi
                Log.e("ScheduleData", "Failed to read schedules", databaseError.toException());
            }
        });*/
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Firebase", "onDataChange called");
                // Xử lý dữ liệu tại đây
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });

    }
}
