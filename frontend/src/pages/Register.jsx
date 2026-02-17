import React from "react";
import { Link } from "react-router-dom";

export default function Register() {
  return (
    <main className="container auth">
      <div className="card glass auth-card">
        <h1 className="auth-title">Register</h1>
        <p className="auth-sub">
          Demo project: registration can be added later (backend + DB).
        </p>

        <div className="row">
          <Link className="btn btn-primary" to="/login">
            Go to Login
          </Link>
          <Link className="btn btn-ghost" to="/">
            Back Home
          </Link>
        </div>
      </div>
    </main>
  );
}
