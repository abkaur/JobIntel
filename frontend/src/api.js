import axios from "axios";

const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

export function loadAuth() {
  return {
    email: localStorage.getItem("auth_email") || "",
    password: localStorage.getItem("auth_password") || "",
    role: localStorage.getItem("auth_role") || "",
  };
}

export function saveAuth(email, password, role = "") {
  localStorage.setItem("auth_email", email);
  localStorage.setItem("auth_password", password);
  if (role) localStorage.setItem("auth_role", role);
}

export function clearAuth() {
  localStorage.removeItem("auth_email");
  localStorage.removeItem("auth_password");
  localStorage.removeItem("auth_role");
}

function authHeader() {
  const { email, password } = loadAuth();
  if (!email || !password) return {};
  const token = btoa(`${email}:${password}`);
  return { Authorization: `Basic ${token}` };
}

export async function apiGet(path) {
  const res = await axios.get(`${API_BASE}${path}`, {
    headers: { ...authHeader() },
  });
  return res.data;
}

export async function apiPost(path, body) {
  const res = await axios.post(`${API_BASE}${path}`, body, {
    headers: { "Content-Type": "application/json", ...authHeader() },
  });
  return res.data;
}
