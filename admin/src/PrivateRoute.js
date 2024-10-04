import React, { useContext } from "react";
import { Navigate } from "react-router-dom";
import { MyContext } from "./App";

const PrivateRoute = ({ element }) => {
  const { isLoggedIn } = useContext(MyContext);

  return isLoggedIn ? element : <Navigate to="/" />;
};

export default PrivateRoute;
