document.addEventListener('DOMContentLoaded', () => {
    // 테마 토글
//    const themeToggle = document.getElementById('themeToggle');
//    themeToggle.addEventListener('click', () => {
//        const theme = document.documentElement.getAttribute('data-theme');
//        document.documentElement.setAttribute('data-theme', theme === 'light' ? 'dark' : 'light');
//    });

    // 입력 필드 선택
    const passwordInput = document.getElementById('password');
    const confirmInput = document.getElementById('confirmPassword');

    // 에러 div 선택 (없으면 새로 생성)
    let passwordErrorDiv = document.querySelector('#password + .text-danger');
    if (!passwordErrorDiv) {
        passwordErrorDiv = document.createElement('div');
        passwordErrorDiv.classList.add('text-danger', 'mt-1');
        passwordInput.parentNode.appendChild(passwordErrorDiv);
    }

    let confirmErrorDiv = document.querySelector('#confirmPassword + .text-danger');
    if (!confirmErrorDiv) {
        confirmErrorDiv = document.createElement('div');
        confirmErrorDiv.classList.add('text-danger', 'mt-1');
        confirmInput.parentNode.appendChild(confirmErrorDiv);
    }

    // 카드형 UI 생성
    const cardDiv = document.createElement('div');
    cardDiv.classList.add('card', 'p-2', 'mb-3', 'shadow-sm');
    cardDiv.style.fontSize = '0.9rem';
    cardDiv.innerHTML = `
        <div><strong>비밀번호 조건:</strong></div>
        <ul style="list-style: none; padding-left: 0; margin: 0;">
            <li id="cond-length" class="text-danger">✖ 8~20자</li>
            <li id="cond-letter" class="text-danger">✖ 영문 포함</li>
            <li id="cond-number" class="text-danger">✖ 숫자 포함</li>
            <li id="cond-special" class="text-danger">✖ 특수문자 포함</li>
            <li id="cond-match" class="text-danger">✖ 비밀번호 일치</li>
        </ul>
    `;
    passwordInput.parentNode.insertBefore(cardDiv, passwordErrorDiv);

    // 비밀번호 조건 체크
    function validatePassword(pwd) {
        const length = pwd.length >= 8 && pwd.length <= 20;
        const letter = /[A-Za-z]/.test(pwd);
        const number = /\d/.test(pwd);
        const special = /[!@#$%^&*()_+\-={}:;"',.<>/?]/.test(pwd);

        const updateCondition = (id, condition) => {
            const li = document.getElementById(id);
            li.textContent = `${condition ? '✔' : '✖'} ${li.textContent.slice(2)}`;
            li.className = condition ? 'text-success' : 'text-danger';
        };

        updateCondition('cond-length', length);
        updateCondition('cond-letter', letter);
        updateCondition('cond-number', number);
        updateCondition('cond-special', special);

        return length && letter && number && special;
    }

    // 실시간 체크
    function checkPasswordMatch() {
        const pwd = passwordInput.value;
        const confirmPwd = confirmInput.value;

        const valid = validatePassword(pwd);

        // 비밀번호 일치 여부 체크
        const match = pwd && confirmPwd && pwd === confirmPwd;
        const matchLi = document.getElementById('cond-match');
        matchLi.textContent = `${match ? '✔' : '✖'} 비밀번호 일치`;
        matchLi.className = match ? 'text-success' : 'text-danger';

        confirmErrorDiv.textContent = match ? '' : (confirmPwd ? '비밀번호가 일치하지 않습니다.' : '');
        passwordErrorDiv.textContent = '';
    }

    passwordInput.addEventListener('input', checkPasswordMatch);
    confirmInput.addEventListener('input', checkPasswordMatch);

    const signupForm = document.getElementById('signupForm');
    const errorBox = document.getElementById('signupError');

    // 폼 제출 시 최종 체크
    signupForm.addEventListener('submit', async(e) => {
        e.preventDefault();

        const nickname = document.getElementById('nickname').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = passwordInput.value;
        const confirmPassword = confirmInput.value;

        const valid = validatePassword(password);
        const match = password === confirmPassword;

        passwordErrorDiv.textContent = '';
        confirmErrorDiv.textContent = '';
        errorBox.style.display = 'none';

        if (!valid) {
            passwordErrorDiv.textContent = '비밀번호 조건을 모두 충족해야 합니다.';
            return;
        }
        if (!match) {
            confirmErrorDiv.textContent = '비밀번호가 일치하지 않습니다.';
            return;
        }

        try{
            const response = await fetch("/api/auth/signup",{
                method: "POST",
                headers: {"Content-Type":"application/json"},
                body: JSON.stringify(
                    {
                        nickname,
                        email,
                        password,
                        confirmPassword
                    }
                )
            });

            const result = await response.json();

            if (response.ok){
                alert(result.message);
                window.location.href = "/login";
            }else{
                errorBox.style.display = "block";
                errorBox.textContent = result.error || (result.errors ? result.errors.join(", ") : "회원가입 실패");
            }
        }catch(err){
            console.log(err)
            errorBox.style.display = "block";
            errorBox.textContent = "서버 통신 중 오류가 발생했습니다.";
        }
    })
});
