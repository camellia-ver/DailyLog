## 🌿 DailyLog — 감정 기반 일기 웹 서비스

> **“오늘의 감정을 기록하고, 나를 돌아보는 시간”**

DailyLog는 하루하루의 감정과 생각을 기록할 수 있는 **감정 추적형 일기 웹앱**입니다.
사용자는 로그인 후 하루에 하나의 일기를 작성하고, 날짜별로 수정·삭제·조회할 수 있습니다.
또한 감정 데이터 기반 통계를 통해 나의 감정 변화를 시각적으로 확인할 수 있습니다.

---

## 🛠️ Tech Stack

| 구분             | 기술                                           |
| -------------- | -------------------------------------------- |
| **Language**   | Java 21                        |
| **Framework**  | Spring Boot 3.5.6                            |
| **Database**   | MySQL                                        |
| **ORM**        | Spring Data JPA                              |
| **Security**   | Spring Security, JWT                         |
| **Build Tool** | Gradle                                       |
| **Frontend**   | Chart.js, Bootstrap, HTML, CSS, JS, Thymeleaf |
| **Others**     | Lombok,Validation                           |

## ⚙️ 주요 기능

### 📝 1. 일기 CRUD

* 하루에 하나의 일기만 작성 가능 (Unique by user + date)
* 일기 조회 / 수정 / 삭제
* 특정 날짜 선택 시, 해당 날짜의 일기 존재 여부 확인

### 😊 2. Emotion Tracker

* 일기 작성 시 감정 선택 (예: 😊 행복 / 😞 슬픔 / 😡 분노 / 😐 평온 등)
* 감정별 색상 혹은 아이콘으로 시각화

### 📊 3. 통계 기능

* 주별 / 월별 감정 통계
* 감정 비율 차트 (ex. Pie chart)
* 특정 기간 동안의 감정 추세 그래프

### 👤 4. 회원 기능

* 회원가입 / 로그인 / 로그아웃
* JWT 기반 인증/인가
* 비밀번호 암호화 (BCrypt)
* 사용자별 데이터 분리

---

## 🧩 ERD

```
User
 ├── id (PK)
 ├── email
 ├── password
 ├── nickname
 ├── created_at
 └── updated_at

Diary
 ├── id (PK)
 ├── user_id (FK)
 ├── date (Unique with user_id)
 ├── content
 ├── emotion (enum)
 ├── created_at
 └── updated_at
```

---

## 📡 API 요약

| 기능    | Method | Endpoint            | 설명            |
| ----- | ------ | ------------------- | -------------|
| 로그인   | POST   | /api/auth/login     | JWT 발급        |
| 로그아웃  | POST   | /api/auth/logout    | 토큰 무효화        |
| 일기 작성 | POST   | /api/diaries        | 일기 등록         |
| 일기 수정 | PUT    | /api/diaries | 일기 내용 수정      |
| 일기 삭제 | DELETE | /api/diaries/{id} | 일기 삭제         |
| 감정 통계 | GET    | /api/statistics     | 감정별 통계 데이터 조회 |

---

## 🧠 주요 설계 포인트

* **JWT + Spring Security**로 인증/인가 구현
* **Controller → Service → Repository** 3-layer 구조
* **JPA Auditing**으로 created_at, updated_at 자동 관리
* DTO / Entity 분리
* **Emotion Enum**으로 감정 일관성 유지

## 📈 향후 개선 예정

* [ ] 이미지 첨부 기능
* [ ] 일기 검색 (키워드, 감정별)
* [ ] OAuth 로그인 (Google, Kakao)
* [ ] 다크모드 UI