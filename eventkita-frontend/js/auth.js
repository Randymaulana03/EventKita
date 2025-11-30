async function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const role = document.getElementById("role").value;

    const data = await window.login(email, password, role);

    if (!data.token) {
        document.getElementById("message").innerText =
            "❌ " + (data.message || "Login gagal");
        return;
    }

    // Simpan token user
    localStorage.setItem("token", data.token);
    localStorage.setItem("user", JSON.stringify(data));

    // Redirect sesuai role
    if (data.role === "ORGANIZER") {
        window.location.href = "organizer-dashboard.html";
    } else {
        window.location.href = "index.html";
    }
}


// --- FUNGSI REGISTRASI DENGAN PERBAIKAN SCOPE DAN ERROR ---
async function register() {
    const fullName = document.getElementById("fullName").value;
    const email = document.getElementById("email").value;
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const companyName = document.getElementById("companyName")?.value || null;
    const role = document.getElementById("role").value;

    const body = { fullName, email, username, password, companyName };

    const data = await window.register(role, body);

    if (data.error || data.message) {
        alert("❌ " + (data.error || data.message));
    } else {
        alert("✅ Registrasi berhasil");
        window.location.href = "login.html";
    }
}
