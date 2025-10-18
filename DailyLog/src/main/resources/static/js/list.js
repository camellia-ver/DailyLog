document.addEventListener('DOMContentLoaded', () => {
    // 오늘 날짜 구하기
    const today = new Date();

    // flatpickr 적용
    flatpickr("#myCalendar", {
        inline: true,
        dateFormat: "Y-m-d",
        defaultDate: today,
        onChange: function(selectedDates, dateStr, instance) {
            // 배지 업데이트
            document.getElementById('selectedDateBadge').textContent = dateStr + ' · 😊';
            // hidden input 동기화
            document.getElementById('todayInput').value = dateStr;
        }
    });

    // 초기 배지에 오늘 날짜 표시
    document.getElementById('selectedDateBadge').textContent = today.toISOString().split('T')[0] + ' · 😊';
    document.getElementById('todayInput').value = today.toISOString().split('T')[0];

    const editBtn = document.getElementById("btnEdit");
    const deleteBtn = document.getElementById("btnDelete");

    editBtn.addEventListener("click", async () => {
        const date = document.getElementById('todayInput').value;
        const emotion = document.getElementById('diaryMood').value;
        const content = document.getElementById('diaryContent').value;
        const id = document.getElementById('diaryId').value;

        try {
            const response = await fetch("/api/diaries", {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    id: id,
                    today: date,
                    emotion: emotion,
                    content: content
                })
            });

            if (!response.ok) {
                throw new Error("업데이트 실패");
            }

            alert("일기가 업데이트되었습니다!");
            window.location.href = "/list";
        } catch (error) {
            alert("업데이트 중 오류가 발생했습니다.");
        }
    });

    // 필요 시 삭제 버튼 기능도 여기에 추가 가능
});
