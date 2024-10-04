import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "./Page/Login/AuthContext";
// Import useAuth để kiểm tra trạng thái đăng nhập

const PrivateRoute = ({ children }) => {
  const { currentUser } = useAuth(); // Lấy thông tin người dùng từ context

  // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
  return currentUser ? children : <Navigate to="/" />;
};

export default PrivateRoute;
