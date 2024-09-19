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
import com.example.happyhomes.databinding.ActivityChinhSachBinding;

public class ChinhSach extends AppCompatActivity {

    ActivityChinhSachBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Chính Sách Và Phúc Lợi");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChinhSachBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();
    }

    private void addEvents() {

        binding.layoutQuyTrinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChinhSach.this, QuyTrinh.class);
                startActivity(intent);
            }
        });

        binding.layoutHoptac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChinhSach.this, HopTac.class);
                startActivity(intent);
            }
        });

        binding.layoutVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChinhSach.this, ThuNhap.class);
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
                Intent intent = new Intent(ChinhSach.this, TaiKhoan.class);
                startActivity(intent);
            }
        });

        binding.backToNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the NhanVienActivity
                Intent intent = new Intent(ChinhSach.this, NhanVienActivity.class);
                startActivity(intent);
                finish(); // Optional: Finish the current activity
            }
        });

    }
}