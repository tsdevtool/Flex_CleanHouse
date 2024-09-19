package com.example.happyhomes.NhanVien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

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

public class LichSuCongViecActivity extends AppCompatActivity {

    private ListView lvLSCongViec;
    private ScheduleAdapter scheduleAdapter;
    private DatabaseHelper databaseHelper;
    private long employeeId;
    private RecyclerView recyclerViewDates;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_cong_viec);

        lvLSCongViec = findViewById(R.id.lvLSCongViec);
        recyclerViewDates = findViewById(R.id.recyclerViewDates);

        databaseHelper = new DatabaseHelper(this);
        employeeId = getIntent().getLongExtra("EMPLOYEE_ID", -1);

        setupDateRecyclerView();

        // Load completed schedules for the current date by default
        selectedDate = new Date();
        loadCompletedSchedulesForDate(selectedDate);

        LinearLayout backToNV = findViewById(R.id.backToNV);
        backToNV.setOnClickListener(v -> {
            Intent intent = new Intent(LichSuCongViecActivity.this, NhanVienActivity.class);
            startActivity(intent);
            finish();
        });

        lvLSCongViec.setOnItemClickListener((parent, view, position, id) -> {
            Schedule schedule = (Schedule) parent.getItemAtPosition(position);
            Intent intent = new Intent(LichSuCongViecActivity.this, ScheduleDetailActivity.class);
            intent.putExtra("SCHEDULE_ID", schedule.getScheduleId());
            startActivity(intent);
        });
    }

    private void setupDateRecyclerView() {
        List<Date> dateList = getDateList(30);  // Get the next 30 days
        DateAdapter dateAdapter = new DateAdapter(dateList, date -> {
            selectedDate = date;
            loadCompletedSchedulesForDate(date);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDates.setLayoutManager(layoutManager);
        recyclerViewDates.setAdapter(dateAdapter);
    }

    private List<Date> getDateList(int days) {
        Calendar calendar = Calendar.getInstance();
        List<Date> dateList = new ArrayList<>();

        // Lùi lại 30 ngày trước
        calendar.add(Calendar.DAY_OF_YEAR, -(days - 1));

        for (int i = 0; i < days; i++) {
            dateList.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return dateList;
    }


    private void loadCompletedSchedulesForDate(Date date) {
        List<Schedule> completedSchedules = databaseHelper.getSchedulesByDateAndStatus(date, Arrays.asList("Hoàn tất"));
        scheduleAdapter = new ScheduleAdapter(this, completedSchedules, employeeId, true);
        lvLSCongViec.setAdapter(scheduleAdapter);
    }
}
