package com.example.happyhomes.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.happyhomes.Model.User;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityEditDetailsProfileBinding;
import com.example.happyhomes.databinding.ActivityEditProfileCustomerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditProfileCustomer extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private LinearLayout llSavedLocations;
    private TextView tvUserName, tvPhoneNumber, tvAddNewAddress;
    private RecyclerView recyclerViewSavedLocations;
    private SavedLocationsAdapter adapter;
    ActivityEditProfileCustomerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase Auth và Database Reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        tvUserName = findViewById(R.id.tvUserName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvAddNewAddress = findViewById(R.id.tvAddNewAddress);

        // Khởi tạo RecyclerView
        recyclerViewSavedLocations = findViewById(R.id.recyclerViewSavedLocations); // Thêm dòng này
        recyclerViewSavedLocations.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavedLocationsAdapter(new ArrayList<>());
        recyclerViewSavedLocations.setAdapter(adapter);

        // Load thông tin người dùng
        loadUserInfo();
        // Gán sự kiện bấm vào "Thêm địa chỉ mới"
        tvAddNewAddress.setOnClickListener(v -> showAddAddressDialog());

        binding.imgProfile.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid(); // Lấy userId hiện tại
                Intent intent = new Intent(EditProfileCustomer.this, EditDetailsProfileActivity.class);
                intent.putExtra("userId", userId); // Truyền userId qua Intent
                startActivity(intent);
            }
        });
        // Xử lý sự kiện bấm nút quay lại
        binding.back.setOnClickListener(v -> {
            finish();  // Kết thúc activity hiện tại và quay lại activity trước đó
        });
    }

    private void loadUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            tvUserName.setText(user.name);
                            tvPhoneNumber.setText(user.phoneNumber);

                            // Hiển thị danh sách các địa chỉ đã lưu
                            loadSavedAddresses(userId);
                        }
                    } else {
                        Toast.makeText(EditProfileCustomer.this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(EditProfileCustomer.this, "Lỗi cơ sở dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Người dùng chưa đăng nhập.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSavedAddresses(String userId) {
        DatabaseReference addressesRef = mDatabase.child(userId).child("addresses");
        addressesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> addressList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String address = snapshot.getValue(String.class);
                    if (address != null) {
                        addressList.add(address);
                    }
                }
                displayAddresses(addressList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileCustomer.this, "Không thể tải địa chỉ: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAddresses(List<String> addressList) {
        adapter = new SavedLocationsAdapter(addressList);
        recyclerViewSavedLocations.setAdapter(adapter);
    }

    private void showAddAddressDialog() {
        // Tạo dialog để thêm địa chỉ
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_address, null);
        dialogBuilder.setView(dialogView);

        EditText etNewAddress = dialogView.findViewById(R.id.etNewAddress);
        Button btnAddAddress = dialogView.findViewById(R.id.btnAddAddress);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        // Xử lý sự kiện bấm nút "Thêm địa chỉ"
        btnAddAddress.setOnClickListener(v -> {
            String newAddress = etNewAddress.getText().toString().trim();
            if (!newAddress.isEmpty()) {
                addAddressToFirebase(newAddress);
                alertDialog.dismiss();
            } else {
                Toast.makeText(EditProfileCustomer.this, "Vui lòng nhập địa chỉ.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAddressToFirebase(String newAddress) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = mDatabase.child(userId).child("addresses");

            // Thêm địa chỉ mới vào Firebase
            String key = userRef.push().getKey(); // Tạo khóa ngẫu nhiên
            userRef.child(key).setValue(newAddress).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    loadSavedAddresses(userId);
                    Toast.makeText(EditProfileCustomer.this, "Địa chỉ đã được thêm thành công.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileCustomer.this, "Thêm địa chỉ thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Người dùng chưa đăng nhập.", Toast.LENGTH_SHORT).show();
        }
    }
}
