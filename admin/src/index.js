import React from "react";
import ReactDOM from "react-dom";
import "../node_modules/bootstrap-4-react/dist/bootstrap-4-react";
import App from "./App";
import { AuthProvider } from "./Page/Login/AuthContext";

// const root = ReactDOM.createRoot(document.getElementById("root"));
// root.render(
//   <React.StrictMode>
//     <App />
//   </React.StrictMode>
// );

ReactDOM.render(
  <AuthProvider>
    <App />
  </AuthProvider>,
  document.getElementById("root")
);
