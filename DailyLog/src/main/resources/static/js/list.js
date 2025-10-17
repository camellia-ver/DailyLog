document.addEventListener('DOMContentLoaded', () => {
    // 오늘 날짜 구하기
        const today = new Date();

        // flatpickr 적용
        flatpickr("#myCalendar", {
            inline: true,          // 카드 안에서 바로 표시
            dateFormat: "Y-m-d",   // 표시 형식
            defaultDate: today,     // 오늘 날짜 기본 선택
            onChange: function(selectedDates, dateStr, instance) {
                // 날짜 선택 시 배지 업데이트
                document.getElementById('selectedDateBadge').textContent = dateStr + ' · 😊';
            }
        });

        // 초기 배지에 오늘 날짜 표시
        document.getElementById('selectedDateBadge').textContent = today.toISOString().split('T')[0] + ' · 😊';
})