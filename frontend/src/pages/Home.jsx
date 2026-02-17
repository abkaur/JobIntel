import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { loadAuth } from "../api.js";

export default function Home() {
  const nav = useNavigate();
  const { email } = loadAuth();

  const launch = () => {
    if (email) nav("/student");
    else nav("/login");
  };

  return (
    <main className="container hero">
      <section className="hero-grid">
        <div className="hero-left">
          <div className="tagline">Co-op tracker • Skill-gap insights • Resume match</div>

          <h1 className="hero-title">
            Track applications. Analyze job posts.
            <span className="grad">Improve your resume match.</span>
          </h1>

          <p className="hero-sub">
            JobIntel helps you compare your resume against target roles, spot missing skills, and tailor
            faster—especially for Cloud / DevOps co-op prep.
          </p>

          <div className="hero-actions">
            <button className="btn btn-primary" onClick={launch}>
              Launch Demo
            </button>
            <Link className="btn btn-secondary" to="/login">
              Sign in
            </Link>
          </div>

          <div className="hero-stats">
            <div className="stat">
              <div className="stat-num">3</div>
              <div className="stat-label">Demo job posts</div>
            </div>
            <div className="stat">
              <div className="stat-num">12+</div>
              <div className="stat-label">Skill tags</div>
            </div>
            <div className="stat">
              <div className="stat-num">Fast</div>
              <div className="stat-label">Gap insights</div>
            </div>
          </div>

          <div className="note">
            Demo mode uses local login for presentation. Backend integration can be added later.
          </div>
        </div>

        <div className="hero-right">
          <div className="card glass">
            <h3 className="card-title">What you can do</h3>

            <div className="feature">
              <span className="dot" />
              <div>
                <div className="feature-title">Resume Match Score</div>
                <div className="feature-sub">See matched vs missing skills instantly.</div>
              </div>
            </div>

            <div className="feature">
              <span className="dot" />
              <div>
                <div className="feature-title">Skill-gap insights</div>
                <div className="feature-sub">Highlights in-demand tools across job posts.</div>
              </div>
            </div>

            <div className="feature">
              <span className="dot" />
              <div>
                <div className="feature-title">Application tracking (next)</div>
                <div className="feature-sub">Save roles, statuses, and notes (next milestone).</div>
              </div>
            </div>

            <button className="btn btn-primary full" onClick={launch}>
              Open Dashboard
            </button>
          </div>
        </div>
      </section>

      <section className="section">
        <h2 className="section-title">How it works</h2>

        <div className="steps">
          <div className="step glass">
            <div className="step-num">1</div>
            <div className="step-title">Pick a demo job</div>
            <div className="step-sub">Auto-fills job title, description, and required skills.</div>
          </div>

          <div className="step glass">
            <div className="step-num">2</div>
            <div className="step-title">Paste resume text</div>
            <div className="step-sub">Drop your resume content (or a project summary).</div>
          </div>

          <div className="step glass">
            <div className="step-num">3</div>
            <div className="step-title">Get a match score</div>
            <div className="step-sub">See matched skills + what to add next.</div>
          </div>
        </div>
      </section>
    </main>
  );
}
