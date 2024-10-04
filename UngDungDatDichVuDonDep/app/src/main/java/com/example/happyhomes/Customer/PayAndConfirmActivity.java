package com.example.happyhomes.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happyhomes.Api.CreateOrder;
import com.example.happyhomes.DatabaseHelper;
import com.example.happyhomes.LoginActivity;
import com.example.happyhomes.Model.Payment;
import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.Model.ServiceSchedule;
import com.example.happyhomes.databinding.ActivityPayAndConfirmBinding;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class PayAndConfirmActivity extends AppCompatActivity {
    // Khởi tạo Firebase Realtime Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ActivityPayAndConfirmBinding binding;
    DatabaseHelper databaseHelper;
    private String cusId;
    private String cusName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayAndConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("jobs");
        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        cusId = intent.getStringExtra("CusId");
        cusName = intent.getStringExtra("Cusname");
        // Load data from the intent and display on the UI
        loadData();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // Handle the event when the Post Job button is pressed
        binding.btnPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveJobToDatabase();
                showSuccessDialog();
            }
        });
    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thành công");
        builder.setMessage("Công việc đã đăng thành công.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent1 = new Intent(PayAndConfirmActivity.this, Main_CustomerActivity.class);
                intent1.putExtra("Cusname", cusName);
                intent1.putExtra("CusId", cusId);
                startActivity(intent1);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        addEvent();
    }

    private void addEvent() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go back to the previous screen
                onBackPressed();
            }
        });
    }

    private void loadData() {
        // Get data from the intent and display on the TextViews
        Intent intent = getIntent();
        if (intent != null) {
            String selectedDate = intent.getStringExtra("selectedDate");
            String selectedHour = intent.getStringExtra("selectedTime");
            String additionalRequest = intent.getStringExtra("additionalRequest");
            String adress = intent.getStringExtra("adress");
            String cost = intent.getStringExtra("cost");
            binding.txtDate.setText(selectedDate);
            binding.txtTime.setText(selectedHour);
            binding.txtNote.setText(additionalRequest);
            binding.locationtext.setText(adress);
            binding.txtCost.setText(cost);
        } else {
            Toast.makeText(this, "Error: No data received.", Toast.LENGTH_LONG).show();
        }
    }
    private void saveJobToDatabase() {
        Intent intent = getIntent();
        int serviceId = intent.getIntExtra("selectedServiceId", -1);

        String note = binding.txtNote.getText().toString();
        String selectedDate = binding.txtDate.getText().toString(); // Expecting format "yyyy/MM/dd"
        String selectedHour = binding.txtTime.getText().toString(); // Expecting format "HH:mm"

        if (serviceId == -1) {
            Toast.makeText(this, "Error: Service not selected.", Toast.LENGTH_LONG).show();
            return;
        }

        // Sử dụng format ngày giờ thích hợp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        try {
            Date date = dateFormat.parse(selectedDate);
            Date startTime = timeFormat.parse(selectedHour);

            // Tạo đối tượng Schedule và lưu vào Firebase
            String scheduleId = databaseReference.push().getKey();
            Schedule schedule = new Schedule(
                    scheduleId,
                    cusId,
                    date,
                    startTime,
                    binding.locationtext.getText().toString(),
                    "Đang chờ"
            );

            // Lưu schedule vào Firebase
            databaseReference.child("schedules").child(scheduleId).setValue(schedule)
                    .addOnSuccessListener(aVoid -> {
                        // Sau khi lưu thành công Schedule, tiếp tục lưu Payment
                        savePayment(scheduleId, serviceId);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("PayAndConfirmActivity", "Error saving schedule to Firebase", e);
                        Toast.makeText(PayAndConfirmActivity.this, "Failed to save schedule.", Toast.LENGTH_LONG).show();
                    });

        } catch (ParseException e) {
            Log.e("PayAndConfirmActivity", "Error parsing date or hour", e);
            Toast.makeText(this, "Invalid date or time format.", Toast.LENGTH_LONG).show();
        }
    }

    private void savePayment(String scheduleId, int serviceId) {
        // Chuyển đổi ngày giờ thành chuỗi định dạng
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String payDay = sdf.format(timestamp);

        // Tạo đối tượng Payment dựa trên phương thức thanh toán
        Payment payment = null;
        if (binding.rdoTienMat.isChecked()) {
            payment = new Payment(null, (long) scheduleId.hashCode(), (long) 1, (long) serviceId, payDay);
        } else if (binding.rdoZalo.isChecked()) {
            payment = new Payment(null, (long) scheduleId.hashCode(), (long) 6, (long) serviceId, payDay);
            zalopay();
        }

        // Lưu Payment vào Firebase
        if (payment != null) {
            databaseReference.child("payments").child(scheduleId).setValue(payment)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(PayAndConfirmActivity.this, "Payment saved successfully!", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("PayAndConfirmActivity", "Error saving payment to Firebase", e);
                        Toast.makeText(PayAndConfirmActivity.this, "Failed to save payment.", Toast.LENGTH_LONG).show();
                    });
        }
    }
    private void zalopay() {
        CreateOrder orderApi = new CreateOrder();
        String cost2 = binding.txtCost.getText().toString();
        try {
            Log.d("PayAndConfirmActivity", "Creating order with cost: " + binding.txtCost.getText().toString());
            JSONObject data = orderApi.createOrder(cost2);
            String code = data.getString("return_code");
            Log.d("PayAndConfirmActivity", "Order creation result: " + data.toString());

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                Log.d("PayAndConfirmActivity", "Received ZaloPay token: " + token);

                ZaloPaySDK.getInstance().payOrder(PayAndConfirmActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Log.d("PayAndConfirmActivity", "Payment succeeded. ZP Trans Token: " + s + ", Info: " + s1);
                        Intent intent1 = new Intent(PayAndConfirmActivity.this, payContificationActivity.class);
                        intent1.putExtra("result", "thanh toan thanh cong");
                        intent1.putExtra("Cusname", cusName);
                        intent1.putExtra("CusId", cusId);
                        startActivity(intent1);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Log.d("PayAndConfirmActivity", "Payment canceled. ZP Trans Token: " + s + ", Info: " + s1);
                        Intent intent1 = new Intent(PayAndConfirmActivity.this, payContificationActivity.class);
                        intent1.putExtra("result", "huy thanh toan");
                        intent1.putExtra("Cusname", cusName);
                        intent1.putExtra("CusId", cusId);
                        startActivity(intent1);
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Log.e("PayAndConfirmActivity", "Payment error: " + zaloPayError.toString() + ", ZP Trans Token: " + s + ", Info: " + s1);
                        Intent intent1 = new Intent(PayAndConfirmActivity.this, payContificationActivity.class);
                        intent1.putExtra("result", "loi thanh toan");
                        intent1.putExtra("Cusname", cusName);
                        intent1.putExtra("CusId", cusId);
                        startActivity(intent1);
                    }
                });
            } else {
                Log.e("PayAndConfirmActivity", "Order creation failed with return_code: " + code);
            }
        } catch (Exception e) {
            Log.e("PayAndConfirmActivity", "Error during order creation or payment initiation", e);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("PayAndConfirmActivity", "Received new intent for ZaloPay result.");
        ZaloPaySDK.getInstance().onResult(intent);
    }
}