package com.example.happyhomes.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhomes.Model.User;
import com.example.happyhomes.R;

import java.util.List;

public class RecipientAdapter extends RecyclerView.Adapter<RecipientAdapter.RecipientViewHolder> {

    private List<User> userList;
    private OnRecipientClickListener onRecipientClickListener;
    private Context context;

    // Giao diện callback khi người nhận được chọn
    public interface OnRecipientClickListener {
        void onRecipientClick(User user);
    }

    public RecipientAdapter(List<User> userList, OnRecipientClickListener onRecipientClickListener) {
        this.userList = userList;
        this.onRecipientClickListener = onRecipientClickListener;
    }

    @NonNull
    @Override
    public RecipientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho từng người nhận
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipient, parent, false);
        return new RecipientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipientViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user, onRecipientClickListener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class RecipientViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewRecipientName;
        private TextView textViewRecipientRole;

        public RecipientViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRecipientName = itemView.findViewById(R.id.textViewRecipientName);
            textViewRecipientRole = itemView.findViewById(R.id.textViewRecipientRole);
        }

        // Ràng buộc dữ liệu vào view và thiết lập listener
        public void bind(User user, OnRecipientClickListener onRecipientClickListener) {
            // Truy cập trực tiếp các thuộc tính public của lớp User
            textViewRecipientName.setText(user.name);  // Hiển thị tên người nhận
            textViewRecipientRole.setText(user.role);  // Hiển thị vai trò (admin hoặc nhân viên)

            // Xử lý khi người nhận được chọn
            itemView.setOnClickListener(v -> onRecipientClickListener.onRecipientClick(user));
        }

    }
}
