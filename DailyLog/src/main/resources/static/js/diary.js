document.addEventListener('DOMContentLoaded', () => {
    // emoji 클릭 이벤트 (이벤트 위임)
    document.addEventListener('click', (e) => {
        const btn = e.target.closest('.emoji-btn');
        if (!btn) return;
        document.querySelectorAll('.emoji-btn').forEach(b => b.classList.remove('emoji-selected'));
        btn.classList.add('emoji-selected');
        const input = document.getElementById('selectedEmotion');
        if(input) input.value = btn.dataset.value;
    });

    // form 제출 이벤트
    document.getElementById('diaryForm').addEventListener('submit', async function(e) {
        e.preventDefault();

        // Thymeleaf에서 isWrite 값 가져오기
        const isWrite = /*[[${isWrite}]]*/ true; // 서버에서 true/false 전달
        const today = document.getElementById('todayInput').value;
        const emotion = document.getElementById('selectedEmotion').value;
        const content = document.getElementById('diaryContent').value;

        const url = isWrite ? '/api/diaries' : `/api/diaries/${today}`;
        const method = isWrite ? 'POST' : 'PUT';

        try{
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type' : 'application/json'
                },
                body: JSON.stringify({
                    today: today,
                    emotion: emotion,
                    content: content
                })
            });

            if(!response.ok) throw new Error('서버 에러');
            alert('저장 완료!');
            window.location.href = "/diary";
        }catch (err){
            alert('저장 실패');
        }
    })
})