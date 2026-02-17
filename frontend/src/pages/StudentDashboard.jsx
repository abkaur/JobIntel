import React, { useMemo, useState } from "react";
import { mockJobs } from "../mockJobs.js";

function normalize(s) {
  return (s || "")
    .toLowerCase()
    .replace(/[^a-z0-9+/#\s-]/g, " ")
    .replace(/\s+/g, " ")
    .trim();
}

function computeMatch(jobSkills, resumeText) {
  const text = normalize(resumeText);
  const matched = [];
  const missing = [];

  for (const skill of jobSkills) {
    const key = normalize(skill);
    if (!key) continue;

    // simple match: exact token OR contains
    if (text.includes(key)) matched.push(skill);
    else missing.push(skill);
  }

  const score =
    jobSkills.length === 0 ? 0 : Math.round((matched.length / jobSkills.length) * 100);

  return { score, matched, missing, requiredCount: jobSkills.length };
}

export default function StudentDashboard() {
  const [selectedJobId, setSelectedJobId] = useState(mockJobs[0]?.id || "");
  const selectedJob = useMemo(
    () => mockJobs.find((j) => j.id === selectedJobId) || mockJobs[0],
    [selectedJobId]
  );

  const [jobTitle, setJobTitle] = useState(selectedJob?.title || "");
  const [jobDescription, setJobDescription] = useState(selectedJob?.description || "");
  const [resumeText, setResumeText] = useState("");

  const [result, setResult] = useState(null);

  function useThisJob(job) {
    setSelectedJobId(job.id);
    setJobTitle(job.title);
    setJobDescription(job.description);
    setResult(null); // IMPORTANT: don't show score until user clicks
  }

  function onGetMatch() {
    const skills = selectedJob?.skills || [];
    const r = computeMatch(skills, resumeText);
    setResult(r);
  }

  function refreshJobs() {
    // demo-only: just clears selection result
    setResult(null);
  }

  return (
    <main className="container dash">
      <div className="dash-header">
        <div>
          <h1 className="dash-title">Student Dashboard</h1>
          <p className="dash-sub">
            Browse demo jobs + run a resume match score (keyword matching).
          </p>
        </div>

        <button className="btn btn-secondary" onClick={refreshJobs}>
          Refresh Jobs
        </button>
      </div>

      <section className="dash-grid">
        {/* LEFT: jobs */}
        <div className="card glass">
          <div className="card-title-row">
            <h2 className="card-title">Available Jobs (Demo)</h2>
            <div className="card-sub">Pick a job to auto-fill details.</div>
          </div>

          <div className="job-list">
            {mockJobs.map((job) => (
              <div key={job.id} className={`job-card ${job.id === selectedJobId ? "active" : ""}`}>
                <div className="job-top">
                  <div>
                    <div className="job-title">{job.title}</div>
                    <div className="job-meta">
                      {job.company} • {job.location}
                    </div>
                  </div>
                </div>

                <div className="tags">
                  {job.skills.slice(0, 6).map((s) => (
                    <span key={s} className="chip">
                      {s}
                    </span>
                  ))}
                </div>

                <div className="job-desc">{job.description}</div>

                <button className="btn btn-ghost" onClick={() => useThisJob(job)}>
                  Use this job
                </button>
              </div>
            ))}
          </div>
        </div>

        {/* RIGHT: resume match */}
        <div className="card glass">
          <h2 className="card-title">Resume Match Score (MVP)</h2>
          <div className="card-sub">
            Paste resume text, then click <b>Get Match Score</b>.
          </div>

          <div className="form">
            <label className="label">Job Title</label>
            <input className="input" value={jobTitle} onChange={(e) => setJobTitle(e.target.value)} />

            <label className="label">Job Description</label>
            <textarea
              className="textarea"
              rows={5}
              value={jobDescription}
              onChange={(e) => setJobDescription(e.target.value)}
            />

            <label className="label">Resume Text</label>
            <textarea
              className="textarea"
              rows={7}
              value={resumeText}
              onChange={(e) => setResumeText(e.target.value)}
              placeholder="Paste your resume text here..."
            />

            <button className="btn btn-primary full" onClick={onGetMatch} disabled={!resumeText.trim()}>
              Get Match Score
            </button>
          </div>

          {/* Result ONLY after click */}
          {result ? (
            <div className="result glass">
              <div className="score-row">
                <div className="score">{result.score}%</div>
                <div className="score-sub">
                  Based on <b>{result.requiredCount}</b> required skills
                </div>
              </div>

              <div className="two-col">
                <div>
                  <div className="mini-title">Matched</div>
                  <div className="tags">
                    {result.matched.length ? (
                      result.matched.map((s) => (
                        <span key={s} className="chip ok">
                          {s}
                        </span>
                      ))
                    ) : (
                      <div className="muted">None</div>
                    )}
                  </div>
                </div>

                <div>
                  <div className="mini-title">Missing</div>
                  <div className="tags">
                    {result.missing.length ? (
                      result.missing.map((s) => (
                        <span key={s} className="chip warn">
                          {s}
                        </span>
                      ))
                    ) : (
                      <div className="muted">None 🎉</div>
                    )}
                  </div>
                </div>
              </div>

              <div className="muted small">
                Demo logic: simple keyword match. Next: connect Spring Boot API + store job posts + analytics.
              </div>
            </div>
          ) : (
            <div className="muted small" style={{ marginTop: 12 }}>
              Tip: pick a job on the left, paste resume text, then run match.
            </div>
          )}
        </div>
      </section>
    </main>
  );
}
