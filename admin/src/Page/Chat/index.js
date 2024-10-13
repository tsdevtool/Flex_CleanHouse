import React, { useEffect, useState } from "react";
import { onValue, ref, push } from "firebase/database"; // Import các hàm cần thiết từ Firebase
import { db } from "../../firebase/config"; // Đảm bảo bạn đã cấu hình Firebase

const Chat = ({ customerId }) => {
  const [messages, setMessages] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [newMessage, setNewMessage] = useState("");

  useEffect(() => {
    // Lấy danh sách người dùng đã nhắn tin
    const customersRef = ref(db, "users");
    onValue(customersRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        const customerList = Object.keys(data).map((key) => ({
          id: key,
          ...data[key],
        }));
        setCustomers(customerList); // Lưu danh sách khách hàng vào state
      }
    });

    // Lấy tin nhắn với customerId
    const messagesRef = ref(db, `messages/${customerId}`);
    onValue(messagesRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        const messageList = Object.keys(data).map((key) => ({
          id: key,
          ...data[key],
        }));
        setMessages(messageList); // Lưu danh sách tin nhắn vào state
      }
    });
  }, [customerId]);

  const handleSendMessage = () => {
    if (newMessage.trim()) {
      // Gửi tin nhắn mới
      const messageData = {
        senderId: "currentUserId", // Thay thế bằng ID của người gửi
        content: newMessage,
        timestamp: new Date().toISOString(), // Thời gian gửi tin nhắn
      };

      // Thêm tin nhắn vào Realtime Database
      const messagesRef = ref(db, `messages/${customerId}`);
      // Thêm tin nhắn mới vào cơ sở dữ liệu
      push(messagesRef, messageData).then(() => {
        setNewMessage(""); // Xóa ô nhập sau khi gửi tin nhắn
      });
    }
  };

  return (
    <div className="flex h-screen">
      <div className="w-1/3 border-r border-gray-300">
        <div className="flex items-center justify-between p-4 border-b border-gray-300">
          <h2 className="text-lg font-semibold">Người dùng đã nhắn tin</h2>
          <button className="p-2 rounded-full hover:bg-gray-200">
            <i className="fas fa-pen"></i>
          </button>
        </div>
        <div className="overflow-y-auto h-full">
          {customers.map((customer) => (
            <div
              key={customer.id}
              className="flex items-center p-4 hover:bg-gray-100 cursor-pointer"
            >
              <img
                src="https://placehold.co/50x50"
                alt="User avatar"
                className="w-12 h-12 rounded-full mr-4"
              />
              <div className="flex-1">
                <h3 className="text-sm font-semibold">{customer.name}</h3>
                <p className="text-xs text-gray-500 truncate">
                  Xin chào mình có vấn đề liên hệ....
                </p>
              </div>
              <span className="text-xs text-gray-500">T5</span>
            </div>
          ))}
        </div>
      </div>
      <div className="w-2/3 flex flex-col">
        <div className="flex items-center justify-between p-4 border-b border-gray-300">
          <div className="flex items-center">
            <img
              src="https://placehold.co/50x50"
              alt="User avatar"
              className="w-12 h-12 rounded-full mr-4"
            />
            <h2 className="text-lg font-semibold">Khách hàng: {customerId}</h2>
          </div>
          <div className="relative">
            <button className="p-2 rounded-full hover:bg-gray-200">
              <i className="fas fa-ellipsis-h"></i>
            </button>
            <div className="absolute right-0 mt-2 w-48 bg-white border border-gray-300 rounded shadow-lg">
              <button className="flex items-center p-2 w-full hover:bg-gray-100">
                <i className="fas fa-trash-alt mr-2"></i> Xoá trò chuyện
              </button>
            </div>
          </div>
        </div>
        <div className="flex-1 overflow-y-auto p-4">
          {messages.map((message) => (
            <div
              key={message.id}
              className={`flex items-start mb-4 ${
                message.senderId === "currentUserId" ? "justify-end" : ""
              }`}
            >
              {message.senderId === "currentUserId" ? ( // Thay thế bằng ID của người gửi
                <div className="bg-blue-100 p-3 rounded-lg">
                  <p>{message.content}</p>
                  <span className="text-xs text-gray-500">
                    {message.timestamp}
                  </span>
                </div>
              ) : (
                <>
                  <img
                    src="https://placehold.co/50x50"
                    alt="User avatar"
                    className="w-10 h-10 rounded-full mr-4"
                  />
                  <div className="bg-gray-200 p-3 rounded-lg">
                    <p>{message.content}</p>
                    <span className="text-xs text-gray-500">
                      {message.timestamp}
                    </span>
                  </div>
                </>
              )}
            </div>
          ))}
        </div>
        <div className="p-4 border-t border-gray-300 flex items-center">
          <input
            type="text"
            placeholder="Tin nhắn mới"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)} // Cập nhật giá trị ô nhập
            className="flex-1 p-2 border border-gray-300 rounded-lg mr-4"
          />
          <button
            onClick={handleSendMessage}
            className="p-2 bg-blue-500 text-white rounded-lg"
          >
            Gửi
          </button>
        </div>
      </div>
    </div>
  );
};

export default Chat;
