package com.example.happyhomes.Customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.happyhomes.Model.Message;
import com.example.happyhomes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private ImageButton imgbutSend;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private DatabaseReference messagesRef;
    private String currentUserId;
    private String adminId = "ADMIN_USER_ID"; // Thay đổi giá trị này với ID của admin

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_messages);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        imgbutSend = view.findViewById(R.id.imgbutSend);

        messageList = new ArrayList<>();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messagesRef = FirebaseDatabase.getInstance().getReference("messages");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessageAdapter(getContext(), messageList, currentUserId);  // Truyền thêm Context từ Fragment
        recyclerView.setAdapter(messageAdapter);


        // Load messages from Firebase
        loadMessages();

        // Send message
        imgbutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return view;
    }

    private void loadMessages() {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Message message = dataSnapshot.getValue(Message.class);

                if (message != null && message.getReceiverId() != null && message.getSenderId() != null) {
                    // Kiểm tra xem người gửi hoặc người nhận có phải là currentUserId không
                    if (message.getReceiverId().equals(currentUserId) || message.getSenderId().equals(currentUserId)) {
                        messageList.add(message);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString();
        if (!messageText.isEmpty()) {
            // Gửi tin nhắn từ người dùng hiện tại đến admin
            Message message = new Message(messageText, currentUserId, adminId, System.currentTimeMillis());
            messagesRef.push().setValue(message);
            editTextMessage.setText("");
        }
    }
}