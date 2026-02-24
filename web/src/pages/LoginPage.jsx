import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { apiRequest, ApiError } from "../api/apiClient";
import { useAuth } from "../auth/AuthContext";

export default function LoginPage() {
  const [username, setUsername] = useState("admin");
  const [password, setPassword] = useState("Admin123!");
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const auth = useAuth();
  const navigate = useNavigate();

  async function onSubmit(e) {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const res = await apiRequest("/api/v1/auth/login", {
        method: "POST",
        body: { username, password }
      });

      // res doit contenir token / username / role (comme ton API)
      auth.login(res);
      navigate("/pages");
    } catch (e) {
      if (e instanceof ApiError) {
        // e.body peut être du texte ou du JSON
        const msg = typeof e.body === "string" ? e.body : "Login failed";
        setError(`${msg} (HTTP ${e.status})`);
      } else {
        setError("Login failed");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: 24 }}>
      <h2>Mycellius — Login</h2>
      <h3>Bienvenue</h3>
      <form onSubmit={onSubmit} style={{ display: "grid", gap: 12, maxWidth: 320 }}>
        <input
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="username"
          autoComplete="username"
        />
        <input
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="password"
          type="password"
          autoComplete="current-password"
        />
        <button type="submit" disabled={loading}>
          {loading ? "Connexion..." : "Se connecter"}
        </button>
      </form>

      {error && <p style={{ marginTop: 12 }}>{error}</p>}
    </div>
  );
}
