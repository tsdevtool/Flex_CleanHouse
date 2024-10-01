package com.example.happyhomes.NhanVien;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhomes.DatabaseHelper;
import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private ListView lvCongViec;
    private ScheduleAdapter scheduleAdapter;
    private DatabaseHelper databaseHelper;
    private long employeeId;
    private RecyclerView recyclerViewDates;
    private Date selectedDate;
    private boolean isCaLamSelected = true; // Flag to track if "caLam" is selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        lvCongViec = findViewById(R.id.lvCongViec);
        TextView caLam = findViewById(R.id.caLam);
        TextView lichDK = findViewById(R.id.lichDK);
        LinearLayout backToNV = findViewById(R.id.backToNV);
        recyclerViewDates = findViewById(R.id.recyclerViewDates);

        databaseHelper = new DatabaseHelper(this);
        employeeId = getIntent().getLongExtra("EMPLOYEE_ID", -1);

        setupDateRecyclerView();

        // Load schedules for the current date by default
        selectedDate = new Date();
        refreshData();

        backToNV.setOnClickListener(v -> {
            Intent intent = new Intent(ScheduleActivity.this, NhanVienActivity.class);
            startActivity(intent);
            finish();
        });
        caLam.setOnClickListener(v -> {
            isCaLamSelected = true; // Set flag for caLam
            // Đặt màu nền cam cho caLam và xóa màu nền của lichDK
            caLam.setBackgroundColor(getResources().getColor(R.color.orange));
            lichDK.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            loadSchedulesForDateAndStatus(selectedDate, Arrays.asList("Đang chờ"));
        });

        lichDK.setOnClickListener(v -> {
            isCaLamSelected = false; // Set flag for lichDK
            // Đặt màu nền cam cho lichDK và xóa màu nền của caLam
            lichDK.setBackgroundColor(getResources().getColor(R.color.orange));
            caLam.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            loadSchedulesForDateAndStatus(selectedDate, Arrays.asList("Đã xác nhận"));
        });

        lvCongViec.setOnItemClickListener((parent, view, position, id) -> {
            Schedule schedule = (Schedule) parent.getItemAtPosition(position);
            long scheduleId = schedule.getScheduleId();
            Log.d("ScheduleActivity", "Schedule ID: " + scheduleId);

            Intent intent = new Intent(ScheduleActivity.this, ScheduleDetailActivity.class);
            intent.putExtra("SCHEDULE_ID", scheduleId);
            startActivity(intent);
        });
    }

    private void setupDateRecyclerView() {
        List<Date> dateList = getDateList(30);  // Lấy danh sách 30 ngày tiếp theo
        DateAdapter dateAdapter = new DateAdapter(dateList, date -> {
            selectedDate = date;
            // Kiểm tra flag và load dữ liệu tương ứng
            if (isCaLamSelected) {
                loadSchedulesForDateAndStatus(date, Arrays.asList("Đang chờ"));
            } else {
                loadSchedulesForDateAndStatus(date, Arrays.asList("Đã xác nhận"));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDates.setLayoutManager(layoutManager);
        recyclerViewDates.setAdapter(dateAdapter);
    }

    private List<Date> getDateList(int days) {
        Calendar calendar = Calendar.getInstance();
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            dateList.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return dateList;
    }

    private void loadSchedulesForDateAndStatus(Date date, List<String> statuses) {
        List<Schedule> schedulesForDate = databaseHelper.getSchedulesByDateAndStatus(date, statuses);
        scheduleAdapter = new ScheduleAdapter(this, schedulesForDate, employeeId, true);
        lvCongViec.setAdapter(scheduleAdapter);
    }

    public void refreshData() {
        // Mặc định load các schedule có trạng thái là "Đang chờ" cho ngày hiện tại
        loadSchedulesForDateAndStatus(selectedDate, Arrays.asList("Đang chờ"));
    }


}
