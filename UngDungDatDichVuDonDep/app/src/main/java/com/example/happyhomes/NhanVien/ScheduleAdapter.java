package com.example.happyhomes.NhanVien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.Model.Workdate;
import com.example.happyhomes.R;

import java.util.List;
public class ScheduleAdapter extends BaseAdapter {
    private Context context;
    private List<Schedule> scheduleList;
    private LayoutInflater inflater;
    private String priceSer;
    private String statusWork;

    private List<Workdate> workdate;
    // Tạo interface
    public interface OnScheduleClickListener {
        void onScheduleClick(Schedule schedule);
    }

    private OnScheduleClickListener scheduleClickListener;

    // Hàm khởi tạo mới, nhận thêm interface làm tham số
    public ScheduleAdapter(Context context, List<Schedule> scheduleList, String priceSer, List<Workdate> workdate , OnScheduleClickListener listener) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.inflater = LayoutInflater.from(context);
        this.priceSer = priceSer;
        this.scheduleClickListener = listener;
        this.workdate = workdate;
    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_service_employee, parent, false);
        }

        TextView tvSubtitle = convertView.findViewById(R.id.tvSubtitle);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvTime = convertView.findViewById(R.id.tvTime);
        TextView tvLocation = convertView.findViewById(R.id.tvAddress);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        TextView tvAction = convertView.findViewById(R.id.tvAction);



        Schedule schedule = scheduleList.get(position);

        tvSubtitle.setText(schedule.getStatus());
        tvDate.setText(schedule.getDateString());
        tvTime.setText(schedule.getStartTimeString());
        tvLocation.setText(schedule.getLocation());

        if(workdate != null)
        {

            TextView tvStatusW = convertView.findViewById(R.id.tvAction);
            for(Workdate workdate1 : workdate)
            {
                if(workdate1.getSerScheId().equals(schedule.getId()))
                    tvStatusW.setText(workdate1.getStatus());
            }

        }

        // Cắt chuỗi giá nếu cần
        if (priceSer.contains(".")) {
            priceSer = priceSer.substring(0, priceSer.indexOf('.'));
        }
        tvPrice.setText(priceSer + "đ");


        tvAction.setOnClickListener(v -> {
            if (scheduleClickListener != null) {
                scheduleClickListener.onScheduleClick(schedule);
            }
        });

        return convertView;
    }
}
