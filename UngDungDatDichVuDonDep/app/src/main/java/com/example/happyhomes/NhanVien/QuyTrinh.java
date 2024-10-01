package com.example.happyhomes.NhanVien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityQuyTrinhBinding;

public class QuyTrinh extends AppCompatActivity {

    ActivityQuyTrinhBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Quy Trình Xử Lý Đơn");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityQuyTrinhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();

    }

    private void addEvents() {

        binding.layoutChinhSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuyTrinh.this, ChinhSach.class);
                startActivity(intent);
            }
        });

        binding.layoutHoptac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuyTrinh.this, HopTac.class);
                startActivity(intent);
            }
        });

        binding.layoutVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuyTrinh.this, ThuNhap.class);
                startActivity(intent);
            }
        });

//        binding.layoutPhanAnh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HoTroActivity.this, QuyTrinh.class);
//                startActivity(intent);
//            }
//        });

        binding.layoutTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuyTrinh.this, TaiKhoan.class);
                startActivity(intent);
            }
        });
    }
}