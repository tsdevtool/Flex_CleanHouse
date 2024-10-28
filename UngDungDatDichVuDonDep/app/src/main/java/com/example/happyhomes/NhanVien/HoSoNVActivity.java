package com.example.happyhomes.NhanVien;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happyhomes.Model.User;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityHoSoNvactivityBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HoSoNVActivity extends AppCompatActivity {

    ActivityHoSoNvactivityBinding binding;
    private String priceSer;
    String emId;
    String emName;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Hồ Sơ Của Tôi");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHoSoNvactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        emId = intent.getStringExtra("EplId");
        emName = intent.getStringExtra("EplName");
        binding.txtUserName.setText(emName);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_employee);
        bottomNavigationView.setSelectedItemId(R.id.nav_account_employee);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                if (item.getItemId() == R.id.nav_account_employee) {
                    intent = new Intent(HoSoNVActivity.this, HoSoNVActivity.class);
                    intent.putExtra("EplId", emId);
                    intent.putExtra("EplName", emName);
                    finish(); // Kết thúc Activity hiện tại.
                } else if (item.getItemId() == R.id.nav_home_employee) {
                    intent = new Intent(HoSoNVActivity.this, NhanVienActivity.class);
                    intent.putExtra("EplId", emId);
                    intent.putExtra("EplName", emName);
                } else if (item.getItemId() == R.id.nav_activity_employee) {
                    intent = new Intent(HoSoNVActivity.this, LichSuCongViecActivity.class);
                    intent.putExtra("EplId", emId);
                    intent.putExtra("EplName", emName);
                } else {
                    return false;
                }

                // Thêm EplId vào Intent
                if (intent != null) {
                    intent.putExtra("EplId", emId);
                    intent.putExtra("EplName", emName);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    private void LoadingPage()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("userId").equalTo(emId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(user.image != null)
                    {
                        Picasso.get()
                                .load(user.image)
                                .placeholder(R.drawable.icons8_document_loading_67) // Hình ảnh hiển thị trong khi tải
                                .error(R.drawable.icons8_error_48) // Hình ảnh hiển thị khi xảy ra lỗi
                                .resize(100, 100) // Thay đổi kích thước để phù hợp với CircleImageView
                                .centerCrop() // Giữ tỷ lệ khung hình và cắt hình nếu cần
                                .into(binding.imageView);
                    }else {
                        Picasso.get()
                                .load(R.drawable.icons8_document_loading_67)
                                .placeholder(R.drawable.icons8_document_loading_67) // Hình ảnh hiển thị trong khi tải
                                .error(R.drawable.icons8_error_48) // Hình ảnh hiển thị khi xảy ra lỗi
                                .resize(100, 100) // Thay đổi kích thước để phù hợp với CircleImageView
                                .centerCrop() // Giữ tỷ lệ khung hình và cắt hình nếu cần
                                .into(binding.imageView);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}