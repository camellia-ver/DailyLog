document.getElementById("loginForm").addEventListener("submit", async function(e) {
    e.preventDefault();

    const email = this.email.value;
    const password = this.password.value;

    try {
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();
        if (!response.ok) {
            showMessage(data.error, "error");
            return;
        }

        // 로그인 성공 → 바로 / 이동 (쿠키가 자동으로 전송됨)
        window.location.href = "/";
    } catch (err) {
        showMessage("서버 오류", "error");
        console.error(err);
    }
});

function showMessage(msg, type) {
    const messageBox = document.getElementById("messageBox");
    if (!messageBox) return;
    messageBox.textContent = msg;
    messageBox.className = type; // success / error
}
