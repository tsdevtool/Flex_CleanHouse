package com.example.happyhomes.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.happyhomes.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private String cusID;
    private FragmentProfileBinding binding; // Khai báo binding

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Load dữ liệu từ Activity
        loadActivity();

        // Thêm sự kiện khi người dùng bấm vào ảnh đại diện
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileDetail(); // Mở trang chi tiết hồ sơ
            }
        });

        return binding.getRoot(); // Trả về view từ binding
    }

    public void loadActivity() {
        // Lấy dữ liệu từ Main_CustomerActivity
        if (getActivity().getIntent() != null) {
            Intent intent = getActivity().getIntent();
            String cusName = intent.getStringExtra("Cusname");
            cusID = intent.getStringExtra("CusId");  // Lấy CusId

            if (cusName != null && cusID != null) {
                // Set tên người dùng vào TextView bằng binding
                binding.username.setText(cusName);
            } else {
                // Ghi log nếu dữ liệu bị null
                Log.e("ProfileFragment", "Cusname or CusId is null");
            }
        }
    }

    // Hàm mở chi tiết hồ sơ
    private void openProfileDetail() {
        // Tạo Intent để mở ProfileDetailCustomer
        Intent intent = new Intent(getActivity(), EditProfileCustomer.class);

        // Truyền thêm thông tin cần thiết nếu có
        intent.putExtra("CusId", cusID);
        intent.putExtra("Cusname", binding.username.getText().toString());

        // Bắt đầu ProfileDetailCustomer Activity
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Hủy bỏ binding khi view bị hủy để tránh memory leaks
    }
}
