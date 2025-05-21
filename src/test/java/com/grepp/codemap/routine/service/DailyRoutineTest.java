package com.grepp.codemap.routine.service;

import com.grepp.codemap.routine.domain.DailyRoutine;
import com.grepp.codemap.routine.domain.PomodoroSession;
import com.grepp.codemap.routine.dto.DailyRoutineDto;
import com.grepp.codemap.routine.dto.PomodoroSessionDto;
import com.grepp.codemap.routine.repository.DailyRoutineRepository;
import com.grepp.codemap.routine.repository.PomodoroSessionRepository;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스를 클래스당 하나만 생성
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트 메서드 순서 지정
public class DailyRoutineTest {

    @Autowired
    private DailyRoutineService dailyRoutineService;

    @Autowired
    private DailyRoutineRepository dailyRoutineRepository;

    @Autowired
    private PomodoroSessionRepository pomodoroSessionRepository;

    @Autowired
    private UserRepository userRepository;

    // 테스트 간 공유할 데이터
    private User testUser;
    private Long userId;
    private Long routineId;
    private Long sessionId;

    @BeforeAll
    void setUp() {
        // 고유한 이메일 생성 (중복 방지)
        String uniqueEmail = "test-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        // 테스트용 사용자 생성 및 저장
        testUser = User.builder()
            .email(uniqueEmail)
            .password("password")
            .nickname("테스트 사용자")
            .role("USER")
            .isActive(true)
            .build();

        testUser = userRepository.save(testUser);
        userId = testUser.getId();

        System.out.println("\n==================================================");
        System.out.println("테스트 사용자 생성 완료");
        System.out.println("사용자 ID: " + userId);
        System.out.println("사용자 이메일: " + uniqueEmail);
        System.out.println("==================================================\n");
    }

    @Test
    @Order(1)
    @DisplayName("1. 루틴 추가 테스트 - 데이터베이스에 새 루틴이 추가됩니다")
    void createRoutineTest() {
        // given
        DailyRoutineDto routineDto = DailyRoutineDto.builder()
            .title("알고리즘 문제 풀기")
            .description("프로그래머스 Level 2 문제 3개 풀기")
            .category("알고리즘")
            .focusTime(45) // 45분
            .build();

        // when - 이 부분이 DB에 데이터를 추가합니다
        DailyRoutineDto savedRoutineDto = dailyRoutineService.createRoutine(routineDto, userId);
        routineId = savedRoutineDto.getId(); // 다음 테스트에서 사용하기 위해 저장

        // then - 여기서는 결과만 확인
        System.out.println("\n==================================================");
        System.out.println("1. 루틴 추가 완료");
        System.out.println("루틴 ID: " + routineId);
        System.out.println("제목: " + savedRoutineDto.getTitle());
        System.out.println("설명: " + savedRoutineDto.getDescription());
        System.out.println("카테고리: " + savedRoutineDto.getCategory());
        System.out.println("집중 시간: " + savedRoutineDto.getFocusTime() + "분");
        System.out.println("==================================================");
        System.out.println("MySQL에서 다음 쿼리로 확인해보세요:");
        System.out.println("SELECT * FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("==================================================\n");
    }

    @Test
    @Order(2)
    @DisplayName("2. 루틴 수정 테스트 - 데이터베이스의 루틴 정보가 수정됩니다")
    void updateRoutineTest() {
        // given
        DailyRoutineDto updateDto = DailyRoutineDto.builder()
            .title("고급 알고리즘 학습")
            .description("그래프 알고리즘 심화 학습")
            .category("알고리즘")
            .focusTime(60) // 60분으로 수정
            .build();

        // when - 이 부분이 DB의 데이터를 수정합니다
        DailyRoutineDto updatedRoutineDto = dailyRoutineService.updateRoutine(routineId, updateDto, userId);

        // then - 여기서는 결과만 확인
        System.out.println("\n==================================================");
        System.out.println("2. 루틴 수정 완료");
        System.out.println("루틴 ID: " + routineId + " (동일한 ID의 루틴이 수정됨)");
        System.out.println("수정된 제목: " + updatedRoutineDto.getTitle());
        System.out.println("수정된 설명: " + updatedRoutineDto.getDescription());
        System.out.println("수정된 집중 시간: " + updatedRoutineDto.getFocusTime() + "분");
        System.out.println("==================================================");
        System.out.println("MySQL에서 다음 쿼리로 확인해보세요:");
        System.out.println("SELECT * FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("==================================================\n");
    }

    @Test
    @Order(3)
    @DisplayName("3. 루틴 완료 테스트 - 데이터베이스의 루틴 상태가 COMPLETED로 변경됩니다")
    void completeRoutineTest() {
        // when - 이 부분이 DB의 루틴 상태를 COMPLETED로 변경합니다
        DailyRoutineDto completedRoutineDto = dailyRoutineService.completeRoutine(routineId, userId);

        // then - 여기서는 결과만 확인
        System.out.println("\n==================================================");
        System.out.println("3. 루틴 완료 처리 완료");
        System.out.println("루틴 ID: " + routineId);
        System.out.println("변경된 상태: " + completedRoutineDto.getStatus());
        System.out.println("완료 시간: " + completedRoutineDto.getCompletedAt());
        System.out.println("==================================================");
        System.out.println("MySQL에서 다음 쿼리로 확인해보세요:");
        System.out.println("SELECT id, title, status, completed_at FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("==================================================\n");
    }

    @Test
    @Order(4)
    @DisplayName("4. 루틴 완료 취소 테스트 - 데이터베이스의 루틴 상태가 ACTIVE로 변경됩니다")
    void cancelCompletionTest() {
        // when - 이 부분이 DB의 루틴 상태를 다시 ACTIVE로 변경합니다
        DailyRoutineDto canceledRoutineDto = dailyRoutineService.cancelCompletion(routineId, userId);

        // then - 여기서는 결과만 확인
        System.out.println("\n==================================================");
        System.out.println("4. 루틴 완료 취소 처리 완료");
        System.out.println("루틴 ID: " + routineId);
        System.out.println("변경된 상태: " + canceledRoutineDto.getStatus());
        System.out.println("완료 시간: " + (canceledRoutineDto.getCompletedAt() == null ? "null (삭제됨)" : canceledRoutineDto.getCompletedAt()));
        System.out.println("==================================================");
        System.out.println("MySQL에서 다음 쿼리로 확인해보세요:");
        System.out.println("SELECT id, title, status, completed_at FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("==================================================\n");
    }

    @Test
    @Order(5)
    @DisplayName("5. 루틴 쉬어가기 테스트 - 데이터베이스의 루틴 상태가 PASS로 변경됩니다")
    void passRoutineTest() {
        // when - 이 부분이 DB의 루틴 상태를 PASS로 변경합니다
        DailyRoutineDto passedRoutineDto = dailyRoutineService.passRoutine(routineId, userId);

        // then - 여기서는 결과만 확인
        System.out.println("\n==================================================");
        System.out.println("5. 루틴 쉬어가기 처리 완료");
        System.out.println("루틴 ID: " + routineId);
        System.out.println("변경된 상태: " + passedRoutineDto.getStatus());
        System.out.println("==================================================");
        System.out.println("MySQL에서 다음 쿼리로 확인해보세요:");
        System.out.println("SELECT id, title, status FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("==================================================\n");
    }

    @Test
    @Order(6)
    @DisplayName("6. 루틴 다시 활성화 테스트 - 포모도로 테스트를 위해 루틴 상태가 ACTIVE로 변경됩니다")
    void reactivateRoutineTest() {
        // when - 이 부분이 DB의 루틴 상태를 다시 ACTIVE로 변경합니다
        DailyRoutineDto reactivatedDto = dailyRoutineService.cancelCompletion(routineId, userId);

        // then - 여기서는 결과만 확인
        System.out.println("\n==================================================");
        System.out.println("6. 루틴 재활성화 처리 완료");
        System.out.println("루틴 ID: " + routineId);
        System.out.println("변경된 상태: " + reactivatedDto.getStatus());
        System.out.println("==================================================");
        System.out.println("MySQL에서 다음 쿼리로 확인해보세요:");
        System.out.println("SELECT id, title, status FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("==================================================\n");
    }

    @Test
    @Order(7)
    @DisplayName("7. 포모도로 세션 시작 테스트 - 데이터베이스에 새 포모도로 세션이 추가됩니다")
    void startPomodoroSessionTest() {
        // when - 이 부분이 DB에 포모도로 세션을 추가합니다
        PomodoroSessionDto sessionDto = dailyRoutineService.startPomodoroSession(routineId, userId);
        sessionId = sessionDto.getId(); // 다음 테스트에서 사용하기 위해 저장

        // then - 여기서는 결과만 확인
        System.out.println("\n==================================================");
        System.out.println("7. 포모도로 세션 시작 완료");
        System.out.println("세션 ID: " + sessionId);
        System.out.println("루틴 ID: " + routineId);
        System.out.println("시작 시간: " + sessionDto.getStartedAt());
        System.out.println("==================================================");
        System.out.println("MySQL에서 다음 쿼리로 확인해보세요:");
        System.out.println("SELECT * FROM pomodoro_sessions WHERE id = " + sessionId + ";");
        System.out.println("SELECT id, title, started_at FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("==================================================\n");

        // 루틴의 시작 시간도 설정되었는지 확인
        Optional<DailyRoutine> foundRoutine = dailyRoutineRepository.findById(routineId);
        assertThat(foundRoutine).isPresent();
        assertThat(foundRoutine.get().getStartedAt()).isNotNull();
    }

    @Test
    @Order(8)
    @DisplayName("8. 포모도로 세션 종료 테스트 - 데이터베이스의 포모도로 세션에 종료 시간이 설정됩니다")
    void endPomodoroSessionTest() {
        // when - 이 부분이 DB의 포모도로 세션에 종료 시간을 설정합니다
        PomodoroSessionDto endedSessionDto = dailyRoutineService.endPomodoroSession(sessionId, userId);

        // then - 여기서는 결과만 확인
        System.out.println("\n==================================================");
        System.out.println("8. 포모도로 세션 종료 완료");
        System.out.println("세션 ID: " + sessionId);
        System.out.println("종료 시간: " + endedSessionDto.getEndedAt());
        System.out.println("==================================================");
        System.out.println("MySQL에서 다음 쿼리로 확인해보세요:");
        System.out.println("SELECT id, routine_id, started_at, ended_at FROM pomodoro_sessions WHERE id = " + sessionId + ";");
        System.out.println("==================================================\n");
    }

    @Test
    @Order(9)
    @DisplayName("9. 루틴 삭제 테스트 - 데이터베이스의 루틴이 소프트 딜리트됩니다")
    void deleteRoutineTest() {
        // when - 이 부분이 DB의 루틴을 소프트 딜리트합니다
        dailyRoutineService.deleteRoutine(routineId, userId);

        // then - 여기서는 결과만 확인
        System.out.println("\n==================================================");
        System.out.println("9. 루틴 삭제 처리 완료 (소프트 딜리트)");
        System.out.println("루틴 ID: " + routineId);
        System.out.println("==================================================");
        System.out.println("MySQL에서 다음 쿼리로 확인해보세요:");
        System.out.println("SELECT id, title, status, is_deleted FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("==================================================\n");

        // 활성 루틴 목록에서 제외되었는지 확인
        List<DailyRoutineDto> activeRoutines = dailyRoutineService.getActiveRoutinesByUser(userId);
        boolean containsDeletedRoutine = activeRoutines.stream()
            .anyMatch(r -> r.getId().equals(routineId));
        assertThat(containsDeletedRoutine).isFalse();
    }

    @Test
    @Order(10)
    @DisplayName("10. 테스트 정리 - 생성된 테스트 데이터 정보 출력")
    void summarizeTest() {
        System.out.println("\n==================================================");
        System.out.println("모든 테스트 완료");
        System.out.println("생성된 테스트 데이터 정보:");
        System.out.println("사용자 ID: " + userId);
        System.out.println("루틴 ID: " + routineId);
        System.out.println("세션 ID: " + sessionId);
        System.out.println("==================================================");
        System.out.println("데이터 확인 및 삭제를 위한 SQL 명령어:");
        System.out.println("-- 포모도로 세션 확인:");
        System.out.println("SELECT * FROM pomodoro_sessions WHERE id = " + sessionId + ";");
        System.out.println("-- 루틴 확인:");
        System.out.println("SELECT * FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("-- 사용자 확인:");
        System.out.println("SELECT * FROM users WHERE id = " + userId + ";");
        System.out.println();
        System.out.println("-- 데이터 삭제 (필요시):");
        System.out.println("DELETE FROM pomodoro_sessions WHERE id = " + sessionId + ";");
        System.out.println("DELETE FROM daily_routines WHERE id = " + routineId + ";");
        System.out.println("DELETE FROM users WHERE id = " + userId + ";");
        System.out.println("==================================================\n");
    }
}