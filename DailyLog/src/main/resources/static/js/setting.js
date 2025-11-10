document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("settingForm");
    const errorDiv = document.getElementById("settingError");
    const saveBtn = form.querySelector("button[type='submit']");
    const deleteBtn = document.getElementById("deleteAccountBtn");

    // ---------- 회원 정보 수정 ----------
    form.addEventListener("submit", async function(e) {
        e.preventDefault();
        saveBtn.disabled = true;
        errorDiv.style.display = "none";

        const nickname = document.getElementById("nickname").value.trim();
        const email = document.getElementById("email").value.trim();
        const currentPassword = document.getElementById("currentPassword").value;
        const newPassword = document.getElementById("newPassword").value;
        const confirmNewPassword = document.getElementById("confirmNewPassword").value;

        // 입력 검증
        if (!nickname) return showError("닉네임을 입력해주세요.");
        if (!email) return showError("이메일을 입력해주세요.");
        if (!currentPassword) return showError("현재 비밀번호를 입력해주세요.");

        if (newPassword || confirmNewPassword) {
            if (newPassword !== confirmNewPassword) return showError("새 비밀번호가 일치하지 않습니다.");
            if (newPassword.length < 8) return showError("새 비밀번호는 최소 8자 이상이어야 합니다.");
        }

        const data = { nickname, email, currentPassword, newPassword: newPassword || null };

        try {
            const response = await fetch("/api/auth/user-update", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });

            const result = await response.json();

            if (response.ok) {
                const changedFields = [];
                if (result.nicknameChanged) changedFields.push("닉네임");
                if (result.emailChanged) changedFields.push("이메일");
                if (result.passwordChanged) changedFields.push("비밀번호");

                let msg = "회원정보가 저장되었습니다.";
                if (changedFields.length > 0) msg += " (" + changedFields.join(", ") + " 변경됨)";
                alert(msg);
                form.reset();
            } else {
                showError(result.error || result.message || "설정 저장에 실패했습니다.");
            }
        } catch (err) {
            console.error(err);
            showError("서버와 통신 중 오류가 발생했습니다.");
        } finally {
            saveBtn.disabled = false;
        }
    });

    // ---------- 회원 탈퇴 ----------
    deleteBtn.addEventListener("click", async function() {
        const confirmed = confirm("정말로 회원 탈퇴를 진행하시겠습니까? 이 작업은 되돌릴 수 없습니다.");
        if (!confirmed) return;

        deleteBtn.disabled = true;
        errorDiv.style.display = "none";

        const currentPassword = document.getElementById("currentPassword").value;
        if (!currentPassword) {
            showError("회원 탈퇴를 위해 현재 비밀번호를 입력해주세요.");
            return;
        }

        try {
            const response = await fetch("/api/auth/user-delete", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ currentPassword })
            });

            const result = await response.json();

            if (response.ok) {
                alert("회원 탈퇴가 완료되었습니다. 그동안 이용해주셔서 감사합니다.");
                window.location.href = "/"; // 탈퇴 후 이동할 페이지
            } else {
                showError(result.error || result.message || "회원 탈퇴에 실패했습니다.");
            }
        } catch (err) {
            console.error(err);
            showError("서버와 통신 중 오류가 발생했습니다.");
        } finally {
            deleteBtn.disabled = false;
        }
    });

    // ---------- 에러 처리 ----------
    function showError(message) {
        errorDiv.textContent = message;
        errorDiv.style.display = "block";
        saveBtn.disabled = false;
        deleteBtn.disabled = false;
    }
});
