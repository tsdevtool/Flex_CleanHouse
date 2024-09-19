package com.example.happyhomes.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.happyhomes.Model.Schedule;
import com.example.happyhomes.R;

import java.util.List;

 class ScheduleAdapter extends BaseAdapter {
    private Context context;
    private List<Schedule> scheduleList;
    private LayoutInflater inflater;

    public ScheduleAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_lv_she_cus, parent, false);
        }

        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvStartTime = convertView.findViewById(R.id.tvStartTime);
        TextView tvLocation = convertView.findViewById(R.id.tvLocation);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        Schedule schedule = scheduleList.get(position);

        tvDate.setText(schedule.getDateString());
        tvStartTime.setText(schedule.getStartTimeString());
        tvLocation.setText(schedule.getLocation());
        tvStatus.setText(schedule.getStatus());

        return convertView;
    }
}
