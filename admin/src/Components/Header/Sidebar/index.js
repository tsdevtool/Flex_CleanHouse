import { AiOutlineAppstoreAdd } from "react-icons/ai";
import { FaUserFriends, FaUserTie } from "react-icons/fa";
import { FaHandsHoldingChild } from "react-icons/fa6";
import { IoMdHome } from "react-icons/io";
import { PiChatsFill } from "react-icons/pi";
import { Link } from "react-router-dom";
import "../../../App.css";
import Logo from "../../../assets/images/logo-rebg.png";
import { useAuth } from "../../../Page/Login/AuthContext";
const Sidebar = () => {
  const { currentUser } = useAuth();
  return (
    <aside className="bg-indigo-900 text-white w-1/5 p-4 min-w-[200px] left-0">
      {currentUser && (
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
            <span className="fullname text-center">Siêu Nguyễn Thanh</span>
          </div>
          <nav className="sidebar">
            <ul>
              <li className="mb-3 jus">
                <Link to="/home" className="flex items-center ml-4 p-2">
                  <i className="fas fa-home mr-2">
                    <IoMdHome />
                  </i>
                  <p>Trang chủ</p>
                </Link>
              </li>
              <li className="mb-3 jus">
                <Link to="/employees" className="flex items-center ml-4 p-2">
                  <i className="fas fa-home mr-2">
                    <FaUserFriends />
                  </i>
                  <p>Nhân viên</p>
                </Link>
              </li>
              <li className="mb-3 jus">
                <Link to="/home" className="flex items-center ml-4 p-2">
                  <i className="fas fa-home mr-2">
                    <FaUserTie />
                  </i>
                  <p>Khách hàng</p>
                </Link>
              </li>
              <li className="mb-3 jus">
                <Link to="/home" className="flex items-center ml-4 p-2">
                  <i className="fas fa-home mr-2">
                    <FaHandsHoldingChild />
                  </i>
                  <p>Dịch vụ</p>
                </Link>
              </li>
              <li className="mb-3 jus">
                <Link to="/home" className="flex items-center ml-4 p-2">
                  <i className="fas fa-home mr-2">
                    <PiChatsFill />
                  </i>
                  <p>Chat</p>
                </Link>
              </li>
              <li className="mb-3 jus">
                <Link to="/home" className="flex items-center ml-4 p-2">
                  <i className="fas fa-home mr-2">
                    <AiOutlineAppstoreAdd />
                  </i>
                  <p>Khác</p>
                </Link>
              </li>
            </ul>
          </nav>
        </>
      )}
    </aside>
  );
};

export default Sidebar;
