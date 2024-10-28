package com.example.happyhomes.NhanVien;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhomes.Customer.HomeFragment;
import com.example.happyhomes.Customer.Main_CustomerActivity;
import com.example.happyhomes.Customer.MessageFragment;
import com.example.happyhomes.Customer.ProfileFragment;
import com.example.happyhomes.Customer.ScheduleHistoryFragment;
import com.example.happyhomes.Model.Service;
import com.example.happyhomes.Model.ServiceSchedule;
import com.example.happyhomes.Model.Workdate;
import com.example.happyhomes.NhanVien.ScheduleAdapter;
import com.example.happyhomes.LoginActivity;
import com.example.happyhomes.Model.Employee;
import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.Model.User;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityNhanVienBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NhanVienActivity extends AppCompatActivity implements ScheduleAdapter.OnScheduleClickListener {

    private ActivityNhanVienBinding binding;
    private Employee employee;
    private DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String workId;
    String emId;
    String priceSer;
    String statusWork = "Hoàn thành?";
     // Biến toàn cục để lưu trữ trạng thái
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNhanVienBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("jobs");
        workId = databaseReference.push().getKey();
        // Nhận dữ liệu Employee từ Intent
        Intent intent = getIntent(); // Sử dụng trường Email lưu id
        employee = new Employee(null, intent.getStringExtra("EplName"), intent.getStringExtra("EplId"), null);
        binding.txtusername.setText("Xin chào, " + employee.getEmName());
        emId = intent.getStringExtra("EplId");
        check_work(new WorkCheckCallback() {
            @Override
            public void onWorkCheckComplete(boolean isWorkCompleted) {
                if (isWorkCompleted) {
                    binding.txtstatus.setText("Chưa nhận công việc");
                } else {
                    binding.txtstatus.setText("Bạn đang thực hiện công việc");
                }
            }
        });
        getSchedulesFromFirebase();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_employee);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home_employee) {
                    Intent intent1 = new Intent(NhanVienActivity.this, NhanVienActivity.class);
                    intent1.putExtra("EplId", emId);
                    intent1.putExtra("EplName", employee.getEmName());
                    startActivity(intent1);
                } else if (item.getItemId() == R.id.nav_activity_employee) {
                    Intent intent1 = new Intent(NhanVienActivity.this, LichSuCongViecActivity.class);
                    intent1.putExtra("EplId", emId);
                    intent1.putExtra("EplName", employee.getEmName());
                    startActivity(intent1);

                } else if (item.getItemId() == R.id.nav_account_employee) {
                    Intent intent1 = new Intent(NhanVienActivity.this, HoSoNVActivity.class);
                    intent1.putExtra("EplId", emId);
                    intent1.putExtra("EplName", employee.getEmName());
                    startActivity(intent1);
                }
                return false;
            }
        });
    }

    @Override
    public void onScheduleClick(Schedule schedule) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn nhận công việc này không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        check_work(new WorkCheckCallback() {
                            @Override
                            public void onWorkCheckComplete(boolean isWorkCompleted) {
                                if (isWorkCompleted) {
                                    Workdate workdate = new Workdate(
                                            workId,
                                            emId,
                                            schedule.getId(),
                                            statusWork
                                    );
                                    databaseReference.child("Workdate").child(workId).setValue(workdate);
                                    Map<String, Object> statusUpdate = new HashMap<>();
                                    statusUpdate.put("status", "Đã nhận");
                                    databaseReference.child("schedules").child(schedule.getId()).updateChildren(statusUpdate).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Công việc đã được nhận thành công!", Toast.LENGTH_SHORT).show();
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                        } else {
                                            // Nếu cập nhật không thành công, hiển thị lỗi
                                            Toast.makeText(getApplicationContext(), "Cập nhật thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Không thể nhận thêm công việc.", Toast.LENGTH_SHORT).show();
                                }
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
       DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("jobs").child("schedules");
       DatabaseReference mDatabase_service = FirebaseDatabase.getInstance().getReference("services");
       DatabaseReference mDatabase_service_sche = FirebaseDatabase.getInstance().getReference("jobs").child("serviceSchedules");

       mDatabase.orderByChild("status").equalTo("Đang chờ")
               .addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       List<Schedule> scheduleList = new ArrayList<>();

                       // Nếu không có dữ liệu, cập nhật giao diện và thoát
                       if (!dataSnapshot.exists()) {
                           updateUIWithScheduleList(scheduleList);
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
                                                                           }
                                                                       }

                                                                       // Thêm schedule vào danh sách sau khi xử lý xong
                                                                       scheduleList.add(schedule);

                                                                       // Kiểm tra điều kiện cập nhật giao diện
                                                                       if (scheduleList.size() == dataSnapshot.getChildrenCount()) {
                                                                           updateUIWithScheduleList(scheduleList);
                                                                       }
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
    // Phương thức cập nhật giao diện
    private void updateUIWithScheduleList(List<Schedule> scheduleList) {
        ListView listView = findViewById(R.id.ListView);
        if (!scheduleList.isEmpty()) {
            ScheduleAdapter adapter = new ScheduleAdapter(NhanVienActivity.this, scheduleList, priceSer,null, this);
            listView.setAdapter(adapter);
        } else {
            Log.d("Schedule", "No schedules found for select");
        }
    }

    public interface WorkCheckCallback {
        void onWorkCheckComplete(boolean isWorkCompleted);
    }
    public void check_work(WorkCheckCallback callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("jobs").child("Workdate");
        mDatabase.orderByChild("emId").equalTo(emId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isWorkCompleted = true;  // Assume work is completed unless found otherwise
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Workdate workdate = dataSnapshot.getValue(Workdate.class);
                    if (statusWork.equals(workdate.getStatus())) {
                        isWorkCompleted = false;  // Found a matching status, so work is not completed
                        break;
                    }
                }
                // Pass the result to the callback
                callback.onWorkCheckComplete(isWorkCompleted);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors here if needed
                Log.e("Firebase", "Database error: " + error.getMessage());
            }
        });
    }
}
