package com.example.happyhomes.Customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhomes.Model.Message;
import com.example.happyhomes.Model.User;
import com.example.happyhomes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private RecyclerView recyclerViewMessages;
    private Spinner spinnerUsers;
    private EditText editTextMessage;
    private ImageButton imgbutSend;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private DatabaseReference messagesRef, usersRef;
    private String currentUserId;
    private String selectedUserId;  // Người nhận được chọn
    private List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        spinnerUsers = view.findViewById(R.id.spinner_users);
        recyclerViewMessages = view.findViewById(R.id.recycler_view_messages);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        imgbutSend = view.findViewById(R.id.imgbutSend);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        messageList = new ArrayList<>();
        userList = new ArrayList<>();

        // Setup RecyclerView cho danh sách tin nhắn
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessageAdapter(getContext(), messageList, currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);

        // Tải danh sách người nhận vào Spinner
        loadUsers();

        // Gửi tin nhắn khi bấm nút
        imgbutSend.setOnClickListener(v -> sendMessage());

        return view;
    }

    // Hàm tải danh sách người dùng (admin và nhân viên)
    private void loadUsers() {
        usersRef.orderByChild("role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                List<String> userNames = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && ("admin".equals(user.role) || "employee".equals(user.role))) {
                        userList.add(user);
                        userNames.add(user.name);  // Lấy tên để hiển thị trong Spinner
                    }
                }

                // Tạo ArrayAdapter cho Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, userNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUsers.setAdapter(adapter);

                // Lắng nghe sự kiện khi người dùng chọn người nhận
                spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedUserId = userList.get(position).userId;  // Lấy ID người nhận được chọn
                        loadMessages();  // Tải tin nhắn giữa người dùng hiện tại và người nhận được chọn
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Không làm gì nếu không có người nhận được chọn
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    // Hàm tải tin nhắn giữa currentUserId và selectedUserId
    private void loadMessages() {
        if (selectedUserId == null) return;

        messagesRef.child(generateChatId(currentUserId, selectedUserId))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        messageList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);
                            if (message != null) {
                                messageList.add(message);
                            }
                        }
                        messageAdapter.notifyDataSetChanged();
                        recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi
                    }
                });
    }

    // Hàm gửi tin nhắn
    private void sendMessage() {
        String messageText = editTextMessage.getText().toString();
        if (!messageText.isEmpty() && selectedUserId != null) {
            Message message = new Message(messageText, currentUserId, selectedUserId, System.currentTimeMillis());
            messagesRef.child(generateChatId(currentUserId, selectedUserId)).push().setValue(message);
            editTextMessage.setText("");  // Xóa trường nhập sau khi gửi
        }
    }

    private String generateChatId(String senderId, String receiverId) {
        return senderId.compareTo(receiverId) < 0 ? senderId + "_" + receiverId : receiverId + "_" + senderId;
    }
}
