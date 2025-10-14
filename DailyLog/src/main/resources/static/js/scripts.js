document.addEventListener('DOMContentLoaded', () => {

    // 연도 자동 갱신
    const yearEl = document.getElementById('year');
    if(yearEl) yearEl.textContent = new Date().getFullYear();

    // 차트 초기화
    if(window.Chart){
        const mini = document.getElementById('miniTrend');
        if(mini){
            new Chart(mini.getContext('2d'), {
                type: 'line',
                data: {
                    labels: ['월','화','수','목','금','토','일'],
                    datasets: [{
                        label:'감정 지수',
                        data:[3,4,2,4,5,4,3],
                        tension:0.4,
                        fill:false
                    }]
                },
                options: {
                    plugins:{legend:{display:false}},
                    scales:{y:{display:false}}
                }
            });
        }

        const pie = document.getElementById('emotionPie');
        if(pie){
            new Chart(pie.getContext('2d'), {
                type:'pie',
                data:{
                    labels:['행복','슬픔','분노','평온'],
                    datasets:[{
                        data:[40,25,15,20],
                        backgroundColor:[
                            getComputedStyle(document.documentElement).getPropertyValue('--happy'),
                            getComputedStyle(document.documentElement).getPropertyValue('--sad'),
                            getComputedStyle(document.documentElement).getPropertyValue('--angry'),
                            getComputedStyle(document.documentElement).getPropertyValue('--calm')
                        ]
                    }]
                }
            });
        }

        const line = document.getElementById('emotionLine');
        if(line){
            new Chart(line.getContext('2d'), {
                type:'line',
                data:{
                    labels:['1주전','6일전','5일전','4일전','3일전','2일전','어제'],
                    datasets:[{
                        label:'감정 점수',
                        data:[2.5,3.2,3.0,4.1,3.8,4.0,3.6],
                        tension:0.3,
                        fill:true
                    }]
                },
                options:{plugins:{legend:{display:false}}}
            });
        }
    }

});
