package com.example.happyhomes.Customer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhomes.Model.Message;
import com.example.happyhomes.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private DatabaseReference messagesDatabaseRef;
    private String senderId, receiverId;
    private EditText editTextMessage;
    private ImageButton imgbutSend;
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_tin);

        // Get sender and receiver ID from intent or session
        senderId = "CURRENT_USER_ID"; // Retrieve from session or FirebaseAuth
        receiverId = getIntent().getStringExtra("RECEIVER_ID"); // Passed from user selection

        // Initialize Firebase reference
        messagesDatabaseRef = FirebaseDatabase.getInstance().getReference("messages").child(generateChatId(senderId, receiverId));

        // UI elements
        editTextMessage = findViewById(R.id.editTextMessage);
        imgbutSend = findViewById(R.id.imgbutSend);
        recyclerViewMessages = findViewById(R.id.recycler_view_messages);

        // Setup RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, senderId);
        recyclerViewMessages.setAdapter(messageAdapter);

        // Load messages from Firebase
        loadMessages();

        // Send button listener
        imgbutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void loadMessages() {
        messagesDatabaseRef.addValueEventListener(new ValueEventListener() {
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
                // Handle database error
            }
        });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(messageText)) {
            long timestamp = new Date().getTime();
            Message message = new Message(messageText, senderId, receiverId, timestamp);

            // Push the message to Firebase
            messagesDatabaseRef.push().setValue(message);

            // Clear input field
            editTextMessage.setText("");
        }
    }

    private String generateChatId(String senderId, String receiverId) {
        return senderId.compareTo(receiverId) < 0 ? senderId + "_" + receiverId : receiverId + "_" + senderId;
    }
}
