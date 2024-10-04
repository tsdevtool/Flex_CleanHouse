package com.example.happyhomes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.happyhomes.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    public static final String DB_NAME="data_app_cleaning.db";
    public static final String TBL_NAME = "CUSTOMER";

    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        login();
        openDB();
        eventRegister();

        handlePasswordVisibility();
    }

    private void openDB() {
        db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
    }
    private void eventRegister() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEmail.getText().toString();
                String name = binding.txtFullName.getText().toString();
                String pass = binding.txtPassword.getText().toString();

                /* Cursor c = db.rawQuery("INSERT INTO "+ TBL_NAME+" (EMAIL, CUSNAME, PASSWORD) VALUES ( " + email +","+name+","+pass+")", null);*/
                String sql = "INSERT INTO " + TBL_NAME + " (EMAIL, CUSNAME, PASSWORD) VALUES ('" + email + "','" + name + "','" + pass + "')";

                try {
                    db.execSQL(sql);
                    gotoLogin();
                    Toast.makeText(RegisterActivity.this, "Registration Successful.\n You can login now!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void gotoLogin()
    {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void login() {
        binding.txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLogin();
            }
        });
    }
    private void handlePasswordVisibility() {
        // Mặc định mật khẩu sẽ bị ẩn
        binding.togglePasswordVisibility.setTag(false);

        // Bắt sự kiện khi người dùng nhấn vào con mắt
        binding.togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPasswordVisible = (boolean) v.getTag();
                if (isPasswordVisible) {
                    // Nếu mật khẩu đang hiển thị, thì ẩn đi và đổi icon
                    binding.txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.togglePasswordVisibility.setImageResource(R.drawable.eye); // Icon con mắt đóng
                } else {
                    // Nếu mật khẩu đang ẩn, thì hiển thị và đổi icon
                    binding.txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.togglePasswordVisibility.setImageResource(R.drawable.view); // Icon con mắt mở
                }
                // Cập nhật tag để xác định trạng thái mới
                v.setTag(!isPasswordVisible);

                // Đặt con trỏ lại cuối văn bản sau khi thay đổi kiểu nhập
                binding.txtPassword.setSelection(binding.txtPassword.getText().length());
            }
        });
    }
}