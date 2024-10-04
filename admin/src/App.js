import "bootstrap/dist/css/bootstrap.min.css";
import { createContext } from "react";
import {
  Navigate,
  Route,
  BrowserRouter as Router,
  Routes,
} from "react-router-dom";
import "./App.css";
import Sidebar from "./Components/Header/Sidebar";
import Employees from "./Page/Employees";
import Home from "./Page/Home";
import LoginForm from "./Page/Login";
import { useAuth } from "./Page/Login/AuthContext";
import NotFound from "./Page/NotFound";
import PrivateRoute from "./PrivateRoute";
const MyContext = createContext();
function App() {
  const { currentUser } = useAuth();
  const values = {};
  return (
    <Router>
      {/* Không bọc LoginForm trong div hoặc main */}
      <Routes>
        <Route
          path="/"
          element={!currentUser ? <LoginForm /> : <Navigate to="/home" />}
        />
        <Route
          path="/home"
          element={
            <PrivateRoute>
              <div className="flex h-screen w-screen">
                <Sidebar /> {/* Hiển thị Sidebar nếu người dùng đã đăng nhập */}
                <main className="flex-1 p-6 bg-gray-100">
                  <Home /> {/* Nội dung Home */}
                </main>
              </div>
            </PrivateRoute>
          }
        />
        <Route
          path="/employees"
          element={
            <PrivateRoute>
              <div className="flex h-screen w-screen">
                <Sidebar /> {/* Hiển thị Sidebar nếu người dùng đã đăng nhập */}
                <main className="flex-1 p-6 bg-gray-100">
                  <Employees /> {/* Nội dung Home */}
                </main>
              </div>
            </PrivateRoute>
          }
        />
        <Route path="*" element={<NotFound />} />{" "}
        {/* Route cho trang Not Found */}
      </Routes>
    </Router>
  );
}

export default App;
