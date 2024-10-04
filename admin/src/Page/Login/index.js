import { signInWithEmailAndPassword } from "firebase/auth";
import React, { useEffect, useState } from "react";
import { MdEmail } from "react-icons/md";
import { TiLockClosed } from "react-icons/ti";
import { useNavigate } from "react-router-dom";
import Logo from "../../assets/images/logo-rebg.png";
import { auth } from "../../firebase/config"; // Import Firebase auth
import { useAuth } from "./AuthContext";
import "./LoginForm.css";

const LoginForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { currentUser } = useAuth(); // Lấy thông tin người dùng từ context
  const navigate = useNavigate();
  useEffect(() => {
    if (currentUser) {
      navigate("/home");
    }
  }, [currentUser, navigate]);
  const handleLogin = async () => {
    try {
      await signInWithEmailAndPassword(auth, email, password);
      // Chuyển hướng tới trang chủ
      // window.location.href = "/home"; // Bạn có thể thay đổi đường dẫn đến trang chính của bạn
    } catch (error) {
      console.error("Error signing in:", error);
      alert("Đăng nhập không thành công. Vui lòng kiểm tra lại thông tin.");
    }
  };
  return (
    <div className="khung-ngoai">
      <div className="khung-trai d-flex justify-content-center align-items-center">
        <img src={Logo} alt="Logo" className="logo" />
        {/* Thêm logo vào khung trái */}
      </div>
      <div className="khung-phai">
        <h2>WELCOME</h2>
        <div>
          <div className="form-group">
            <label>Email</label>
            <div className="input-group">
              <span className="input-group-text">
                <MdEmail />
              </span>
              <div className="divider"></div>
              <input
                className="form-control"
                placeholder="Nhập email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
          </div>
          <div className="form-group">
            <label>Mật khẩu</label>
            <div className="input-group">
              <span className="input-group-text">
                <TiLockClosed />
              </span>
              <div className="divider"></div>
              <input
                className="form-control"
                placeholder="Nhập mật khẩu"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>
          <button onClick={handleLogin} className="btn btn-primary">
            Đăng nhập
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginForm;
