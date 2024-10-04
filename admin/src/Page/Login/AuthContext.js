// src/context/AuthContext.js

import { onValue, ref } from "firebase/database";
import React, { createContext, useContext, useEffect, useState } from "react";
import { auth, db } from "../../firebase/config"; // Import cấu hình Firebase
const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [userName, setUserName] = useState(null);
  useEffect(() => {
    const unsubscribe = auth.onAuthStateChanged((users) => {
      setCurrentUser(users);
      if (users) {
        // Khi người dùng đăng nhập, lấy tên từ Realtime Database
        const userRef = ref(db, `users/${users.uid}`); // Tạo tham chiếu đến đường dẫn người dùng
        onValue(userRef, (snapshot) => {
          const userData = snapshot.val();
          if (userData) {
            setUserName(userData.name); // Cập nhật tên người dùng
          } else {
            setUserName(null); // Không tìm thấy tên người dùng
          }
        });
      } else {
        setUserName(null); // Đăng xuất, reset tên người dùng
      }
    });
    return () => unsubscribe();
  }, []);
  const value = {
    currentUser,
    userName,
  };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  return useContext(AuthContext);
};
