package com.example.happyhomes.NhanVien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.happyhomes.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private List<Date> dateList;
    private OnDateClickListener onDateClickListener;
    private int selectedPosition = -1;  // Lưu vị trí của item được chọn

    public DateAdapter(List<Date> dateList, OnDateClickListener onDateClickListener) {
        this.dateList = dateList;
        this.onDateClickListener = onDateClickListener;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        Date date = dateList.get(holder.getAdapterPosition()); // Sử dụng getAdapterPosition()
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        holder.txtDate.setText(dateFormat.format(date));

        // Kiểm tra xem item này có đang được chọn không
        if (selectedPosition == holder.getAdapterPosition()) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.orange));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition(); // Sử dụng getAdapterPosition()
            notifyDataSetChanged(); // Thông báo cho adapter để cập nhật giao diện

            if (onDateClickListener != null) {
                onDateClickListener.onDateClick(date);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;

        public DateViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }

    public interface OnDateClickListener {
        void onDateClick(Date date);
    }
}
