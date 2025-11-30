const API_BASE = "http://localhost:8888/api";

// POST (with token support)
async function apiPost(path, body) {
    const token = localStorage.getItem("token");

    const headers = { "Content-Type": "application/json" };
    if (token) headers["Authorization"] = `Bearer ${token}`;

    const res = await fetch(API_BASE + path, {
        method: "POST",
        headers,
        body: JSON.stringify(body)
    });

    // Better error handling: return JSON on success, otherwise throw with body text
    const text = await res.text();
    if (!res.ok) {
        throw new Error(text || `HTTP ${res.status}`);
    }

    try {
        return JSON.parse(text);
    } catch (e) {
        return text;
    }
}

// GET (with token)
async function apiGet(path) {
    const token = localStorage.getItem("token");
    const headers = {};

    if (token) headers["Authorization"] = `Bearer ${token}`;

    const res = await fetch(API_BASE + path, { headers });
    const text = await res.text();
    if (!res.ok) {
        throw new Error(text || `HTTP ${res.status}`);
    }

    try {
        return JSON.parse(text);
    } catch (e) {
        return text;
    }
}

// PUT (with token)
async function apiPut(path, body) {
    const token = localStorage.getItem("token");

    const headers = { "Content-Type": "application/json" };
    if (token) headers["Authorization"] = `Bearer ${token}`;

    const res = await fetch(API_BASE + path, {
        method: "PUT",
        headers,
        body: body ? JSON.stringify(body) : undefined
    });

    const text = await res.text();
    if (!res.ok) {
        throw new Error(text || `HTTP ${res.status}`);
    }

    try {
        return JSON.parse(text);
    } catch (e) {
        return text;
    }
}

async function login(email, password, role) {
    return await apiPost(`/auth/login/${role}`, { email, password });
}


// REGISTER
async function register(role, userData) {
    return await apiPost(`/auth/register/${role}`, userData);
}
