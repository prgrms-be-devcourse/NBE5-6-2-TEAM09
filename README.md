# 요구사항 정의서

### 🧭 프로젝트 개요

> 개발자 취업을 준비하는 사용자가 일정과 집중 시간을 효율적으로 관리하고, 건강 알림과 면접 준비까지 통합적으로 수행할 수 있는 스케줄러 웹 서비스 제공
> 

---

### 👥 주요 사용자

- 취준생 (프론트/백엔드 목표)
- 자기 주도 학습을 진행 중인 사람
- 일정/집중 관리가 필요한 사람

---

### ✅ 사용자 요구사항

### 🗂️ 1. TODO 리스트 기능

- 사용자는 할 일을 추가/수정/삭제할 수 있어야 한다.
- 사용자는 할 일을 조회할 수 있어야 한다.
- 사용자는 할 일을 완료할 수 있어야 하며, 완료된 할 일을 취소할 수 있어야 한다.
- 완료된 할 일은 하단에 회색 처리로 따로 구분되어 보여야 한다.

### 🔔 2. 푸시 알람

- 사용자는 알림 설정을 끄고 켤 수 있어야 한다. (마이페이지에서 설정)
- 할 일 시작 10분 전에 알림을 받을 수 있어야 한다. (옵션 가능)
- 알림 방식은 화면 점멸과 음성 알림으로 제공될 수 있어야 한다.

### 📅 3. 데일리 루틴

- 사용자는 카테고리로 데일리 루틴을 추가/수정/삭제할 수 있어야 한다.
- 사용자는 데일리 루틴을 조회할 수 있어야 한다.
- 데일리 루틴은 포모도로 타이머를 기반으로 시간 측정 후 완료 가능해야 한다.
- 완료된 데일리 루틴은 하단에 회색 처리로 따로 구분되어 보여야 한다.
- 완료된 데일리 루틴은 취소할 수 있어야 한다.
- 데일리 루틴은 쉬어가기 상태로 전환할 수 있어야 한다.

### ⏱️ 4. 순공 시간 타이머

- 사용자는 포모도로 방식의 타이머(예: 25분/5분)를 사용할 수 있어야 한다.
- 타이머는 시작/일시정지/종료할 수 있어야 한다.
- 사용자가 브라우저를 벗어나면(focus out) 타이머는 자동으로 일시정지 되어야 한다.
- 사용자는 목표 시간 내 완료하지 못할 경우 알림을 받아야 한다.

### 👨‍💻 5. 개발자 면접 질문 기능

- 사용자는 예상 면접 질문을 랜덤하게 받을 수 있어야 한다.
- 각 질문에 대해 사용자가 직접 답변을 입력하고, 모든 답변 후 모범 답안을 확인할 수 있어야 한다.
- 질문은 카테고리와 난이도(상/중/하) 기준으로 구성되며, UI에서 선택할 수 있어야 한다.

### 👨‍💻 6. 스트레칭 알림 기능

- 사용자는 스트레칭 알림을 받을 수 있는 시간을 선택할 수 있어야 한다.
- 사용자는 스트레칭 컨텐츠를 동영상이나 이미지로 확인할 수 있어야 한다.

### 📊 7. 마이페이지

- 사용자는 자신의 총 순공 시간, 카테고리 별 학습 시간 통계를 확인할 수 있어야 한다.
- 사용자는 데일리 루틴 완료 비율을 전체/카테고리/일자 기준으로 확인할 수 있어야 한다
- 사용자 정보(비밀번호, 닉네임 등)를 수정할 수 있어야 한다.
- 통계 데이터는 1개월 단위로 저장되며, 매일 갱신된다. (이전 데이터는 자동 제거)

### 🤖 8. LLM 챗봇 플래너

- 사용자는 키워드를 선택하여 LLM 챗봇을 이용할 수 있어야 한다.
- 사용자는 모든 페이지에서 LLM 챗봇에 접근할 수 있어야 한다.

---

### 🧑‍💼 관리자 요구사항

### 📝 1. 면접 질문 관리

- 관리자는 면접 질문을 카테고리 및 난이도를 포함하여 등록하고 질문을 수정, 삭제할 수 있어야 한다.

### 🎥 2. 콘텐츠 관리

- 관리자는 이미지 또는 영상 형태의 스트레칭 콘텐츠를 등록/수정/삭제할 수 있어야 한다.
- 영상은 iframe 형식으로 링크 삽입될 수 있어야 하며, 콘텐츠 타입은 구분되어야 한다.

### 📈 사용자 활동 통계 관리

- 관리자는 사용자 접속 횟수, 시간대 등 로그 데이터를 수집하고 시각화할 수 있어야 한다.
- 로그인 시 사용자 ID 및 시간을 기록하며, 접속 빈도/시간대별 통계를 확인할 수 있어야 한다.

---
# UI Flow Chart

<img width="3952" alt="2차 프로젝트 (3)" src="https://github.com/user-attachments/assets/c56814ad-0477-429e-b6ec-dab665efe06d" />

---
# API 명세서
## 🌐 [0] 초기화면 / 인증

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| 메인화면 (로고+시작하기 버튼) | `/` | `/templates/common/main.html` | GET |
| 로그인 페이지 | `/login` | `/templates/user/login.html` | GET |
| 로그인 처리 | `/login` | redirect:`/routines` | POST |
| 회원가입 폼 이동 | `/signup` | `/templates/user/signup.html` | GET |
| 회원가입 처리 | `/signup` | redirect:`/login` | POST |
| 로그아웃 | `/logout` | redirect:`/` | POST |

---

## ✅ [1] TODO 기능

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| TODO 목록 조회 | `/todos` | `/templates/todo/todo-list.html` | GET |
| TODO 추가 폼 | `/todos/new` | `/templates/todo/todo-form.html` | GET |
| TODO 추가 | `/todos` | redirect:`/todos` | POST |
| TODO 수정 폼 | `/todos/{id}/edit` | `/templates/todo/todo-form.html` | GET |
| TODO 수정 | `/todos/{id}` | redirect:`/todos` | PATCH |
| TODO 삭제 | `/todos/{id}` | redirect:`/todos` | DELETE |
| TODO 완료 | `/todos/{id}/complete` | redirect:`/todos` | PATCH |
| TODO 완료 취소 | `/todos/{id}/cancel` | redirect:`/todos` | PATCH |

---

## ✅ [2] 데일리 루틴 + 타이머

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| 루틴 목록 조회 | `/routines` | `/templates/routine/routine-list.html` | GET |
| 루틴 추가 폼 모달 | `/routines/new` | modal in `/routine-list.html` | GET |
| 루틴 추가 | `/routines` | redirect:`/routines` | POST |
| 루틴 수정 폼 모달 | `/routines/{id}/edit` | modal in `/routine-list.html` | GET |
| 루틴 수정 | `/routines/{id}` | redirect:`/routines` | PATCH |
| 루틴 삭제 | `/routines/{id}` | redirect:`/routines` | DELETE |
| 루틴 완료 | `/routines/{id}/complete` | redirect:`/routines` | PATCH |
| 루틴 완료 취소 | `/routines/{id}/cancel` | redirect:`/routines` | PATCH |
| 루틴 쉬어가기(스킵) | `/routines/{id}/skip` | redirect:`/routines` | PATCH |
| 루틴 타이머 페이지 진입 + 즉시 시작 | `/routines/{id}/timer` | `/templates/routine/timer.html` | GET |
| 타이머 일시정지 | `/timer/pause` | JS 처리 (AJAX) | PATCH |
| 타이머 완료 수동 클릭 | `/timer/complete` | redirect:`/routines` | PATCH |

---

## ✅ [3] 마이 통계 + 환경설정

### 📊 마이 통계

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| 마이 통계 페이지 | `/mypage/stats` | `/templates/mypage/stats.html` | GET |

---

### ⚙️ 환경 설정

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| 환경 설정 페이지 | `/settings` | `/templates/mypage/settings.html` | GET |
| 닉네임 수정 | `/settings/nickname` | redirect:`/settings` | PATCH |
| 비밀번호 수정 | `/settings/password` | redirect:`/settings` | PATCH |
| 전체 알림 설정 (On/Off) | `/settings/notifications` | redirect:`/settings` | PATCH |

---

## ✅ [4] 면접 질문 기능

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| 면접 질문 카테고리 선택 | `/interview/select` | `/templates/interview/interview-select.html` | GET |
| 질문 랜덤 제공 | `/interview/question` | `/templates/interview/interview-random.html` | GET |
| 사용자 답변 제출 | `/interview/answer` | `/templates/interview/interview-answer.html` | POST |
| 모범 답안 확인 | `/interview/result` | `/templates/interview/interview-result.html` | GET |

---

## ✅ [5] 챗봇 (LLM)

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| 챗봇 모달 호출 | `/chatbot` | modal in 모든 페이지 | GET |
| 챗봇 대화 전송 | `/chatbot/message` | JS (AJAX) | POST |

---

## ✅ [6] 알림 기능

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| TODO/루틴 알림 등록 | `/alarm/{type}/{id}` | 없음 (JS 비동기) | POST |
| 알림 일괄 설정 On/Off (환경설정 내) | `/settings/notifications` | redirect:`/settings` | PATCH |

---

## ✅ [7] 관리자 기능

### 👥 회원 관리

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| 회원 목록 조회 (기본 진입화면) | `/admin/members` | `/templates/admin/manage-members.html` | GET |
| 회원 수정 폼 표시 | `/admin/members/{id}/edit` | modal in `/manage-members.html` | GET |
| 회원 수정 | `/admin/members/{id}` | redirect:`/admin/members` | PATCH |
| 회원 삭제 | `/admin/members/{id}` | confirm modal → redirect | DELETE |

---

### 📚 컨텐츠 관리 (면접 질문)

| 이름 | Controller URL | View URL | 메서드 |
| --- | --- | --- | --- |
| 질문 목록 조회 | `/admin/questions` | `/templates/admin/manage-questions.html` | GET |
| 질문 등록 | `/admin/questions` | redirect:`/admin/questions` | POST |
| 질문 수정 | `/admin/questions/{id}` | redirect:`/admin/questions` | PATCH |
| 질문 삭제 | `/admin/questions/{id}` | confirm modal → redirect | DELETE |

---
# ERD
![image (8)](https://github.com/user-attachments/assets/b4bdb409-dcf2-45c1-a823-c2345000e7c1)

---
# 역할 분담

### 👤 **유강현 - TODO + 푸시 알림**

- **폴더**: `todo/`, `alarm/`
- **담당 기능**:
    - 할 일 추가 / 수정 / 삭제 (U-00)
    - 할 일 조회 (U-01)
    - 할 일 완료 및 완료 취소 (U-02)
    - 완료된 할 일은 하단 회색 구분 (U-04)
    - 알림 설정 On/Off (U-05)
    - 할 일 시작 10분 전 알림 (U-06)
    - 알림 방식: 화면 점멸 + 음성 (U-07)

---

### 👤 **윤동현 - 데일리 루틴 + 타이머**

- **폴더**: `routine/`
- **담당 기능**:
    - 루틴 추가 / 수정 / 삭제 (U-08)
    - 루틴 조회 (U-09)
    - 포모도로 타이머 기반 루틴 완료 (U-10)
    - 루틴 완료 취소 (U-12)
    - 루틴 쉬어가기 전환 (U-13)
    - 타이머: 시작 / 일시정지 / 종료 (U-15)
    - ~~타이머 focus out 시 자동 일시정지 (U-16)~~
    - 목표 시간 미완 시 알림 (U-17)

---

### 👤 **정서윤 - 면접 질문 + 관리자 기능**

- **폴더**: `interview/`, `admin/`
- **담당 기능**:
    - 예상 면접 질문 랜덤 출력 (U-18)
    - 질문별 사용자 답변 입력 + 모범답안 확인 (U-19)
    - 질문 카테고리/난이도 선택 가능 (U-20)
    - 관리자 면접 질문 관리 (A-00)
    - 프론트엔드드

---

### 👤 **최대열 - 마이페이지 + 통계 + 챗봇**

- **폴더**: `mypage/`, `chatbot/`, `admin/`
- **담당 기능**:
    - 총 순공 시간, 카테고리별 학습 시간 통계 (U-23)
    - 데일리 루틴 완료 비율 (U-24)
    - 사용자 정보 수정 (U-25)
    - ~~통계 1개월 단위 저장 및 갱신 (U-26)~~
    - LLM 챗봇 키워드 기반 이용 (U-27)
    - 모든 페이지에서 챗봇 접근 가능 (U-28)
 
---
# 와이어 프레임
https://www.figma.com/design/WX8o32DITdAjKM6mPDytDv/2%EC%B0%A8-%ED%8C%80%ED%94%8C-%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84?node-id=0-1&p=f&t=R4i3D9VpURoJkVGl-0


