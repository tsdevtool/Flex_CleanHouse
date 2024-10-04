import { AiOutlineAppstoreAdd } from "react-icons/ai";
import { FaUserFriends, FaUserTie } from "react-icons/fa";
import { FaHandsHoldingChild } from "react-icons/fa6";
import { IoMdHome } from "react-icons/io";
import { PiChatsFill } from "react-icons/pi";
import "../../../App.css";
import Logo from "../../../assets/images/logo-rebg.png";

const Sidebar = () => {
  return (
    <>
      <aside className="bg-indigo-900 text-white w-1/5 p-4 min-w-[200px] left-0">
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
              <a href="/home" className="flex items-center ml-4 p-2">
                <i className="fas fa-home mr-2">
                  <IoMdHome />
                </i>
                <p>Trang chủ</p>
              </a>
            </li>
            <li className="mb-3 jus">
              <a href="/employee" className="flex items-center ml-4 p-2">
                <i className="fas fa-home mr-2">
                  <FaUserFriends />
                </i>
                <p>Nhân viên</p>
              </a>
            </li>
            <li className="mb-3 jus">
              <a href="/home" className="flex items-center ml-4 p-2">
                <i className="fas fa-home mr-2">
                  <FaUserTie />
                </i>
                <p>Khách hàng</p>
              </a>
            </li>
            <li className="mb-3 jus">
              <a href="/home" className="flex items-center ml-4 p-2">
                <i className="fas fa-home mr-2">
                  <FaHandsHoldingChild />
                </i>
                <p>Dịch vụ</p>
              </a>
            </li>
            <li className="mb-3 jus">
              <a href="/home" className="flex items-center ml-4 p-2">
                <i className="fas fa-home mr-2">
                  <PiChatsFill />
                </i>
                <p>Chat</p>
              </a>
            </li>
            <li className="mb-3 jus">
              <a href="/home" className="flex items-center ml-4 p-2">
                <i className="fas fa-home mr-2">
                  <AiOutlineAppstoreAdd />
                </i>
                <p>Khác</p>
              </a>
            </li>
          </ul>
        </nav>
      </aside>
    </>
  );
};

export default Sidebar;
