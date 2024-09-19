import React from "react";
import ReactDOM from "react-dom/client";
import "../node_modules/bootstrap-4-react/dist/bootstrap-4-react";
import Home from "./Home";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <Home />
  </React.StrictMode>
);
