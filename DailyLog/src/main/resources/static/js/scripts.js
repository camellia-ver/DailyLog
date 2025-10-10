// 공용 스크립트: nav/footer 로드, 간단한 상호작용, 차트 초기화
document.addEventListener('DOMContentLoaded', () => {
// nav/footer include
['nav','footer'].forEach(name => {
const el = document.getElementById(name + '-include');
if (!el) return;
fetch(`partials/${name}.html`).then(r => r.text()).then(html => el.innerHTML = html);
});


// 연도 자동 갱신
const yearEl = document.getElementById('year');
if(yearEl) yearEl.textContent = new Date().getFullYear();


// 간단한 emoji selector 핸들러
document.querySelectorAll('[data-emoji]').forEach(btn => {
btn.addEventListener('click', (e)=>{
document.querySelectorAll('[data-emoji]').forEach(b=>b.classList.remove('emoji-selected'));
btn.classList.add('emoji-selected');
const input = document.querySelector('#selectedEmotion');
if(input) input.value = btn.dataset.value;
});
});


// 차트 초기화 (존재하면)
if(window.Chart){
// mini chart on dashboard (if element exists)
const mini = document.getElementById('miniTrend');
if(mini){
new Chart(mini.getContext('2d'),{
type:'line', data:{labels:['월','화','수','목','금','토','일'], datasets:[{label:'감정 지수', data:[3,4,2,4,5,4,3], tension:0.4, fill:false}]}, options:{plugins:{legend:{display:false}}, scales:{y:{display:false}}}
});
}


// stats charts (if present) — Pie and Line
const pie = document.getElementById('emotionPie');
if(pie){
new Chart(pie.getContext('2d'),{type:'pie', data:{labels:['행복','슬픔','분노','평온'], datasets:[{data:[40,25,15,20], backgroundColor:[getComputedStyle(document.documentElement).getPropertyValue('--happy'), getComputedStyle(document.documentElement).getPropertyValue('--sad'), getComputedStyle(document.documentElement).getPropertyValue('--angry'), getComputedStyle(document.documentElement).getPropertyValue('--calm')]}]});
}
const line = document.getElementById('emotionLine');
if(line){
new Chart(line.getContext('2d'),{type:'line', data:{labels:['1주전','6일전','5일전','4일전','3일전','2일전','어제'], datasets:[{label:'감정 점수', data:[2.5,3.2,3.0,4.1,3.8,4.0,3.6], tension:0.3, fill:true}]}, options:{plugins:{legend:{display:false}}}});
}
}


});


// 단순 폼 검사 함수 (예시)
function validateLogin(form){
const email = form.querySelector('input[name=email]').value.trim();
const pw = form.querySelector('input[name=password]').value.trim();
if(!email || !pw){ alert('이메일과 비밀번호를 입력해 주세요.'); return false; }
return true;
}