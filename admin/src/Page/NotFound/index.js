// src/Page/NotFound.js
import React from "react";
import { Link } from "react-router-dom";

const NotFound = () => {
  return (
    <div className="flex flex-col items-center justify-center h-screen ">
      <h1 className="text-4xl font-bold text-indigo-900">404</h1>
      <p className="mt-4 text-lg">Trang bạn đang tìm kiếm không tồn tại.</p>
      <Link to="/home" className="mt-6 text-indigo-600 hover:underline">
        Quay lại Trang Chủ
      </Link>
    </div>
  );
};

export default NotFound;
