import React from "react";
import { Routes, Route, Navigate, Link, useNavigate } from "react-router-dom";
import Home from "./pages/Home.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import StudentDashboard from "./pages/StudentDashboard.jsx";
import { clearAuth, loadAuth } from "./api.js";
import "./App.css";

function Protected({ children }) {
  const { email, password } = loadAuth();
  if (!email || !password) return <Navigate to="/login" replace />;
  return children;
}

function NavBar() {
  const nav = useNavigate();
  const { email, role } = loadAuth();
  const loggedIn = !!email;

  const logout = () => {
    clearAuth();
    nav("/");
  };

  return (
    <header className="nav">
      <div className="nav-inner">
        <div className="brand">
          <Link to="/" className="brand-link">
            JobIntel
          </Link>
        </div>

        <nav className="nav-right">
          <Link to="/" className="navlink">
            Home
          </Link>

          {!loggedIn ? (
            <>
              <Link to="/login" className="navlink">
                Login
              </Link>
              <Link to="/register" className="navlink">
                Register
              </Link>
            </>
          ) : (
            <>
              <span className="pill">{(role || "STUDENT").toUpperCase()}</span>
              <button className="btn btn-ghost" onClick={logout}>
                Logout
              </button>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}

export default function App() {
  return (
    <div className="app-shell">
      <NavBar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route
          path="/student"
          element={
            <Protected>
              <StudentDashboard />
            </Protected>
          }
        />

        {/* default */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </div>
  );
}
