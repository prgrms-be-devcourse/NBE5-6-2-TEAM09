
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