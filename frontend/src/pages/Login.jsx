import React, { useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { apiGet, saveAuth } from "../api.js";

const DEMO_USERS = [
  { email: "studentdemo@test.com", password: "password", role: "STUDENT" },
  { email: "recruiterdemo@test.com", password: "password", role: "RECRUITER" },
  { email: "admindemo@test.com", password: "password", role: "ADMIN" },
];

export default function Login() {
  const nav = useNavigate();

  const [email, setEmail] = useState("studentdemo@test.com");
  const [password, setPassword] = useState("password");
  const [error, setError] = useState("");
  const [showDemos, setShowDemos] = useState(true);
  const [loading, setLoading] = useState(false);

  const demoHint = useMemo(() => {
    return DEMO_USERS.map((u) => `${u.role}: ${u.email}`).join(" • ");
  }, []);

  async function onSubmit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      // 1) Try backend (if running)
      saveAuth(email.trim(), password, "");
      const me = await apiGet("/api/me"); // backend should return { role: "STUDENT" } etc
      const role = (me?.role || "STUDENT").toUpperCase();
      saveAuth(email.trim(), password, role);
      nav(role === "STUDENT" ? "/student" : "/student"); // extend later for other dashboards
      return;
    } catch {
      // 2) Fallback to demo (frontend-only)
      const match = DEMO_USERS.find(
        (u) => u.email.toLowerCase() === email.trim().toLowerCase() && u.password === password
      );

      if (!match) {
        setError("Login failed. Use a demo account (password: password) or start the backend.");
        setLoading(false);
        return;
      }

      saveAuth(match.email, match.password, match.role);
      nav(match.role === "STUDENT" ? "/student" : "/student");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="container auth">
      <div className="card glass auth-card">
        <h1 className="auth-title">Login</h1>
        <p className="auth-sub">Sign in to open your dashboard and run resume match.</p>

        <form onSubmit={onSubmit} className="form">
          <label className="label">Email</label>
          <input
            className="input"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="studentdemo@test.com"
          />

          <label className="label">Password</label>
          <input
            className="input"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="password"
          />

          <button className="btn btn-primary full" disabled={loading}>
            {loading ? "Signing in..." : "Login"}
          </button>

          {error ? <div className="error">{error}</div> : null}

          <div className="row">
            <Link className="btn btn-ghost" to="/">
              Back
            </Link>
            <Link className="link" to="/register">
              Register
            </Link>
          </div>
        </form>

        <div className="demo-box glass">
          <div className="demo-top">
            <div className="demo-title">Demo mode (for presentation)</div>
            <button className="link-btn" onClick={() => setShowDemos((s) => !s)}>
              {showDemos ? "Hide" : "Show"} demo accounts
            </button>
          </div>

          {showDemos ? (
            <>
              <div className="demo-line">{demoHint}</div>
              <div className="demo-line">Password: <b>password</b></div>
            </>
          ) : (
            <div className="demo-line">Use demo roles: Student / Recruiter / Admin</div>
          )}
        </div>
      </div>

      <div className="small-note">
        If Chrome shows a “password found in a breach” popup: that’s browser-only because the password is literally
        <b> password</b>. Click OK (or use Incognito / “Never save”).
      </div>
    </main>
  );
}
