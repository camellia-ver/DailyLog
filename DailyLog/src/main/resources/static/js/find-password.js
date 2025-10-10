document.addEventListener('DOMContentLoaded', () => {
    // 폼 제출 이벤트
        document.getElementById('findPasswordForm').addEventListener('submit', function(e){
            e.preventDefault();
            const email = document.getElementById('email').value;
            if(!email){
                alert('이메일을 입력해주세요.');
                return;
            }
            alert('비밀번호 재설정 링크가 발송되었습니다.\n(콘솔 확인)');
            console.log({ email });
            // 실제 백엔드 연동 필요
        });

        // 다크모드 토글
        const themeToggle = document.getElementById('themeToggle');
        themeToggle.addEventListener('click', () => {
            const theme = document.documentElement.getAttribute('data-theme');
            document.documentElement.setAttribute('data-theme', theme === 'light' ? 'dark' : 'light');
        });
})