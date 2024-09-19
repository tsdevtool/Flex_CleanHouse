package com.example.happyhomes.NhanVien;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happyhomes.DatabaseHelper;
import com.example.happyhomes.LoginActivity;
import com.example.happyhomes.Model.Check_Work;
import com.example.happyhomes.Model.Employee;
import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.R;
import com.example.happyhomes.databinding.ActivityNhanVienBinding;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NhanVienActivity extends AppCompatActivity {

    ActivityNhanVienBinding binding;
    boolean checkIn;
    Employee employee;
    Schedule selectedSchedule; // Biến lưu trữ SCHEDULE được chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNhanVienBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận dữ liệu Employee từ Intent
        Intent intent = getIntent();
        employee = (Employee) intent.getSerializableExtra("Employee");

        // Kiểm tra nếu đối tượng employee là null
        if (employee == null) {
            Log.e("NhanVienActivity", "Employee object is null!");
            finish();  // Dừng Activity nếu không có dữ liệu employee
            return;
        }

        // Hiển thị thông tin Employee
        binding.txtusername.setText(employee.getEmName());

        binding.lvLichSu.setEmptyView(findViewById(R.id.txtEmpty));

        // Khôi phục trạng thái từ SharedPreferences
        //restoreCheckStatusFromPreferences();

        addEvents();

    }

    private void addEvents() {
        // Hiển thị dialog khi nhấn vào layoutProfile
        binding.layotProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        // Xử lý sự kiện Check-in/Check-out
        binding.btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkIn && binding.btnCheckIn.getText().toString().equals("Sẵn sàng làm việc")) {
                    showReadyToWorkDialog();
                } else if (checkIn && binding.btnCheckIn.getText().toString().equals("Hoàn thành")) {
                    showConfirmDialog();
                }
            }
        });
    }

    // Hiển thị dialog sẵn sàng làm việc
    private void showReadyToWorkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NhanVienActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_ready_to_work, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView tvReadyDate = dialogView.findViewById(R.id.tvReadyDate);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnCheckIn = dialogView.findViewById(R.id.btnCheckIn);

        // Lấy thời gian hiện tại
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        tvReadyDate.setText(currentDate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Xử lý khi bấm nút Check-in
                dialog.dismiss();

                showAvailableSchedulesDialog();
            }
        });
    }

    private void showAvailableSchedulesDialog() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Schedule> schedules = dbHelper.getAvailableSchedulesByEmployeeId(employee.getEmId());

        Dialog dialog = new Dialog(NhanVienActivity.this);
        dialog.setContentView(R.layout.dialog_available_schedules);

        ListView lvSchedules = dialog.findViewById(R.id.lvSchedules);
        ScheduleAdapter adapter = new ScheduleAdapter(this, schedules, employee.getEmId(), false);
        lvSchedules.setAdapter(adapter);

        lvSchedules.setOnItemClickListener((parent, view, position, id) -> {
            selectedSchedule = schedules.get(position); // Gán giá trị cho selectedSchedule
            updateScheduleStatusToWorking(selectedSchedule.getScheduleId());
            showSelectedScheduleDetails(selectedSchedule);
            dialog.dismiss(); // Đóng dialog sau khi chọn lịch làm việc
        });

        // Xử lý nút Hủy
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(NhanVienActivity.this);
//        builder.setTitle("Chọn lịch làm việc");
//
//        String[] scheduleItems = new String[schedules.size()];
//        for (int i = 0; i < schedules.size(); i++) {
//            scheduleItems[i] = schedules.get(i).getLocation() + " - " + schedules.get(i).getDateString();
//        }
//
//        builder.setItems(scheduleItems, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                selectedSchedule = schedules.get(which); // Gán giá trị cho selectedSchedule
//                updateScheduleStatusToWorking(selectedSchedule.getScheduleId());
//                showSelectedScheduleDetails(selectedSchedule);
//            }
//        });
//
//        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builder.create().show();
    }

    private void updateScheduleStatusToWorking(Long scheduleId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.updateScheduleStatusToWorking(scheduleId);

        // Cập nhật giao diện hoặc thông báo cho người dùng
        checkIn = true;
        //saveCheckStatusToPreferences(0);
        binding.txtstatus.setText("Đang làm việc");
        binding.btnCheckIn.setText("Hoàn thành");
    }

    private void showSelectedScheduleDetails(Schedule schedule) {
        List<Schedule> selectedSchedules = new ArrayList<>();
        selectedSchedules.add(schedule);


        ScheduleAdapter adapter = new ScheduleAdapter(this, selectedSchedules, false);
        binding.lvLichSu.setAdapter(adapter);
    }

    private void showConfirmDialog() {
        if (selectedSchedule == null) {
            Log.e("NhanVienActivity", "No schedule selected");
            return; // Không thực hiện gì nếu không có lịch làm việc nào được chọn
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(NhanVienActivity.this);
        builder.setTitle("Xác nhận Hoàn thành");

        builder.setMessage("Bạn có chắc chắn muốn Hoàn thành?");

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cập nhật STATUS của SCHEDULE thành "Hoàn Thành"
                DatabaseHelper dbHelper = new DatabaseHelper(NhanVienActivity.this);
                dbHelper.updateScheduleStatusToCompleted(selectedSchedule.getScheduleId());

                // Lưu thông tin vào bảng CHECK_WORK
                saveCheckWorkData();

                // Cập nhật giao diện
                checkIn = false;
                //saveCheckStatusToPreferences(1);
                binding.txtstatus.setText("Hoàn thành");
                binding.btnCheckIn.setText("Sẵn sàng làm việc");

                clearScheduleDetails();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void saveCheckWorkData() {
        if (selectedSchedule == null) {
            Log.e("NhanVienActivity", "No schedule selected");
            return; // Không thực hiện gì nếu không có lịch làm việc nào được chọn
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Lấy thời gian hiện tại khi người dùng nhấn nút "Tạm nghỉ"
        Date currentTime = new Date();

        Check_Work checkWork = new Check_Work();
        checkWork.setWorkdateId(selectedSchedule.getScheduleId()); // Giả sử rằng WorkdateId chính là ScheduleId
        checkWork.setCheckPic(null); // Lưu giá trị null cho hình ảnh
        checkWork.setCheckType(1); // 1 cho "Check-Out", 0 cho "Check-In"
        checkWork.setTime(currentTime); // Lưu thời gian hiện tại vào bảng CHECK_WORK

        dbHelper.addCheckWork(checkWork);
    }

    private void clearScheduleDetails() {
        List<Schedule> emptyList = new ArrayList<>();
        ScheduleAdapter adapter = new ScheduleAdapter(this, emptyList, true);
        binding.lvLichSu.setAdapter(adapter);
    }

    // Lưu trạng thái vào SharedPreferences
    private void saveCheckStatusToPreferences(int checkType) {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckStatusPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CheckType", checkType); // Lưu trạng thái check-in hoặc check-out
        editor.apply();
    }

    // Khôi phục trạng thái từ SharedPreferences
    private void restoreCheckStatusFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckStatusPrefs", MODE_PRIVATE);
        int checkType = sharedPreferences.getInt("CheckType", -1);

        if (checkType == 0) { // 1 là Check-in
            checkIn = true;
            binding.txtstatus.setText("Đang làm việc");
            binding.btnCheckIn.setText("Hoàn thành");
        } else if (checkType == 1) { // 0 là Check-out
            checkIn = false;
            binding.txtstatus.setText("Hoàn thành");
            binding.btnCheckIn.setText("Sẵn sàng làm việc");
        }
    }

    // Hiển thị bottom sheet
    private void showBottomSheet() {
        Dialog dialog = new Dialog(NhanVienActivity.this);
        dialog.setContentView(R.layout.bottom_dialog);

        TextView txtEmployeeName = dialog.findViewById(R.id.txtusername);
        if (employee != null) {
            txtEmployeeName.setText(employee.getEmName());
        }

        //Lịch làm việc
        LinearLayout linearLichLamViec= dialog.findViewById(R.id.layotLich);
        linearLichLamViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NhanVienActivity.this, ScheduleActivity.class);
                intent.putExtra("EMPLOYEE_ID", employee.getEmId());
                startActivity(intent);
            }
        });
        //Lich sử làm việc
        LinearLayout linearLichSuLamViec= dialog.findViewById(R.id.layotThuNhap);
        linearLichSuLamViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NhanVienActivity.this, LichSuCongViecActivity.class);
                intent.putExtra("EMPLOYEE_ID", employee.getEmId());
                startActivity(intent);
            }
        });

        //ho tro
        LinearLayout linearHoTro = dialog.findViewById(R.id.layotHoTro);
        linearHoTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NhanVienActivity.this, HoTroActivity.class);
                startActivity(intent);
            }
        });

        //dang xat
        LinearLayout linearDangXat = dialog.findViewById(R.id.layotĐangXuat);
        linearDangXat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NhanVienActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //Ho So
        LinearLayout linearHoSo = dialog.findViewById(R.id.layoutHoSo);
        linearHoSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NhanVienActivity.this, HoSoNVActivity.class);
                intent.putExtra("EMPLOYEE_ID", employee.getEmId());
                startActivity(intent);
            }
        });

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.START | Gravity.TOP;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.x = 0;
        params.y = 0;

        dialog.getWindow().setAttributes(params);
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottonSheetAnimation;
        dialog.show();
    }

}
