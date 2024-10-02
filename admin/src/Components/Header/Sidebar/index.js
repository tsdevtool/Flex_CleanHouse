import { Link } from "react-router-dom";
import Logo from "../../../assets/images/logo-rebg.png";
const Sidebar = () => {
  return (
    <div className="bg-dark text-white p-3 vh-100">
      <div className="text-center mb-4">
        <img src={Logo} alt="Logo" style={{ width: "50px", height: "50px" }} />
      </div>
      <h4 className="text-center mb-4">NAME FULLNAME</h4>
      <ul className="list-unstyled">
        <li className="mb-3">
          <Link to="/" className="text-white">
            Trang chủ
          </Link>
        </li>
        <li className="mb-3">
          <Link to="/nhan-vien" className="text-white">
            Nhân viên
          </Link>
        </li>
        <li className="mb-3">
          <Link to="/khach-hang" className="text-white">
            Khách hàng
          </Link>
        </li>
        <li className="mb-3">
          <a href="#" className="text-white">
            Dịch vụ
          </a>
        </li>
        <li className="mb-3">
          <a href="#" className="text-white">
            Chat
          </a>
        </li>
        <li className="mb-3">
          <a href="#" className="text-white">
            Khác
          </a>
        </li>
      </ul>
    </div>
  );
};

export default Sidebar;
