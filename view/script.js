"use strict";
// Aula 15: login / cadastro / dashboard protegido com Bearer JWT.
const API_BASE = "http://localhost:8080";
const TOKEN_KEY = "token";
const page = document.body.dataset.page;

document.addEventListener("DOMContentLoaded", () => {
  if (page === "login") {
    document.getElementById("loginForm").addEventListener("submit", onLogin);
  } else if (page === "signup") {
    document.getElementById("signupForm").addEventListener("submit", onSignup);
  } else if (page === "dashboard") {
    document.getElementById("btnLogout").addEventListener("click", logout);
    document.getElementById("btnAtualizar").addEventListener("click", loadResources);
    if (!localStorage.getItem(TOKEN_KEY)) { window.location.replace("index.html"); return; }
    loadResources();
  }
});

function message(text, type = "danger") {
  const el = document.getElementById("mensagem");
  if (!el) return;
  el.replaceChildren();
  const alert = document.createElement("div");
  alert.className = `alert alert-${type}`;
  alert.textContent = text;
  el.appendChild(alert);
}

function logout() {
  localStorage.removeItem(TOKEN_KEY);
  window.location.replace("index.html");
}

function setBusy(id, busy) {
  const button = document.getElementById(id);
  if (button) button.disabled = busy;
}

async function parseError(response) {
  try {
    const data = await response.json();
    return data.message || data.error || `Erro HTTP ${response.status}`;
  } catch {
    return `Erro HTTP ${response.status}`;
  }
}

async function login(username, password) {
  const response = await fetch(`${API_BASE}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({username, password})
  });
  if (!response.ok) throw new Error(await parseError(response));
  const body = await response.json();
  const headerToken = response.headers.get("Authorization");
  const token = body.token || (headerToken?.startsWith("Bearer ") ? headerToken.slice(7) : null);
  if (!token) throw new Error("O servidor não retornou um token JWT.");
  localStorage.setItem(TOKEN_KEY, token);
  window.location.replace("dashboard.html");
}

async function onLogin(event) {
  event.preventDefault();
  setBusy("btnEntrar", true);
  try {
    const email = document.getElementById("email").value.trim();
    const senha = document.getElementById("senha").value;
    await login(email, senha);
  } catch (error) { message(error.message || "Não foi possível fazer login."); }
  finally { setBusy("btnEntrar", false); }
}

async function onSignup(event) {
  event.preventDefault();
  setBusy("btnCadastrar", true);
  try {
    const payload = {
      nome: document.getElementById("nome").value.trim(),
      email: document.getElementById("email").value.trim(),
      senha: document.getElementById("senha").value,
      cpf: document.getElementById("cpf").value.trim()
    };
    const response = await fetch(`${API_BASE}/usuario`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    if (!response.ok) throw new Error(await parseError(response));
    message("Cadastro realizado! Redirecionando para o login...", "success");
    document.getElementById("signupForm").reset();
    // Redirecionamento após confirmação; não cria sessão nem token no cadastro.
    window.location.replace("index.html");
  } catch (error) { message(error.message || "Erro ao cadastrar."); }
  finally { setBusy("btnCadastrar", false); }
}

async function protectedFetch(path) {
  const token = localStorage.getItem(TOKEN_KEY);
  if (!token) { logout(); return null; }
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  if (response.status === 401 || response.status === 403) {
    logout();
    return null;
  }
  if (!response.ok) throw new Error(await parseError(response));
  return response.json();
}

// Somente dados pertencentes ao usuário autenticado.
async function loadResources() {
  if (!localStorage.getItem(TOKEN_KEY)) { logout(); return; }
  try {
    const usuario = await protectedFetch("/usuario/me");
    if (!usuario) return;
    const denuncias = await protectedFetch("/denuncia/user");
    if (!denuncias) return;
    document.getElementById("nomeUsuario").textContent = usuario.nome ?? "Usuário";
    document.getElementById("emailUsuario").textContent = usuario.email ?? "";
    document.getElementById("idUsuario").textContent = usuario.idUsuario ?? "—";
    document.getElementById("totalDenuncias").textContent = denuncias.length;
    renderTable(denuncias);
    document.getElementById("mensagem").replaceChildren();
  } catch (error) {
    message(error.message || "Não foi possível consultar os recursos.");
  }
}

function renderTable(items) {
  const tbody = document.getElementById("tabelaDenuncias");
  tbody.replaceChildren();
  if (!Array.isArray(items) || items.length === 0) {
    const tr = document.createElement("tr");
    const td = document.createElement("td");
    td.colSpan = 4;
    td.className = "text-center text-muted py-5";
    td.textContent = "Nenhuma denúncia associada ao seu usuário.";
    tr.appendChild(td);
    tbody.appendChild(tr);
    return;
  }
  for (const item of items) {
    const tr = document.createElement("tr");
    const values = [item.idDenuncia ?? "—", item.titulo ?? "—", item.descricao ?? "—", item.status ?? "—"];
    values.forEach((value, index) => {
      const td = document.createElement("td");
      td.textContent = String(value); // Sem innerHTML com dados da API (evita XSS).
      if (index === 2) td.classList.add("text-break-word");
      if (index === 3) {
        const span = document.createElement("span");
        span.className = "status-pill";
        span.textContent = String(value);
        td.replaceChildren(span);
      }
      tr.appendChild(td);
    });
    tbody.appendChild(tr);
  }
}
