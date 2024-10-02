import "bootstrap/dist/css/bootstrap.min.css";
import { createContext } from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import "./App.css";
import Header from "./Components/Header";
import Sidebar from "./Components/Header/Sidebar";
import Chat from "./Page/Chat";
import Customers from "./Page/Customers";
import Employees from "./Page/Employees";
import Home from "./Page/Home";
import Service from "./Page/Services";

const MyContext = createContext();
function App() {
  const values = {};
  return (
    <Router>
      <div className="d-flex">
        <div className="col-2">
          <Sidebar />
        </div>
        <div className="col-10">
          <Header />
          <div className="p-4">
            <Routes>
              <Route path="/" exact component={<Home />} />
              <Route path="/nhan-vien" component={<Employees />} />
              <Route path="/khach-hang" component={<Customers />} />
              <Route path="/dich-vu" component={<Service />} />
              <Route path="/chat" component={<Chat />} />
            </Routes>
          </div>
        </div>
      </div>
    </Router>
  );
}

export default App;
