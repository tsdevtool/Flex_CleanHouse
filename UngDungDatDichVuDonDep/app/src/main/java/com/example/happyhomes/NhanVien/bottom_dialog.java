package com.example.happyhomes.NhanVien;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.happyhomes.R;
import com.example.happyhomes.databinding.BottomDialogBinding;

public class bottom_dialog extends AppCompatActivity {

    BottomDialogBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = BottomDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận dữ liệu từ Intent
        String employeeName = getIntent().getStringExtra("EmployeeName");
        // Hiển thị dữ liệu
        if (employeeName != null) {
            binding.txtusername.setText(employeeName);
        }

    }
}