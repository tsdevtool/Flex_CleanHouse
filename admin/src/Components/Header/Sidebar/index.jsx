import { getAuth } from "firebase/auth";
import { useEffect, useState } from "react";
import { AiOutlineAppstoreAdd } from "react-icons/ai";
import { FaUserFriends, FaUserTie } from "react-icons/fa";
import { FaHandsHoldingChild } from "react-icons/fa6";
import { IoMdHome } from "react-icons/io";
import { PiChatsFill } from "react-icons/pi";
import { Link, useNavigate } from "react-router-dom";
import "../../../App.css";
import Logo from "../../../assets/images/logo-rebg.png";
import { useAuth } from "../../../Page/Login/AuthContext";

const Sidebar = () => {
  const { currentUser, userName } = useAuth(); // Using the auth context
  const [userId, setUserId] = useState();
  const navigate = useNavigate();
  useEffect(() => {
    const auth = getAuth();
    const user = auth.currentUser; // Get current user
    if (user) {
      setUserId(user.uid); // Set the user ID if the user is logged in
    }
  }, []);

  const handleMessageClick = (id) => {
    navigate(`/chats/${id}`); // Điều hướng đến trang chat với ID khách hàng
  };

  return (
    <aside className="bg-indigo-900 text-white w-1/5 p-4 min-w-[200px] left-0">
      {currentUser ? ( // Ensure currentUser is defined
        <>
          <div className="flex items-center justify-center mb-8">
            <div className="w-40 h-40 bg-white rounded-full flex items-center justify-center">
              <img
                className="logo w-32 h-auto text-indigo-900"
                src={Logo}
                alt="Trang quản lý HappyHome"
              />
            </div>
          </div>
          <div className="justify-center flex items-center ">
            <span className="fullname text-center">
              {userName || "Người dùng"}
            </span>
          </div>
          <nav className="sidebar">
            <ul>
              <li className="mb-3 jus">
                <Link to="/home" className="flex items-center ml-4 p-2">
                  <IoMdHome className="mr-2" />
                  <p>Trang chủ</p>
                </Link>
              </li>
              <li className="mb-3 jus">
                <Link to="/employees" className="flex items-center ml-4 p-2">
                  <FaUserFriends className="mr-2" />
                  <p>Nhân viên</p>
                </Link>
              </li>
              <li className="mb-3 jus">
                <Link to="/customers" className="flex items-center ml-4 p-2">
                  <FaUserTie className="mr-2" />
                  <p>Khách hàng</p>
                </Link>
              </li>
              <li className="mb-3 jus">
                <Link to="/services" className="flex items-center ml-4 p-2">
                  <FaHandsHoldingChild className="mr-2" />
                  <p>Dịch vụ</p>
                </Link>
              </li>
              <li className="mb-3 jus">
                <button
                  onClick={() => handleMessageClick(userId)}
                  className="flex items-center ml-4 p-2"
                >
                  <PiChatsFill className="mr-2" />
                  <p>Chat</p>
                </button>
              </li>
              <li className="mb-3 jus">
                <Link to="/other" className="flex items-center ml-4 p-2">
                  <AiOutlineAppstoreAdd className="mr-2" />
                  <p>Khác</p>
                </Link>
              </li>
            </ul>
          </nav>
        </>
      ) : (
        <p className="text-center">Vui lòng đăng nhập để tiếp tục.</p> // Optional message for logged-out users
      )}
    </aside>
  );
};

export default Sidebar;
