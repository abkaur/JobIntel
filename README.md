# JobIntel — Co-op Tracker & Market Analyzer (Full-Stack Project)

JobIntel is a full-stack project designed to help co-op students **track applications** and **analyze job market skill trends** so they can tailor resumes faster and apply smarter.

This project is actively being developed and expanded over time. The current version already includes a working frontend experience and a foundation for backend integration, with continuous improvements planned.

---

## What JobIntel Does

### ✅ Co-op Application Tracking
Track co-op applications with details like:
- Company
- Role title
- Status (Applied / Interview / Offer / Rejected)
- Date applied
- Notes

### ✅ Resume Match + Skill Gap Insights
Paste a job description + resume text to get:
- Match percentage
- Matched skills
- Missing skills (skill gaps)

### ✅ Market Analyzer / Skill Trends (in progress)
Analyzes job postings to highlight:
- Most in-demand skills
- Skill trends for cloud/DevOps roles
- Keyword frequency insights (with synonyms)

---

## Tech Stack (Planned + In Progress)

**Frontend**
- React + Vite
- React Router
- CSS (UI styling)
- Charts planned (Recharts / Chart.js)

**Backend**
- Java 17 + Spring Boot
- REST APIs
- Spring Security + JWT 
- PostgreSQL via Docker Compose 

---

## Run Locally

### Frontend
```bash
cd frontend
npm install
npm run dev


Frontend runs on:
http://localhost:5173

Backend

Run from IntelliJ (Spring Boot), typically on:
http://localhost:8080
