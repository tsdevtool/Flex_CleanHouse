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
import Chat from "./Page/Chat";
import Customers from "./Page/Customers/index.jsx";
import Employees from "./Page/Employees";
import AddEmployee from "./Page/Employees/addEmployee";
import UpdateEmployee from "./Page/Employees/updateEmployee";
import Home from "./Page/Home";
import LoginForm from "./Page/Login";
import { AuthProvider, useAuth } from "./Page/Login/AuthContext";
import NotFound from "./Page/NotFound";
import Other from "./Page/Other";
import Service from "./Page/Services";
import PrivateRoute from "./PrivateRoute";
const MyContext = createContext();
function App() {
  const { currentUser } = useAuth();
  const values = {};
  return (
    <AuthProvider>
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
                <div className="flex min-h-screen h-screen w-screen">
                  <Sidebar />{" "}
                  {/* Hiển thị Sidebar nếu người dùng đã đăng nhập */}
                  <main className="flex-1 p-6 bg-gray-100 overflow-y-auto">
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
                <div className="flex  min-h-screen h-screen w-screen">
                  <Sidebar />{" "}
                  {/* Hiển thị Sidebar nếu người dùng đã đăng nhập */}
                  <main className="flex-1 p-6 bg-gray-100 overflow-y-auto">
                    <Employees /> {/* Nội dung Home */}
                  </main>
                </div>
              </PrivateRoute>
            }
          />
          <Route
            path="/employees/add"
            element={
              <PrivateRoute>
                <div className="flex  min-h-screen h-screen w-screen">
                  <Sidebar />{" "}
                  {/* Hiển thị Sidebar nếu người dùng đã đăng nhập */}
                  <main className="flex-1 p-6 bg-gray-100 overflow-y-auto">
                    <AddEmployee /> {/* Nội dung Home */}
                  </main>
                </div>
              </PrivateRoute>
            }
          />
          <Route
            path="/employees/update/:id"
            element={
              <PrivateRoute>
                <div className="flex  min-h-screen h-screen w-screen">
                  <Sidebar />{" "}
                  {/* Hiển thị Sidebar nếu người dùng đã đăng nhập */}
                  <main className="flex-1 p-6 bg-gray-100 overflow-y-auto">
                    <UpdateEmployee /> {/* Nội dung Home */}
                  </main>
                </div>
              </PrivateRoute>
            }
          />
          <Route
            path="/customers"
            element={
              <PrivateRoute>
                <div className="flex min-h-screen h-screen w-screen">
                  <Sidebar />{" "}
                  {/* Hiển thị Sidebar nếu người dùng đã đăng nhập */}
                  <main className="flex-1 p-6 bg-gray-100 overflow-y-auto">
                    <Customers /> {/* Nội dung Home */}
                  </main>
                </div>
              </PrivateRoute>
            }
          />
          <Route
            path="/chats/:id"
            element={
              <PrivateRoute>
                <div className="flex min-h-screen h-screen w-screen">
                  <Sidebar />{" "}
                  {/* Hiển thị Sidebar nếu người dùng đã đăng nhập */}
                  <main className="flex-1 p-6 bg-gray-100 overflow-y-auto">
                    <Chat /> {/* Nội dung Home */}
                  </main>
                </div>
              </PrivateRoute>
            }
          />
          <Route
            path="/services"
            element={
              <PrivateRoute>
                <div className="flex min-h-screen h-screen w-screen">
                  <Sidebar />
                  <main className="flex-1 p-6 bg-gray-100 overflow-y-auto">
                    <Service />
                  </main>
                </div>
              </PrivateRoute>
            }
          />
          <Route
            path="/other"
            element={
              <PrivateRoute>
                <div className="flex min-h-screen h-screen w-screen">
                  <Sidebar />{" "}
                  {/* Hiển thị Sidebar nếu người dùng đã đăng nhập */}
                  <main className="flex-1 p-6 bg-gray-100 overflow-y-auto">
                    <Other /> {/* Nội dung Home */}
                  </main>
                </div>
              </PrivateRoute>
            }
          />
          <Route path="*" element={<NotFound />} />{" "}
          {/* Route cho trang Not Found */}
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
