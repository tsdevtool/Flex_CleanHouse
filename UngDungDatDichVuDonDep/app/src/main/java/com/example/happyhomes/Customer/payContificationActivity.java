package com.example.happyhomes.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityPayContificationBinding;

public class payContificationActivity extends AppCompatActivity {
    ActivityPayContificationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPayContificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String cusName = intent.getStringExtra("Cusname");
        int cusId = intent.getIntExtra("CusId",-1);
        binding.textviewnotifi.setText(intent.getStringExtra("result"));
        binding.btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(payContificationActivity.this,Main_CustomerActivity.class);
                intent1.putExtra("Cusname", cusName);
                intent1.putExtra("CusId", cusId);
                startActivity(intent1);
            }
        });
    }
}