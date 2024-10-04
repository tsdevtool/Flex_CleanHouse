import "bootstrap/dist/css/bootstrap.min.css";
import { createContext } from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import "./App.css";
import Home from "./Page/Home";
import LoginForm from "./Page/Login";
const MyContext = createContext();
function App() {
  const values = {};
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginForm />} />
        <Route path="/home" element={<Home />} />
      </Routes>
    </Router>
  );
}

export default App;
