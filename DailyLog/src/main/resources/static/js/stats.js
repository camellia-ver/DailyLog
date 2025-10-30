document.addEventListener('DOMContentLoaded', async () => {
    if (!window.Chart) return;

    const periodSelect = document.getElementById('periodSelect');
    const pieCanvas = document.getElementById('emotionPie');
    const lineCanvas = document.getElementById('emotionLine');

    if (!periodSelect || !pieCanvas || !lineCanvas) {
        console.error("필요한 DOM 요소를 찾을 수 없습니다!");
        return;
    }

    let pieChart = null;
    let lineChart = null;

    async function fetchAndRenderCharts(period) {
        try {
            // --------------------
            // 1️⃣ 감정 비율 Pie 차트
            // --------------------
            const ratioRes = await fetch(`/api/statistics/emotion-ratio?period=${period}`, {
                credentials: 'same-origin'
            });

            if (!ratioRes.ok) throw new Error(`Pie API 호출 실패: ${ratioRes.status}`);
            const ratioData = await ratioRes.json();

            const pieLabels = Object.keys(ratioData);
            const pieValues = Object.values(ratioData);
            const pieColors = [
                getComputedStyle(document.documentElement).getPropertyValue('--happy') || '#FFD700',
                getComputedStyle(document.documentElement).getPropertyValue('--sad') || '#1E90FF',
                getComputedStyle(document.documentElement).getPropertyValue('--angry') || '#FF4500',
                getComputedStyle(document.documentElement).getPropertyValue('--calm') || '#32CD32'
            ];

            if (pieChart) pieChart.destroy();
            pieChart = new Chart(pieCanvas.getContext('2d'), {
                type: 'pie',
                data: { labels: pieLabels, datasets: [{ data: pieValues, backgroundColor: pieColors }] },
                options: { responsive: true, plugins: { legend: { position: 'bottom' } } }
            });

            // --------------------
            // 2️⃣ 감정 추세 Line 차트
            // --------------------
            const trendRes = await fetch(`/api/statistics/emotion-trend?period=${period}`, {
                credentials: 'same-origin'
            });

            if (!trendRes.ok) throw new Error(`Line API 호출 실패: ${trendRes.status}`);
            const trendData = await trendRes.json();

            const lineLabels = trendData.map(item => new Date(item.date).toLocaleDateString('ko-KR'));
            const lineScores = trendData.map(item => item.score ?? 0); // null 처리

            if (lineChart) lineChart.destroy();
            lineChart = new Chart(lineCanvas.getContext('2d'), {
                type: 'line',
                data: {
                    labels: lineLabels,
                    datasets: [{
                        label: '감정 점수',
                        data: lineScores,
                        tension: 0.3,
                        fill: true,
                        backgroundColor: 'rgba(54,162,235,0.2)',
                        borderColor: 'rgba(54,162,235,1)',
                        pointBackgroundColor: 'rgba(54,162,235,1)',
                        pointRadius: 4
                    }]
                },
                options: {
                    responsive: true,
                    plugins: { legend: { display: false } },
                    scales: { y: { beginAtZero: true, suggestedMax: 5 } }
                }
            });

        } catch (err) {
            console.error("차트 데이터를 불러오거나 렌더링하는 중 오류 발생:", err);
        }
    }

    async function updateSummary(period) {
        try{
            const res = await fetch(`/api/statistics/summary?period=${period}`,{
                credentials: 'same-origin'
            });

            if(!res.ok) throw new Error(`API 호출 실패: ${res.status}`);
            const data = await res.json();

            const summaryDiv = document.getElementById('summaryInfo');

            summaryDiv.innerHTML = `
                평균 감정 지수: <strong>${data.avgEmotionScore.toFixed(1)}</strong> · 
                긍정 비율: <strong>${(data.positiveRatio * 100).toFixed(0)}%</strong>
            `;
        }catch(err){
            console.error("Summary 데이터 로딩 실패:", err);
        }
    }

    // 초기 렌더링
    await fetchAndRenderCharts(periodSelect.value);

    // 선택 박스 변경시 차트 업데이트
    periodSelect.addEventListener('change', async (e) => {
        const period = e.target.value;
        updateSummary(period);

        await fetchAndRenderCharts(e.target.value);
    });
});