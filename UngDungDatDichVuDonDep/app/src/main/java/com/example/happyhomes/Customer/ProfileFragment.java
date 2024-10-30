package com.example.happyhomes.Customer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private String cusID;
    private FragmentProfileBinding binding;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout bằng ViewBinding
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Khởi tạo Firebase Storage và Firebase Realtime Database
        storage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Tải dữ liệu từ Activity
        loadActivity();

        // Sự kiện khi người dùng bấm vào ảnh đại diện để tải lên ảnh mới
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();//Mở trình chọn ảnh
                //Mở trình chi tiết hồ sơ
            }
        });
        openProfileDetail();
        return binding.getRoot();
    }
    private void openProfileDetail() {
        // Tạo Intent để mở ProfileDetailCustomer
        binding.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileCustomer.class);
                // Truyền thêm thông tin cần thiết nếu có
                intent.putExtra("CusId", cusID);
                intent.putExtra("Cusname", binding.username.getText().toString());
                // Bắt đầu ProfileDetailCustomer Activity
                startActivity(intent);
            }
        });

    }
    public void loadActivity() {
        if (getActivity().getIntent() != null) {
            Intent intent = getActivity().getIntent();
            String cusName = intent.getStringExtra("Cusname");
            cusID = intent.getStringExtra("CusId");

            if (cusName != null && cusID != null) {
                binding.username.setText(cusName);

                // Lấy URL ảnh từ Firebase Realtime Database
                DatabaseReference userRef = databaseReference.child("users").child(cusID).child("image");
                userRef.get().addOnSuccessListener(dataSnapshot -> {
                    String imageUrl = dataSnapshot.getValue(String.class);

                    // Hiển thị ảnh nếu có URL
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(getActivity())
                                .load(imageUrl)
                                .placeholder(R.drawable.circle_background)
                                .into(binding.profileImage);
                    } else {
                        binding.profileImage.setImageResource(R.drawable.circle_background);
                    }
                }).addOnFailureListener(e -> {
                    Log.e("ProfileFragment", "Lỗi khi tải URL ảnh từ Firebase Realtime Database.", e);
                });
            }
        }
    }

    // Mở trình chọn ảnh
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                binding.profileImage.setImageBitmap(bitmap);

                // Tải ảnh lên Firebase Storage
                uploadImageToFirebase(imageUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Tải ảnh lên Firebase Storage và lưu URL vào Firebase Realtime Database
    private void uploadImageToFirebase(Uri imageUri) {
        // Tạo tham chiếu đến Firebase Storage với đường dẫn đúng
        StorageReference profileImageRef = storage.getReference().child("images/" + cusID + "/profile.jpg");

        // Tải lên ảnh
        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();

                                // Hiển thị ảnh từ Firebase Storage vào ImageView
                                Glide.with(getActivity())
                                        .load(imageUrl)
                                        .into(binding.profileImage);

                                // Lưu URL ảnh vào Firebase Realtime Database
                                databaseReference.child("users").child(cusID).child("image").setValue(imageUrl)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("ProfileFragment", "URL ảnh đã được lưu vào Firebase Database.");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("ProfileFragment", "Lỗi khi lưu URL ảnh vào Firebase Database.", e);
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("ProfileFragment", "Lỗi khi tải ảnh lên Firebase Storage.", exception);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
