package com.example.happyhomes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.happyhomes.Customer.Main_CustomerActivity;
import com.example.happyhomes.Model.Customer;
import com.example.happyhomes.Model.Employee;
import com.example.happyhomes.NhanVien.NhanVienActivity;
import com.example.happyhomes.databinding.ActivityLoginBinding;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    ActivityLoginBinding binding;

    public static final String DB_NAME="data_app_cleaning.db";
    public static final String DB_FOLDER="databases";
    public static final String TBL_NAME_CUS = "CUSTOMER";
    public static final String TBL_NAME_EMP = "EMPLOYEE";

    SQLiteDatabase db = null;
    ArrayList<HashMap<String, String>> customers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareDB();
        openDB();
        EventLogin();

        Register();
    }

    private void openDB() {
        db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
    }
    private void EventLogin() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checklogin = false;
                String emailLogin = binding.txtEmail.getText().toString();
                String passLogin = binding.txtPassword.getText().toString();
                //LOGIN CUSTOMER
                Cursor customers = db.rawQuery("SELECT * FROM "+ TBL_NAME_CUS, null);
                while (customers.moveToNext())
                {
                    if (customers.getString(3).equalsIgnoreCase(emailLogin) && customers.getString(5).equalsIgnoreCase(passLogin)) {
                        Toast.makeText(LoginActivity.this, "Login SUCCESS", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, Main_CustomerActivity.class);
                        intent.putExtra("Cusname", customers.getString(1));
                        intent.putExtra("phone",customers.getString(2));
                        intent.putExtra("CusId", customers.getInt(0));
                        startActivity(intent);
                        checklogin = true;
                        break;
                    }
                }
                //LOGIN EMPLOYEE
                Cursor employees = db.rawQuery("SELECT * FROM "+ TBL_NAME_EMP, null);
                while (employees.moveToNext())
                {
                    if (employees.getString(2).equalsIgnoreCase(emailLogin) && employees.getString(3).equalsIgnoreCase(passLogin)) {
                        Toast.makeText(LoginActivity.this, "Login SUCCESS", Toast.LENGTH_SHORT).show();
                        checklogin = true;

                        // Lấy thông tin nhân viên
                        Employee employee = new Employee(
                                employees.getLong(0), // emId
                                employees.getString(1), // emName
                                employees.getString(2), // emEmail
                                employees.getString(3) // password
                        );

                        // Chuyển sang NhanVienActivity
                        Intent intent = new Intent(LoginActivity.this, NhanVienActivity.class);
                        intent.putExtra("Employee", employee);
                        startActivity(intent);
                        break;
                    }
                }
                if(checklogin == false)
                {
                    Toast.makeText(LoginActivity.this, "Login FAIL", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void Register() {
        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    private void prepareDB() {
        File dbfile = getDatabasePath(DB_NAME);
        if(!dbfile.exists())
        {
            if(CopyDB())
            {
                Toast.makeText(this, "Open app SUCCESS", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Open app FAIL", Toast.LENGTH_SHORT).show();
            }
        }

    }
    //copy db vào ứng dụng
    private boolean CopyDB()
    {
        String dbPath = getApplicationInfo().dataDir +"/"+DB_FOLDER+"/"+DB_NAME;
        try {
            InputStream inputStream=getAssets().open(DB_NAME);
            File file= new File(getApplicationInfo().dataDir+"/"+DB_FOLDER+"/");
            if(!file.exists()){
                file.mkdir();
            }
            OutputStream outputStream= null;
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
            {
                outputStream = Files.newOutputStream(Paths.get(dbPath));
            }
            byte[] buffer= new byte[1024];
            int lenght;
            while ((lenght=inputStream.read(buffer))>0){
                outputStream.write(buffer,0,lenght);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
