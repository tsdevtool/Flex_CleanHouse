import { getAuth } from "firebase/auth";
import { onValue, push, ref } from "firebase/database";
import React, { useEffect, useRef, useState } from "react";
import { IoIosSend } from "react-icons/io";
import { IoList } from "react-icons/io5";
import { MdDelete } from "react-icons/md";
import { useParams } from "react-router-dom"; // Import useParams
import { db } from "../../firebase/config";

const Chat = () => {
  const { id } = useParams(); // Lấy ID từ URL
  const [messages, setMessages] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const auth = getAuth();
  const currentUser = auth.currentUser;
  const [menuOpen, setMenuOpen] = useState(false);
  const messagesEndRef = useRef(null); // Tạo ref cho phần tử cuối cùng của danh sách tin nhắn

  useEffect(() => {
    const customersRef = ref(db, "users");
    onValue(customersRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        const customerList = Object.keys(data).map((key) => ({
          id: key,
          ...data[key],
        }));
        setCustomers(customerList);
      }
    });
  }, []);

  useEffect(() => {
    if (selectedCustomer) {
      const messagesRef = ref(db, `messages`);
      onValue(messagesRef, (snapshot) => {
        const data = snapshot.val();
        if (data) {
          const messageList = Object.keys(data)
            .flatMap((userId) =>
              Object.keys(data[userId]).map((msgId) => ({
                id: msgId,
                ...data[userId][msgId],
                userId: userId,
              }))
            )
            .filter(
              (message) =>
                (message.senderId === selectedCustomer.id &&
                  message.userId === currentUser.uid) ||
                (message.senderId === currentUser.uid &&
                  message.userId === selectedCustomer.id)
            );

          setMessages(messageList);
        } else {
          setMessages([]);
        }
      });
    }
  }, [selectedCustomer]);

  useEffect(() => {
    const foundCustomer = customers.find((customer) => customer.id === id);
    if (foundCustomer) {
      setSelectedCustomer(foundCustomer);
    }
  }, [id, customers]);

  const handleSendMessage = () => {
    if (newMessage.trim() && selectedCustomer) {
      const messageData = {
        senderId: currentUser.uid,
        content: newMessage,
        timestamp: new Date().toISOString(),
      };

      const messagesRef = ref(db, `messages/${selectedCustomer.id}`);
      push(messagesRef, messageData).then(() => {
        setMessages((prevMessages) => [
          ...prevMessages,
          { ...messageData, id: `${new Date().getTime()}` },
        ]);
        setNewMessage(""); // Reset input message
      });
    }
  };

  const handleCustomerClick = (customer) => {
    setSelectedCustomer(customer);
  };

  const toggleMenu = () => {
    setMenuOpen((prev) => !prev);
  };

  // Hàm xử lý sự kiện nhấn phím trong trường nhập liệu
  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      event.preventDefault(); // Ngăn chặn hành động mặc định
      handleSendMessage(); // Gửi tin nhắn
    }
  };

  // Cuộn xuống mỗi khi tin nhắn mới được thêm
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="flex h-full">
      <div className="w-1/3 border-r border-gray-300">
        <div className="flex items-center justify-between p-4 border-b border-gray-300">
          <h2 className="text-lg font-semibold">Người dùng đã nhắn tin</h2>
          <button className="p-2 rounded-full hover:bg-gray-200">
            <i className="fas fa-pen"></i>
          </button>
        </div>
        <div className="overflow-y-auto h-full">
          {customers
            .filter((customer) => customer.id !== currentUser.uid) // Lọc người dùng đang đăng nhập ra khỏi danh sách
            .map((customer) => (
              <div
                key={customer.id}
                className="flex items-center p-4 hover:bg-gray-100 cursor-pointer"
                onClick={() => handleCustomerClick(customer)}
              >
                <img
                  src={
                    customer.image ||
                    "https://w7.pngwing.com/pngs/178/595/png-transparent-user-profile-computer-icons-login-user-avatars-thumbnail.png"
                  }
                  alt="User avatar"
                  className="w-12 h-12 rounded-full mr-4"
                />
                <div className="flex-1">
                  <h3 className="text-sm font-semibold">{customer.name}</h3>
                  <p className="text-xs text-gray-500 truncate">
                    {customer.email}
                  </p>
                </div>
                <span className="text-xs text-gray-500">{customer.role}</span>
              </div>
            ))}
        </div>
      </div>
      <div className="w-2/3 flex flex-col h-screen">
        {selectedCustomer ? (
          <>
            <div className="flex items-center justify-between p-3.5 border-b border-gray-300">
              <div className="flex items-center">
                <img
                  src={
                    selectedCustomer.image ||
                    "https://w7.pngwing.com/pngs/178/595/png-transparent-user-profile-computer-icons-login-user-avatars-thumbnail.png"
                  }
                  alt="User avatar"
                  className="w-12 h-12 rounded-full mr-4"
                />
                <h2 className="text-lg font-semibold">
                  {selectedCustomer.name}
                </h2>
              </div>
              <div className="relative">
                <button
                  className="p-2 rounded-full hover:bg-gray-200"
                  onClick={toggleMenu}
                >
                  <IoList />
                </button>
                {menuOpen && (
                  <div className="absolute right-0 mt-2 w-48 bg-white border border-gray-300 rounded shadow-lg">
                    <button className="flex items-center p-2 w-full hover:bg-gray-100">
                      <MdDelete className="mr-2" />
                      Xoá trò chuyện
                    </button>
                  </div>
                )}
              </div>
            </div>
            <div className="flex-1 overflow-y-auto p-4 h-[calc(100vh-200px)]">
              {" "}
              {/* Cập nhật chiều cao */}
              {messages.length === 0 ? (
                <p className="text-gray-500 text-center">
                  Chưa có tin nhắn nào.
                </p>
              ) : (
                messages
                  .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp))
                  .map((message) => (
                    <div
                      key={message.id}
                      className={`flex items-start mb-4 ${
                        message.senderId === selectedCustomer.id
                          ? "justify-start"
                          : "justify-end"
                      }`}
                    >
                      {message.senderId === selectedCustomer.id ? (
                        <>
                          <img
                            src={
                              selectedCustomer.image ||
                              "https://w7.pngwing.com/pngs/178/595/png-transparent-user-profile-computer-icons-login-user-avatars-thumbnail.png"
                            }
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
                      ) : (
                        <div className="bg-blue-100 p-3 rounded-lg">
                          <p>{message.content}</p>
                          <span className="text-xs text-gray-500">
                            {message.timestamp}
                          </span>
                        </div>
                      )}
                    </div>
                  ))
              )}
              {/* Phần tử ref để cuộn đến cuối */}
              <div ref={messagesEndRef} />
            </div>

            <div className="p-4 border-t border-gray-300 flex items-center">
              <input
                type="text"
                placeholder="Tin nhắn mới"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                onKeyPress={handleKeyPress} // Thêm sự kiện nhấn phím
                className="border rounded-full flex-1 py-2 px-4 mr-2"
              />
              <button
                onClick={handleSendMessage}
                className="p-2 rounded-full bg-blue-500 text-white hover:bg-blue-600"
              >
                <IoIosSend />
              </button>
            </div>
          </>
        ) : (
          <div className="flex-1 flex items-center justify-center">
            <p className="text-gray-500 text-lg">
              Chọn người dùng để bắt đầu trò chuyện.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Chat;
