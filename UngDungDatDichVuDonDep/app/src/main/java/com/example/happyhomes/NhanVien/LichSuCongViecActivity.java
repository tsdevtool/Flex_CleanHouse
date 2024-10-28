package com.example.happyhomes.NhanVien;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhomes.DatabaseHelper;
import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.Model.Service;
import com.example.happyhomes.Model.ServiceSchedule;
import com.example.happyhomes.Model.Workdate;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityLichSuCongViecBinding;
import com.example.happyhomes.databinding.ActivityNhanVienBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LichSuCongViecActivity extends AppCompatActivity implements ScheduleAdapter.OnScheduleClickListener {

    private ActivityLichSuCongViecBinding binding;
    private String priceSer;
    String emId;
    String emName;
    String status;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private double total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityLichSuCongViecBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        emId = intent.getStringExtra("EplId");
        emName = intent.getStringExtra("EplName");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("jobs");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_employee);
        bottomNavigationView.setSelectedItemId(R.id.nav_activity_employee);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;

                if (item.getItemId() == R.id.nav_activity_employee) {
                    intent = new Intent(LichSuCongViecActivity.this, LichSuCongViecActivity.class);
                    intent.putExtra("EplId", emId);
                    intent.putExtra("EplName", emName);
                    finish(); // Kết thúc Activity hiện tại.
                } else if (item.getItemId() == R.id.nav_home_employee) {
                    intent = new Intent(LichSuCongViecActivity.this, NhanVienActivity.class);
                    intent.putExtra("EplId", emId);
                    intent.putExtra("EplName", emName);
                } else if (item.getItemId() == R.id.nav_account_employee) {
                    intent = new Intent(LichSuCongViecActivity.this, HoSoNVActivity.class);
                    intent.putExtra("EplId", emId);
                    intent.putExtra("EplName", emName);
                } else {
                    return false; // Trả về false nếu không có item nào được chọn.
                }

                // Thêm EplId vào Intent
                if (intent != null) {
                    intent.putExtra("EplId", emId);
                    intent.putExtra("EplName", emName);
                    startActivity(intent);
                }
                return true; // Trả về true nếu một item đã được chọn và xử lý thành công.
            }
        });


        getSchedulesFromFirebase();

    }

    @Override
    public void onScheduleClick(Schedule schedule) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn hoàn thành công việc không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference mDatabase_workdate = FirebaseDatabase.getInstance().getReference("jobs").child("Workdate");
                        mDatabase_workdate.orderByChild("serScheId").
                                equalTo(schedule.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshotWork : snapshot.getChildren()) {

                                    Workdate workdate = snapshotWork.getValue(Workdate.class);
                                    Map<String, Object> statusUpdate = new HashMap<>();
                                    statusUpdate.put("status", "XONG");
                                    databaseReference.child("schedules").child(schedule.getId()).updateChildren(statusUpdate).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            databaseReference.child("Workdate").child(workdate.getWorkDateId()).updateChildren(statusUpdate).addOnCompleteListener(task1 -> {
                                                if(task1.isSuccessful())
                                                {
                                                    Toast.makeText(getApplicationContext(), "Công việc đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = getIntent();
                                                    finish();
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            // Nếu cập nhật không thành công, hiển thị lỗi
                                            Toast.makeText(getApplicationContext(), "Cập nhật thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void getSchedulesFromFirebase() {

        DatabaseReference mDatabase_workdate = FirebaseDatabase.getInstance().getReference("jobs").child("Workdate");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("jobs").child("schedules");
        DatabaseReference mDatabase_service = FirebaseDatabase.getInstance().getReference("services");
        DatabaseReference mDatabase_service_sche = FirebaseDatabase.getInstance().getReference("jobs").child("serviceSchedules");


        mDatabase_workdate.orderByChild("emId").equalTo(emId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Schedule> scheduleList = new ArrayList<>();
                List<Workdate> workdateList = new ArrayList<>();
                for (DataSnapshot snapshotWork : snapshot.getChildren()) {
                    Workdate workdate = snapshotWork.getValue(Workdate.class);
                    if(workdate != null)
                    {
                        status = workdate.getStatus();
                        mDatabase.orderByChild("id").equalTo(workdate.getSerScheId())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // Nếu không có dữ liệu, cập nhật giao diện và thoát
                                        if (!dataSnapshot.exists()) {
                                            updateUIWithScheduleList(scheduleList,null);
                                            return;
                                        }
                                        // Duyệt qua các snapshot của lịch biểu
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Schedule schedule = snapshot.getValue(Schedule.class);
                                            if (schedule != null) {
                                                // Truy vấn lấy thông tin ServiceSchedule
                                                mDatabase_service_sche.orderByChild("scheduleId").equalTo(schedule.getId())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot serviceScheduleSnapshot) {
                                                                for (DataSnapshot ser_sche : serviceScheduleSnapshot.getChildren()) {
                                                                    ServiceSchedule ser_sche_model = ser_sche.getValue(ServiceSchedule.class);
                                                                    if (ser_sche_model != null) {
                                                                        // Cập nhật thông tin của schedule
                                                                        schedule.setStatus(ser_sche_model.getServiceId().toString());

                                                                        // Lấy thông tin dịch vụ tương ứng
                                                                        mDatabase_service.orderByChild("serviceId")
                                                                                .equalTo(ser_sche_model.getServiceId())
                                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot serviceSnapshot) {
                                                                                        for (DataSnapshot service : serviceSnapshot.getChildren()) {
                                                                                            Service ser = service.getValue(Service.class);
                                                                                            if (ser != null) {
                                                                                                schedule.setStatus(ser.getServiceType());
                                                                                                priceSer = ser.getServiceCost().toString();
                                                                                                if(workdate.getStatus().equals("XONG"))
                                                                                                {
                                                                                                    total += ser.getServiceCost();
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                        // Thêm schedule vào danh sách sau khi xử lý xong
                                                                                        scheduleList.add(schedule);
                                                                                        workdateList.add(workdate);

                                                                                        // Kiểm tra điều kiện cập nhật giao diện
                                                                                        if (scheduleList.size() == dataSnapshot.getChildrenCount()) {
                                                                                            updateUIWithScheduleList(scheduleList, workdateList);
                                                                                        }
                                                                                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                                                                                        String totalStr = decimalFormat.format(total).replace(',', '.');
                                                                                        binding.txtstatus.setText("Doanh thu: "+totalStr);
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                                        Log.e("Service", "Failed to read service data", error.toException());
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                Log.e("Schedule", "Failed to read service schedules", error.toException());
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("Schedule", "Failed to read data from Firebase", databaseError.toException());
                                    }
                                });
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateUIWithScheduleList(List<Schedule> scheduleList, List<Workdate> workdates) {
        ListView listView = findViewById(R.id.ListView);
        if (!scheduleList.isEmpty()) {
            ScheduleAdapter adapter = new ScheduleAdapter(LichSuCongViecActivity.this, scheduleList, priceSer,workdates, this);
            listView.setAdapter(adapter);
        } else {
            Log.d("Schedule", "No schedules found for select");
        }
    }

}
