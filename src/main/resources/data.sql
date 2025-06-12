
-- 2. 데일리 루틴 데이터 생성
-- 사용자 1: 프론트엔드 취준생 (2개 완료, 1개 진행중, 1개 쉬어가기)
INSERT INTO daily_routines (user_id, category, title, description, status, focus_time, break_time, started_at, completed_at, is_deleted, created_at, updated_at)
VALUES
    (1, '알고리즘', 'JavaScript 알고리즘 문제 풀기', '프로그래머스 Level 2 문제 3개 풀기', 'COMPLETED', 60, 10, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 1 HOUR, false, NOW() - INTERVAL 1 DAY, NOW()),
    (1, 'HTML/CSS', '반응형 웹 디자인 학습', 'Media Query 활용 실습', 'COMPLETED', 45, 5, NOW() - INTERVAL 5 HOUR, NOW() - INTERVAL 4 HOUR, false, NOW() - INTERVAL 2 DAY, NOW()),
    (1, 'React', 'React Hooks 연습', 'useState, useEffect 활용한 미니 프로젝트', 'ACTIVE', 90, 15, NULL, NULL, false, NOW(), NOW()),
    (1, 'JavaScript', 'ES6+ 문법 학습', 'Arrow Function, Destructuring 학습', 'PASS', 60, 10, NULL, NULL, false, NOW() - INTERVAL 3 DAY, NOW());

-- 사용자 2: 백엔드 취준생 (3개 완료, 2개 진행중)
INSERT INTO daily_routines (user_id, category, title, description, status, focus_time, break_time, started_at, completed_at, is_deleted, created_at, updated_at)
VALUES
    (2, 'Java', 'Java 스트림 API 학습', '람다와 스트림 실습', 'COMPLETED', 70, 10, NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 2 HOUR, false, NOW() - INTERVAL 1 DAY, NOW()),
    (2, 'Spring', 'Spring Security 구현', '인증/인가 기능 구현 연습', 'COMPLETED', 120, 20, NOW() - INTERVAL 6 HOUR, NOW() - INTERVAL 4 HOUR, false, NOW() - INTERVAL 2 DAY, NOW()),
    (2, 'DB', 'MySQL 인덱스 최적화', '쿼리 성능 개선 실습', 'COMPLETED', 60, 5, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 23 HOUR, false, NOW() - INTERVAL 3 DAY, NOW()),
    (2, 'JPA', 'JPA 연관관계 매핑 학습', 'OneToMany, ManyToMany 실습', 'ACTIVE', 90, 15, NOW() - INTERVAL 30 MINUTE, NULL, false, NOW(), NOW()),
    (2, '알고리즘', '그래프 알고리즘 구현', 'DFS/BFS 구현 연습', 'ACTIVE', 120, 20, NULL, NULL, false, NOW(), NOW());

-- 사용자 3: 풀스택 개발자 (4개 완료, 1개 진행중, 1개 쉬어가기)
INSERT INTO daily_routines (user_id, category, title, description, status, focus_time, break_time, started_at, completed_at, is_deleted, created_at, updated_at)
VALUES
    (3, 'Node.js', 'Express 서버 구현', 'REST API 개발 실습', 'COMPLETED', 90, 15, NOW() - INTERVAL 4 HOUR, NOW() - INTERVAL 2.5 HOUR, false, NOW() - INTERVAL 1 DAY, NOW()),
    (3, 'React', '리액트 컴포넌트 설계', '재사용 가능한 컴포넌트 개발', 'COMPLETED', 75, 10, NOW() - INTERVAL 7 HOUR, NOW() - INTERVAL 6 HOUR, false, NOW() - INTERVAL 2 DAY, NOW()),
    (3, 'TypeScript', 'TypeScript 타입 정의', '제네릭 활용 실습', 'COMPLETED', 60, 5, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 23 HOUR, false, NOW() - INTERVAL 3 DAY, NOW()),
    (3, 'Docker', '도커 컨테이너 배포', '멀티 컨테이너 애플리케이션 배포', 'COMPLETED', 120, 20, NOW() - INTERVAL 2.5 DAY, NOW() - INTERVAL 2.3 DAY, false, NOW() - INTERVAL 4 DAY, NOW()),
    (3, 'CS', '네트워크 기초 학습', 'TCP/IP 프로토콜 공부', 'ACTIVE', 90, 15, NOW() - INTERVAL 1 HOUR, NULL, false, NOW(), NOW()),
    (3, 'DB', 'NoSQL 학습', 'MongoDB 기본 CRUD 연습', 'PASS', 60, 10, NULL, NULL, false, NOW() - INTERVAL 2 DAY, NOW());
-- 3. 포모도로 세션 데이터 생성
-- 사용자 1의 포모도로 세션
INSERT INTO pomodoro_sessions (routine_id, duration_minutes, started_at, ended_at)
VALUES
    (1, 25, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 1 HOUR - INTERVAL 35 MINUTE),
    (1, 25, NOW() - INTERVAL 1 HOUR - INTERVAL 30 MINUTE, NOW() - INTERVAL 1 HOUR - INTERVAL 5 MINUTE),
    (1, 10, NOW() - INTERVAL 1 HOUR, NOW() - INTERVAL 1 HOUR + INTERVAL 10 MINUTE),
    (2, 25, NOW() - INTERVAL 5 HOUR, NOW() - INTERVAL 4 HOUR - INTERVAL 35 MINUTE),
    (2, 20, NOW() - INTERVAL 4 HOUR - INTERVAL 30 MINUTE, NOW() - INTERVAL 4 HOUR - INTERVAL 10 MINUTE),
    (3, 25, NOW() - INTERVAL 30 MINUTE, NULL);

-- 사용자 2의 포모도로 세션
INSERT INTO pomodoro_sessions (routine_id, duration_minutes, started_at, ended_at)
VALUES
    (5, 25, NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 2 HOUR - INTERVAL 35 MINUTE),
    (5, 25, NOW() - INTERVAL 2 HOUR - INTERVAL 30 MINUTE, NOW() - INTERVAL 2 HOUR - INTERVAL 5 MINUTE),
    (5, 20, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 2 HOUR + INTERVAL 20 MINUTE),
    (6, 25, NOW() - INTERVAL 6 HOUR, NOW() - INTERVAL 5 HOUR - INTERVAL 35 MINUTE),
    (6, 25, NOW() - INTERVAL 5 HOUR - INTERVAL 30 MINUTE, NOW() - INTERVAL 5 HOUR - INTERVAL 5 MINUTE),
    (6, 25, NOW() - INTERVAL 5 HOUR, NOW() - INTERVAL 4 HOUR - INTERVAL 35 MINUTE),
    (6, 25, NOW() - INTERVAL 4 HOUR - INTERVAL 30 MINUTE, NOW() - INTERVAL 4 HOUR - INTERVAL 5 MINUTE),
    (6, 20, NOW() - INTERVAL 4 HOUR, NOW() - INTERVAL 4 HOUR + INTERVAL 20 MINUTE),
    (7, 25, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY + INTERVAL 25 MINUTE),
    (7, 25, NOW() - INTERVAL 1 DAY - INTERVAL 30 MINUTE, NOW() - INTERVAL 23 HOUR - INTERVAL 5 MINUTE),
    (7, 10, NOW() - INTERVAL 23 HOUR, NOW() - INTERVAL 23 HOUR + INTERVAL 10 MINUTE),
    (8, 25, NOW() - INTERVAL 30 MINUTE, NULL);

-- 사용자 3의 포모도로 세션
INSERT INTO pomodoro_sessions (routine_id, duration_minutes, started_at, ended_at)
VALUES
    (10, 25, NOW() - INTERVAL 4 HOUR, NOW() - INTERVAL 3 HOUR - INTERVAL 35 MINUTE),
    (10, 25, NOW() - INTERVAL 3 HOUR - INTERVAL 30 MINUTE, NOW() - INTERVAL 3 HOUR - INTERVAL 5 MINUTE),
    (10, 25, NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 2 HOUR - INTERVAL 35 MINUTE),
    (10, 15, NOW() - INTERVAL 2 HOUR - INTERVAL 30 MINUTE, NOW() - INTERVAL 2 HOUR - INTERVAL 15 MINUTE),
    (11, 25, NOW() - INTERVAL 7 HOUR, NOW() - INTERVAL 6 HOUR - INTERVAL 35 MINUTE),
    (11, 25, NOW() - INTERVAL 6 HOUR - INTERVAL 30 MINUTE, NOW() - INTERVAL 6 HOUR - INTERVAL 5 MINUTE),
    (11, 25, NOW() - INTERVAL 6 HOUR, NOW() - INTERVAL 6 HOUR + INTERVAL 25 MINUTE),
    (12, 25, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY + INTERVAL 25 MINUTE),
    (12, 25, NOW() - INTERVAL 1 DAY - INTERVAL 30 MINUTE, NOW() - INTERVAL 23 HOUR - INTERVAL 5 MINUTE),
    (12, 10, NOW() - INTERVAL 23 HOUR, NOW() - INTERVAL 23 HOUR + INTERVAL 10 MINUTE),
    (13, 25, NOW() - INTERVAL 2.5 DAY, NOW() - INTERVAL 2.5 DAY + INTERVAL 25 MINUTE),
    (13, 25, NOW() - INTERVAL 2.5 DAY - INTERVAL 30 MINUTE, NOW() - INTERVAL 2.4 DAY - INTERVAL 5 MINUTE),
    (13, 25, NOW() - INTERVAL 2.4 DAY, NOW() - INTERVAL 2.4 DAY + INTERVAL 25 MINUTE),
    (13, 25, NOW() - INTERVAL 2.4 DAY - INTERVAL 30 MINUTE, NOW() - INTERVAL 2.3 DAY - INTERVAL 5 MINUTE),
    (13, 20, NOW() - INTERVAL 2.3 DAY, NOW() - INTERVAL 2.3 DAY + INTERVAL 20 MINUTE),
    (14, 25, NOW() - INTERVAL 1 HOUR, NULL);

-- 4. Todo 항목 데이터 생성
-- 사용자 1의 Todo 항목
INSERT INTO todos (user_id, title, description, start_time, is_completed, completed_at, is_deleted, created_at, updated_at)
VALUES
    (1, '이력서 작성', '프론트엔드 개발자 포지션에 맞게 이력서 업데이트', NOW() - INTERVAL 2 DAY + INTERVAL 10 HOUR, true, NOW() - INTERVAL 2 DAY + INTERVAL 12 HOUR, false, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 2 DAY),
    (1, 'React 포트폴리오 개선', '프로젝트에 Redux 추가하고 README 업데이트', NOW() - INTERVAL 1 DAY + INTERVAL 14 HOUR, true, NOW() - INTERVAL 1 DAY + INTERVAL 16 HOUR, false, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY),
    (1, 'JavaScript 코딩 테스트 연습', '프로그래머스 Level 2 문제 5개 풀기', NOW() + INTERVAL 2 HOUR, false, NOW() + INTERVAL 4 HOUR, false, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),
    (1, 'CSS 프레임워크 비교 분석', 'Tailwind, Bootstrap, Material UI 장단점 정리', NOW() + INTERVAL 1 DAY + INTERVAL 10 HOUR, false, NOW() + INTERVAL 1 DAY + INTERVAL 12 HOUR, false, NOW(), NOW());

-- 사용자 2의 Todo 항목
INSERT INTO todos (user_id, title, description, start_time, is_completed, completed_at, is_deleted, created_at, updated_at)
VALUES
    (2, 'Spring 프로젝트 리팩토링', '컨트롤러 계층 개선 및 서비스 로직 분리', NOW() - INTERVAL 3 DAY + INTERVAL 9 HOUR, true, NOW() - INTERVAL 3 DAY + INTERVAL 11 HOUR, false, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 3 DAY),
    (2, 'SQL 성능 최적화', '인덱스 추가 및 쿼리 개선', NOW() - INTERVAL 1 DAY + INTERVAL 13 HOUR, true, NOW() - INTERVAL 1 DAY + INTERVAL 15 HOUR, false, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY),
    (2, 'JPA 연관관계 매핑 실습', 'OneToMany, ManyToMany 관계 구현 연습', NOW() - INTERVAL 5 HOUR, true, NOW() - INTERVAL 3 HOUR, false, NOW() - INTERVAL 1 DAY, NOW()),
    (2, '코딩 테스트 준비', '백준 알고리즘 문제 5개 풀기', NOW() + INTERVAL 3 HOUR, false, NOW() + INTERVAL 5 HOUR, false, NOW(), NOW()),
    (2, '자기소개서 작성', '백엔드 개발자 포지션 지원용 자기소개서 작성', NOW() + INTERVAL 1 DAY + INTERVAL 9 HOUR, false, NOW() + INTERVAL 1 DAY + INTERVAL 11 HOUR, false, NOW(), NOW());

-- 사용자 3의 Todo 항목
INSERT INTO todos (user_id, title, description, start_time, is_completed, completed_at, is_deleted, created_at, updated_at)
VALUES
    (3, 'MERN 스택 프로젝트 시작', '풀스택 프로젝트 기본 구조 세팅', NOW() - INTERVAL 4 DAY + INTERVAL 10 HOUR, true, NOW() - INTERVAL 4 DAY + INTERVAL 12 HOUR, false, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 4 DAY),
    (3, 'AWS 배포 환경 구축', 'EC2, RDS 설정 및 CI/CD 파이프라인 구축', NOW() - INTERVAL 2 DAY + INTERVAL 14 HOUR, true, NOW() - INTERVAL 2 DAY + INTERVAL 16 HOUR, false, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 2 DAY),
    (3, 'TypeScript 리팩토링', '기존 JS 프로젝트 TypeScript로 변환', NOW() - INTERVAL 1 DAY + INTERVAL 9 HOUR, true, NOW() - INTERVAL 1 DAY + INTERVAL 11 HOUR, false, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY),
    (3, '기술 면접 준비', '네트워크/OS/DB 주요 개념 복습', NOW() + INTERVAL 2 HOUR, false, NOW() + INTERVAL 4 HOUR, false, NOW() - INTERVAL 1 DAY, NOW()),
    (3, '포트폴리오 웹사이트 개선', '반응형 디자인 적용 및 프로젝트 추가', NOW() + INTERVAL 1 DAY + INTERVAL 13 HOUR, false, NOW() + INTERVAL 1 DAY + INTERVAL 15 HOUR, false, NOW(), NOW()),
    (3, '오픈 소스 기여하기', '관심 있는 GitHub 프로젝트에 PR 생성', NOW() + INTERVAL 2 DAY + INTERVAL 10 HOUR, false, NOW() + INTERVAL 2 DAY + INTERVAL 12 HOUR, false, NOW(), NOW());

-- 사용자 면접 질문
INSERT INTO interview_questions (question_text, answer_text, category, difficulty)
VALUES
-- 알고리즘
('퀵 정렬(Quick Sort)의 시간복잡도와 작동 원리를 설명해주세요.', '퀵 정렬은 기준값을 기준으로 작은 값과 큰 값으로 나눈 뒤 재귀적으로 정렬하며, 평균 시간복잡도는 O(n log n)입니다.', '알고리즘', '상'),
('DFS와 BFS의 차이점을 설명해주세요.', 'DFS는 깊이 우선 탐색으로 스택 또는 재귀를, BFS는 너비 우선 탐색으로 큐를 사용합니다.', '알고리즘', '중'),
('이진 탐색(Binary Search)의 전제 조건과 시간복잡도는?', '정렬된 배열에서만 사용 가능하며, 시간복잡도는 O(log n)입니다.', '알고리즘', '하'),
('다익스트라 알고리즘은 어떤 문제에 사용되나요?', '가중치가 있는 그래프에서 최단 경로를 구할 때 사용됩니다.', '알고리즘', '상'),
('조합과 순열의 차이점은?', '순열은 순서를 고려하고, 조합은 순서를 고려하지 않습니다.', '알고리즘', '하'),

-- 자료구조
('스택과 큐의 차이점은 무엇인가요?', '스택은 LIFO, 큐는 FIFO 구조입니다.', '자료구조', '하'),
('해시 충돌은 어떻게 해결하나요?', '체이닝과 개방 주소법 같은 방법을 사용합니다.', '자료구조', '중'),
('이진 트리와 이진 탐색 트리의 차이는?', '이진 탐색 트리는 왼쪽은 작은 값, 오른쪽은 큰 값을 가집니다.', '자료구조', '중'),
('인접 리스트와 인접 행렬의 차이는?', '인접 리스트는 메모리를 절약하고, 인접 행렬은 접근이 빠릅니다.', '자료구조', '상'),
('우선순위 큐는 어떤 자료구조로 구현하나요?', '보통 힙 구조로 구현합니다.', '자료구조', '상'),

-- 데이터베이스
('정규화의 목적은 무엇인가요?', '데이터 중복 제거와 무결성 유지입니다.', '데이터베이스', '중'),
('트랜잭션과 ACID란?', '트랜잭션은 하나의 작업 단위이고, ACID는 원자성, 일관성, 고립성, 지속성을 의미합니다.', '데이터베이스', '중'),
('인덱스의 장단점은?', '검색은 빠르지만, 저장 공간과 쓰기 성능에 영향을 줍니다.', '데이터베이스', '상'),
('JOIN의 종류에는 어떤 것이 있나요?', 'INNER, LEFT, RIGHT, FULL JOIN 등이 있습니다.', '데이터베이스', '하'),
('SQL Injection이란?', '쿼리 조작을 통한 공격이며, PreparedStatement로 방지할 수 있습니다.', '데이터베이스', '상'),

-- 운영체제
('프로세스와 스레드의 차이는?', '프로세스는 독립된 실행 단위, 스레드는 프로세스 내 작업 단위입니다.', '운영체제', '중'),
('교착 상태란?', '자원을 점유한 채 서로 대기하는 상태로, 예방 및 회피 전략이 있습니다.', '운영체제', '상'),
('컨텍스트 스위칭이란?', 'CPU가 다른 프로세스로 전환할 때의 저장 및 복원 작업입니다.', '운영체제', '중'),
('CPU 스케줄링 알고리즘에는 어떤 것이 있나요?', 'FCFS, SJF, RR, Priority 등이 있습니다.', '운영체제', '상'),
('세마포어는 무엇인가요?', '공유 자원 접근을 제어하기 위한 동기화 도구입니다.', '운영체제', '하'),

-- 네트워크
('HTTP와 HTTPS의 차이는?', 'HTTPS는 SSL을 통해 암호화되어 보안이 강화됩니다.', '네트워크', '하'),
('TCP와 UDP의 차이는?', 'TCP는 신뢰성 보장, UDP는 빠르지만 비신뢰성입니다.', '네트워크', '중'),
('3-way handshake란?', 'TCP 연결 설정을 위한 SYN, SYN-ACK, ACK 절차입니다.', '네트워크', '중'),
('DNS란?', '도메인을 IP 주소로 변환하는 시스템입니다.', '네트워크', '하'),
('OSI 7계층은 무엇인가요?', '통신을 7단계로 나눈 모델로, 계층별 역할이 나뉩니다.', '네트워크', '상'),

-- 컴퓨터구조
('캐시 메모리란?', 'CPU와 메인 메모리 사이의 고속 메모리입니다.', '컴퓨터구조', '중'),
('파이프라이닝이란?', '명령어를 분할하여 병렬로 처리하는 방식입니다.', '컴퓨터구조', '상'),
('레지스터란?', 'CPU 내부의 고속 임시 저장장치입니다.', '컴퓨터구조', '하'),
('RISC와 CISC의 차이는?', 'RISC는 단순 명령어, CISC는 복잡 명령어를 사용합니다.', '컴퓨터구조', '중'),
('명령어 사이클이란?', '명령어 인출, 해석, 실행의 반복 과정입니다.', '컴퓨터구조', '상');