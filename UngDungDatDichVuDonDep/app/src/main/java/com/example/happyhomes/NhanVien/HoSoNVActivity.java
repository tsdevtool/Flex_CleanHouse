package com.example.happyhomes.NhanVien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.happyhomes.DatabaseHelper;
import com.example.happyhomes.Model.Employee;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityHoSoNvactivityBinding;

public class HoSoNVActivity extends AppCompatActivity {

    ActivityHoSoNvactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Hồ Sơ Của Tôi");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHoSoNvactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Nhận Employee ID từ Intent
        long employeeId = getIntent().getLongExtra("EMPLOYEE_ID", -1);
        if (employeeId != -1) {
            // Tải dữ liệu nhân viên từ cơ sở dữ liệu
            loadEmployeeData(employeeId);
        }

        addEvents();


    }

    private void addEvents() {
        binding.backToNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the NhanVienActivity
                Intent intent = new Intent(HoSoNVActivity.this, NhanVienActivity.class);
                startActivity(intent);
                finish(); // Optional: Finish the current activity
            }
        });

    }

    private void loadEmployeeData(long employeeId) {
        // Khởi tạo DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Truy vấn thông tin nhân viên từ cơ sở dữ liệu
        Employee employee = dbHelper.getEmployeeById(employeeId);

        // Nếu có dữ liệu nhân viên, hiển thị lên giao diện
        if (employee != null) {
            binding.txtMaNV.setText(String.valueOf(employee.getEmId()));
            binding.txtHoTen.setText(employee.getEmName());
            binding.txtEmail.setText(employee.getEmEmail());
        }
    }

}