package com.example.happyhomes.Customer;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happyhomes.Model.User;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityEditDetailsProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDetailsProfileActivity extends AppCompatActivity {

    private ActivityEditDetailsProfileBinding binding;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Thiết lập View Binding
        binding = ActivityEditDetailsProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận userId từ Intent
        String userId = getIntent().getStringExtra("userId");

        // Khởi tạo Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Truy xuất thông tin người dùng từ Firebase
        if (userId != null) {
            loadUserData(userId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
        }

        // Xử lý sự kiện bấm nút Lưu
        binding.saveButton.setOnClickListener(v -> {
            String updatedName = binding.editTextUserName.getText().toString();
            String updatedEmail = binding.editTextEmail.getText().toString();
            String updatedPhoneNumber = binding.editTextPhoneNumber.getText().toString();

            // Lưu thông tin cập nhật
            saveUserInfo(userId, updatedName, updatedEmail, updatedPhoneNumber);
        });

        // Xử lý sự kiện bấm nút quay lại
        binding.back.setOnClickListener(v -> {
            finish();  // Kết thúc activity hiện tại và quay lại activity trước đó
        });
    }

    private void loadUserData(String userId) {
        // Truy xuất dữ liệu từ Firebase
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu người dùng từ Firebase
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // Hiển thị dữ liệu người dùng lên các trường EditText
                        binding.editTextUserName.setText(user.name);
                        binding.editTextEmail.setText(user.email);
                        binding.editTextPhoneNumber.setText(user.phoneNumber);
                    }
                } else {
                    Toast.makeText(EditDetailsProfileActivity.this, "Không tìm thấy người dùng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditDetailsProfileActivity.this, "Lỗi cơ sở dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserInfo(String userId, String name, String email, String phoneNumber) {
        // Cập nhật thông tin người dùng trong Firebase
        DatabaseReference userRef = mDatabase.child(userId);
        userRef.child("name").setValue(name);
        userRef.child("email").setValue(email);
        userRef.child("phoneNumber").setValue(phoneNumber).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditDetailsProfileActivity.this, "Cập nhật thông tin thành công.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditDetailsProfileActivity.this, "Cập nhật thất bại.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
