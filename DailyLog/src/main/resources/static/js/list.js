document.addEventListener('DOMContentLoaded', () => {
    // 1. URL에서 date 파라미터 읽기
    const urlParams = new URLSearchParams(window.location.search);
    let selectedDateStr = urlParams.get('date'); // 문자열
    let selectedDate;

    // 문자열이 있으면 Date 객체로 변환, 없으면 오늘 날짜
    if (selectedDateStr) {
        selectedDate = new Date(selectedDateStr);
    } else {
        selectedDate = new Date();
    }

    // flatpickr 적용
    flatpickr("#myCalendar", {
        inline: true,
        dateFormat: "Y-m-d",
        defaultDate: selectedDate,
        onChange: function(selectedDates, dateStr, instance) {
            // 배지 업데이트
            document.getElementById('selectedDateBadge').textContent = dateStr + ' · 😊';
            // hidden input 동기화
            const inputElem = document.getElementById('todayInput');
            if (inputElem) {
                inputElem.value = dateStr;
            }
            window.location.href = `/list?date=${dateStr}`;
        }
    });

    // 초기 배지에 오늘 날짜 표시
    document.getElementById('selectedDateBadge').textContent = selectedDate.toISOString().split('T')[0] + ' · 😊';

    const editBtn = document.getElementById("btnEdit");
    const deleteBtn = document.getElementById("btnDelete");

    if(editBtn){
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
                window.location.href = `/list?date=${date}`;
            } catch (error) {
                alert("업데이트 중 오류가 발생했습니다.");
            }
        });
    }

    // 필요 시 삭제 버튼 기능도 여기에 추가 가능
});
