import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { apiRequest, ApiError } from "../api/apiClient";
import { useAuth } from "../auth/AuthContext";
import SafeMarkdown from "../components/SafeMarkdown";

export default function PageDetailPage() {
  const { id } = useParams();
  const { token, role, logout } = useAuth();
  const [page, setPage] = useState(null);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    async function load() {
      setError(null);

      try {
        const res = await apiRequest(`/api/v1/pages/${id}`, { token });
        setPage(res);
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
        setError("Erreur chargement page");
      }
    }

    load();
  }, [id, token, logout, navigate]);

  if (error) {
    return (
      <div style={{ padding: 24 }}>
        <p>{error}</p>
        <p><Link to="/pages">Retour</Link></p>
      </div>
    );
  }

  if (!page) return <div style={{ padding: 24 }}>Chargement...</div>;

  return (
    <div style={{ padding: 24 }}>
      <p><Link to="/pages">← Retour liste</Link></p>

      <h2>{page.title}</h2>
      <p><b>ID</b> : {page.id}</p>

      {(page.tags?.length ?? 0) > 0 && (
        <p>Tags : {page.tags.join(", ")}</p>
      )}

      <div style={{ whiteSpace: "normal" }}>
        <SafeMarkdown markdown={page.content} />
      </div>

      {(role === "DEV" || role === "ADMIN") && (
        <p><Link to={`/pages/${page.id}/edit`}>Éditer</Link></p>
      )}
    </div>
  );
}
