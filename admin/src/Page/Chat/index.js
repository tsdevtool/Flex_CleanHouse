import { getAuth } from "firebase/auth";
import { onValue, push, ref } from "firebase/database";
import React, { useEffect, useState } from "react";
import { IoIosSend } from "react-icons/io";
import { IoList } from "react-icons/io5";
import { MdDelete } from "react-icons/md";
import { db } from "../../firebase/config";
import { useParams } from "react-router-dom"; // Import useParams

const Chat = () => {
  const { id } = useParams(); // Lấy ID từ URL
  const [messages, setMessages] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const auth = getAuth();
  const currentUser = auth.currentUser;
  const [menuOpen, setMenuOpen] = useState(false);
  const [customerIds, setCustomerIds] = useState(new Set()); // Lưu trữ ID của những người đã gửi tin nhắn

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
      const messagesRef = ref(db, `messages/${selectedCustomer.id}`);
      onValue(messagesRef, (snapshot) => {
        const data = snapshot.val();
        if (data) {
          const messageList = Object.keys(data).map((key) => ({
            id: key,
            ...data[key],
          }));
          setMessages(messageList);

          // Cập nhật customerIds với ID của người đã gửi tin nhắn
          setCustomerIds((prev) => new Set(prev).add(selectedCustomer.id));
        } else {
          setMessages([]);
        }
      });
    }
  }, [selectedCustomer]);

  useEffect(() => {
    // Kiểm tra xem ID từ URL có tồn tại trong danh sách khách hàng không
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
        setNewMessage("");
      });
    }
  };

  const toggleMenu = () => {
    setMenuOpen((prev) => !prev);
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      handleSendMessage();
    }
  };

  // Lọc danh sách khách hàng chỉ để hiển thị những người đã gửi tin nhắn
  const filteredCustomers = customers.filter((customer) =>
    customerIds.has(customer.id)
  );

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
          {filteredCustomers.map((customer) => (
            <div
              key={customer.id}
              className="flex items-center p-4 hover:bg-gray-100 cursor-pointer"
              onClick={() => setSelectedCustomer(customer)}
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
      <div className="w-2/3 flex flex-col">
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
            <div className="flex-1 overflow-y-auto p-4">
              {messages.length === 0 ? (
                <p className="text-gray-500 text-center">
                  Chưa có tin nhắn nào.
                </p>
              ) : (
                messages.map((message) => (
                  <div
                    key={message.id}
                    className={`flex items-start mb-4 ${
                      message.senderId === currentUser.uid // Tin nhắn của tài khoản đang đăng nhập
                        ? "justify-end"
                        : "justify-start" // Tin nhắn từ người dùng khác
                    }`}
                  >
                    {message.senderId === currentUser.uid ? ( // Kiểm tra nếu người gửi là tài khoản đang đăng nhập
                      <div className="bg-blue-100 p-3 rounded-lg">
                        <p>{message.content}</p>
                        <span className="text-xs text-gray-500">
                          {message.timestamp}
                        </span>
                      </div>
                    ) : (
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
                    )}
                  </div>
                ))
              )}
            </div>

            <div className="p-4 border-t border-gray-300 flex items-center">
              <input
                type="text"
                placeholder="Tin nhắn mới"
                value={newMessage}
                onKeyDown={handleKeyDown}
                onChange={(e) => setNewMessage(e.target.value)}
                className="flex-1 p-2 border border-gray-300 rounded-lg mr-4 mb-3"
              />
              <button
                onClick={handleSendMessage}
                className="flex p-2 bg-blue-500 text-white rounded-lg mb-3 p-3"
              >
                <IoIosSend className="mr-2 text-2xl" />
                Gửi
              </button>
            </div>
          </>
        ) : (
          <div className="flex-1 flex items-center justify-center">
            <p className="text-gray-500">
              Vui lòng chọn một người dùng để bắt đầu trò chuyện.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Chat;
