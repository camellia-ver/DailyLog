document.addEventListener('DOMContentLoaded', () => {
    // 테마 토글
    const themeToggle = document.getElementById('themeToggle');
    themeToggle.addEventListener('click', () => {
      const theme = document.documentElement.getAttribute('data-theme');
      document.documentElement.setAttribute('data-theme', theme === 'light' ? 'dark' : 'light');
    });

    // 폼 제출
    document.getElementById('signupForm').addEventListener('submit', (e) => {
      e.preventDefault();
      const name = document.getElementById('name').value;
      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;
      const confirmPassword = document.getElementById('confirmPassword').value;
      const mood = moodInput.value;

      if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
      }

      console.log({ name, email, password, mood });
      alert('회원가입 완료! (콘솔 확인)');
      // 실제 백엔드 연동 필요
    });
})