import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { apiRequest, ApiError } from "../api/apiClient";
import { useAuth } from "../auth/AuthContext";

export default function PagesListPage() {
  const { token, role, logout } = useAuth();
  const [pages, setPages] = useState([]);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    async function load() {
      setError(null);

      try {
        let res;

        // 1) on tente la pagination Spring
        try {
          res = await apiRequest("/api/v1/pages?page=0&size=20", { token });
        } catch {
          // 2) sinon, liste simple
          res = await apiRequest("/api/v1/pages", { token });
        }

        const content = Array.isArray(res) ? res : (res?.content ?? []);
        setPages(content);
      } catch (e) {
        if (e instanceof ApiError && e.status === 401) {
          logout();
          navigate("/login");
          return;
        }
        if (e instanceof ApiError && e.status === 403) {
          navigate("/forbidden");
          return;
        }
        setError("Erreur chargement pages");
      }
    }

    load();
  }, [token, logout, navigate]);

  async function onDelete(id) {
    if (!window.confirm(`Supprimer ${id} ?`)) return;

    try {
      await apiRequest(`/api/v1/pages/${id}`, { method: "DELETE", token });
      setPages(prev => prev.filter(p => p.id !== id)); // update UI
    } catch (e) {
      if (e instanceof ApiError && e.status === 401) {
        logout();
        navigate("/login");
        return;
      }
      if (e instanceof ApiError && e.status === 403) {
        navigate("/forbidden");
        return;
      }
      setError(e instanceof ApiError ? JSON.stringify(e.body) : "Erreur suppression");
    }
  }

  return (
    <div style={{ padding: 24 }}>
      <h2>Pages</h2>

      <div style={{ marginBottom: 12 }}>
        <p>Rôle : {role}</p>

        <button
          style={{ marginLeft: 12 }}
          onClick={async () => {
            await logout();
            navigate("/login");
          }}
        >
          Déconnexion
        </button>

        {(role === "DEV" || role === "ADMIN") && (
          <Link to="/pages/new">Créer une page</Link>
        )}
      </div>

      {error && <p>{error}</p>}

      <ul>
        {pages.map((p) => (
          <li key={p.id}>
            <Link to={`/pages/${p.id}`}>
              {p.title} ({p.id})
            </Link>
            {(p.tags?.length ?? 0) > 0 && (
                <div style={{ fontSize: 12, opacity: 0.8 }}>
                  Tags : {p.tags.join(", ")}
                </div>
            )}
            {(role === "DEV" || role === "ADMIN") && (
              <button style={{ marginLeft: 10 }} onClick={() => onDelete(p.id)}>
                Supprimer
              </button>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
