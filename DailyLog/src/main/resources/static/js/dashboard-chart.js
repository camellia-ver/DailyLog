document.addEventListener('DOMContentLoaded', async() => {
    // 감정 점수 API 호출
    let emotionData = [];
    try{
        const res = await fetch('/api/statistics/seven-days-emotion-score');
        emotionData = await res.json();
    }catch(e){
        console.error("감정 점수 불러오기 실패:", e);
        return;
    }

    // API 데이터 → 차트 형식으로 변환
    const labels = emotionData.map(item => new Date(item.date).toLocaleDateString('ko-KR'));
    const scores = emotionData.map(item => item.score);

    // miniTrend chart
    if(window.Chart){
        const mini = document.getElementById('miniTrend');
        if(mini){
            new Chart(mini.getContext('2d'), {
                type: 'line',
                data: {
                    labels,
                    datasets: [{
                        label: '감정 지수',
                        data: scores,
                        tension: 0.4,
                        fill: false
                    }]
                },
                options: {
                    plugins: {legend:{display:false}},
                    scales: {y:{display:false}}
                }
            });
        }

        // emotionLine 차트
        const line = document.getElementById('emotionLine');
        if(line){
            new Chart(line.getContext('2d'), {
                type:'line',
                data:{
                    labels,
                    datasets:[{
                        label:'감정 점수',
                        data:scores,
                        tension:0.3,
                        fill:true
                    }]
                },
                options:{plugins:{legend:{display:false}}}
            });
        }
    }
});