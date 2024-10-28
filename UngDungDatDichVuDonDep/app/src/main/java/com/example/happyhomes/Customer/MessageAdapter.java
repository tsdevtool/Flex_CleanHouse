package com.example.happyhomes.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhomes.Model.Message;
import com.example.happyhomes.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private String currentUserId;
    private LayoutInflater inflater;

    // Constructor nhận thêm Context
    public MessageAdapter(Context context, List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        // Kiểm tra nếu không có tin nhắn gửi đi hoặc nhận được
        if (viewType == 1) {
            view = inflater.inflate(R.layout.item_message_sent, parent, false);  // Layout cho tin nhắn gửi đi
        } else if (viewType == 0) {
            view = inflater.inflate(R.layout.item_message_receiver, parent, false);  // Layout cho tin nhắn nhận được
        }

        // Nếu view là null, bỏ qua việc xử lý
        if (view == null) {
            return null;  // Không làm gì nếu không có tin nhắn
        }

        // Trả về ViewHolder bình thường nếu có view hợp lệ
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageText.setText(message.getContent());  // Hiển thị nội dung tin nhắn
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        // Kiểm tra nếu currentUserId hoặc senderId của message là null
        if (currentUserId != null && message.getSenderId() != null) {
            if (currentUserId.equals(message.getSenderId())) {
                return 1; // Tin nhắn do người dùng hiện tại gửi
            } else {
                return 0; // Tin nhắn từ người khác
            }
        } else {
            // Trường hợp nếu một trong hai là null, trả về loại mặc định
            return 1; // Hoặc 1, tùy theo bạn muốn xử lý như thế nào khi gặp null
        }
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);  // Dùng chung cho cả nhận và gửi
        }
    }
}
