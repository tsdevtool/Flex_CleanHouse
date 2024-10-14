import { signOut } from "firebase/auth";
import { ref as databaseRef, onValue } from "firebase/database"; // Import Firebase Realtime Database
import { useEffect, useRef, useState } from "react";
import { CiSearch } from "react-icons/ci";
import { FaRegClock } from "react-icons/fa6";
import { useNavigate } from "react-router-dom";
import { auth, db } from "../../../firebase/config"; // Sử dụng Firebase config
import { useAuth } from "../../../Page/Login/AuthContext";

const HeaderWrapper = () => {
  const [showMenu, setShowMenu] = useState(false);
  const [userImage, setUserImage] = useState(null); // Trạng thái để lưu hình ảnh của người dùng
  const menuRef = useRef(null);
  const { currentUser } = useAuth(); // Lấy thông tin người dùng hiện tại
  const navigate = useNavigate();
  const now = new Date();
  const time = now.toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true,
  });
  const date = now.toLocaleDateString();
  const handleAvatarClick = () => {
    setShowMenu(!showMenu);
  };

  // Lấy thông tin người dùng từ Realtime Database
  useEffect(() => {
    if (currentUser && currentUser.uid) {
      const userRef = databaseRef(db, `users/${currentUser.uid}`);
      onValue(userRef, (snapshot) => {
        const userData = snapshot.val();
        if (userData && userData.image) {
          setUserImage(userData.image); // Cập nhật hình ảnh từ dữ liệu Firebase
        } else {
          setUserImage("https://cdn-icons-png.flaticon.com/512/219/219986.png"); // Sử dụng hình ảnh mặc định nếu không có ảnh trong DB
        }
      });
    }
  }, [currentUser]);

  useEffect(() => {
    const handleClickOutSide = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setShowMenu(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutSide);
    return () => {
      document.removeEventListener("mousedown", handleClickOutSide);
    };
  }, []);

  const handleLogout = async () => {
    try {
      await signOut(auth);
      navigate("/");
    } catch (error) {
      console.error(error);
      alert("Đăng xuất thất bại. Có lẽ có một lỗi nào đó!");
    }
  };

  return (
    <div className="headerWrapper flex justify-between items-center mb-3 flex-wrap">
      <div className="search p-2 border rounded w-full md:w-3/4 mb-2 md:mb-0">
        <CiSearch />
        <input type="text" placeholder="Tìm file" />
      </div>
      <div className="flex items-center">
        <div className="timeNow ">
          <div className="text-right mr-4">
            <p>{time}</p>
            <p>{date}</p>
          </div>
          <FaRegClock />
        </div>
        <div className="relative">
          <div
            className="user w-12 h-12 bg-gray-300 rounded-full flex items-center justify-center overflow-hidden cursor-pointer"
            onClick={handleAvatarClick}
          >
            {/* Hiển thị hình ảnh người dùng */}
            <img
              className="w-full h-full object-cover"
              src={userImage} // Hiển thị ảnh từ state userImage
              alt="User avatar"
            />
          </div>

          {/* Menu thả xuống */}
          {showMenu && (
            <div
              ref={menuRef}
              className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg z-20"
              style={{ top: "35px" }} // Điều chỉnh khoảng cách với avatar
            >
              <ul className="py-2">
                <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer">
                  Thông tin tài khoản
                </li>
                <li
                  className="px-4 py-2 hover:bg-gray-200 cursor-pointer"
                  onClick={handleLogout}
                >
                  Đăng xuất
                </li>
              </ul>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default HeaderWrapper;
