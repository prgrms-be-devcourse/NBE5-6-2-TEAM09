package com.grepp.codemap.routine.service;

import com.grepp.codemap.routine.domain.CodingTestReview;
import com.grepp.codemap.routine.domain.DailyRoutine;
import com.grepp.codemap.routine.domain.InterviewReview;
import com.grepp.codemap.routine.domain.PomodoroSession;
import com.grepp.codemap.routine.dto.CodingTestReviewDto;
import com.grepp.codemap.routine.dto.DailyRoutineDto;
import com.grepp.codemap.routine.dto.InterviewReviewDto;
import com.grepp.codemap.routine.dto.PomodoroSessionDto;
import com.grepp.codemap.routine.repository.CodingTestReviewRepository;
import com.grepp.codemap.routine.repository.DailyRoutineRepository;
import com.grepp.codemap.routine.repository.InterviewReviewRepository;
import com.grepp.codemap.routine.repository.PomodoroSessionRepository;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("DailyRoutineService 단위 테스트")
class DailyRoutineServiceTest {

    @Mock
    private DailyRoutineRepository dailyRoutineRepository;

    @Mock
    private PomodoroSessionRepository pomodoroSessionRepository;

    @Mock
    private CodingTestReviewRepository codingTestReviewRepository;

    @Mock
    private InterviewReviewRepository interviewReviewRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private DailyRoutineService dailyRoutineService;

    private User testUser;
    private DailyRoutine testRoutine;
    private DailyRoutineDto testRoutineDto;
    private PomodoroSession testSession;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .email("test@example.com")
            .nickname("테스터")
            .build();

        testRoutine = DailyRoutine.builder()
            .id(1L)
            .user(testUser)
            .category("알고리즘")
            .title("프로그래머스 Level 2")
            .description("DFS/BFS 문제 풀기")
            .focusTime(60)
            .actualFocusTime(0)
            .breakTime(10)
            .status("ACTIVE")
            .isDeleted(false)
            .build();

        testRoutineDto = DailyRoutineDto.builder()
            .category("알고리즘")
            .title("프로그래머스 Level 2")
            .description("DFS/BFS 문제 풀기")
            .focusTime(60)
            .breakTime(10)
            .build();

        testSession = PomodoroSession.builder()
            .id(1L)
            .routine(testRoutine)
            .durationMinutes(25)
            .startedAt(LocalDateTime.now())
            .build();
    }

    // === 루틴 생성 테스트 ===

    @Test
    @DisplayName("루틴 생성 성공")
    void createRoutine_Success() {
        // given
        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willReturn(testRoutine);

        // when
        DailyRoutineDto result = dailyRoutineService.createRoutine(testRoutineDto, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCategory()).isEqualTo("알고리즘");
        assertThat(result.getTitle()).isEqualTo("프로그래머스 Level 2");
        assertThat(result.getStatus()).isEqualTo("ACTIVE");

        verify(userService).getUserById(1L);
        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    @Test
    @DisplayName("루틴 생성 시 기본값 설정 확인")
    void createRoutine_DefaultValues() {
        // given
        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willAnswer(invocation -> {
            DailyRoutine routine = invocation.getArgument(0);
            return DailyRoutine.builder()
                .id(1L)
                .user(routine.getUser())
                .category(routine.getCategory())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .focusTime(routine.getFocusTime())
                .breakTime(routine.getBreakTime())
                .status(routine.getStatus())
                .isDeleted(routine.getIsDeleted())
                .build();
        });

        // when
        DailyRoutineDto result = dailyRoutineService.createRoutine(testRoutineDto, 1L);

        // then
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
        assertThat(result.getIsDeleted()).isFalse();
    }

    // === 루틴 조회 테스트 ===

    @Test
    @DisplayName("루틴 ID로 조회 성공")
    void getRoutineById_Success() {
        // given
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));

        // when
        DailyRoutineDto result = dailyRoutineService.getRoutineById(1L, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("프로그래머스 Level 2");

        verify(dailyRoutineRepository).findById(1L);
    }

    @Test
    @DisplayName("루틴 조회 실패 - 존재하지 않는 루틴")
    void getRoutineById_NotFound() {
        // given
        given(dailyRoutineRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> dailyRoutineService.getRoutineById(999L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Routine not found with id: 999");

        verify(dailyRoutineRepository).findById(999L);
    }

    @Test
    @DisplayName("루틴 조회 실패 - 권한 없음")
    void getRoutineById_NoPermission() {
        // given
        User otherUser = User.builder().id(2L).build();
        DailyRoutine otherRoutine = DailyRoutine.builder()
            .id(1L)
            .user(otherUser)
            .build();
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(otherRoutine));

        // when & then
        assertThatThrownBy(() -> dailyRoutineService.getRoutineById(1L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("You don't have permission to access this routine");
    }

    // === 날짜별 루틴 조회 테스트 ===

    @Test
    @DisplayName("날짜별 루틴 조회 성공")
    void getRoutinesByDate_Success() {
        // given
        LocalDate today = LocalDate.now();
        List<DailyRoutine> activeRoutines = List.of(testRoutine);
        List<DailyRoutine> completedRoutines = List.of();
        List<DailyRoutine> passedRoutines = List.of();

        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findRoutinesByUserAndDate(testUser, today))
            .willReturn(List.of(testRoutine));

        // when
        Map<String, List<DailyRoutineDto>> result = dailyRoutineService.getRoutinesByDate(1L, today);

        // then
        assertThat(result).containsKeys("active", "completed", "passed");
        assertThat(result.get("active")).hasSize(1);
        assertThat(result.get("completed")).isEmpty();
        assertThat(result.get("passed")).isEmpty();

        verify(userService).getUserById(1L);
        verify(dailyRoutineRepository).findRoutinesByUserAndDate(testUser, today);
    }

    // === 루틴 수정 테스트 ===

    @Test
    @DisplayName("루틴 수정 성공")
    void updateRoutine_Success() {
        // given
        DailyRoutineDto updateDto = DailyRoutineDto.builder()
            .category("자료구조")
            .title("스택과 큐 연습")
            .description("LIFO, FIFO 구조 이해")
            .focusTime(90)
            .breakTime(15)
            .build();

        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willReturn(testRoutine);

        // when
        DailyRoutineDto result = dailyRoutineService.updateRoutine(1L, updateDto, 1L);

        // then
        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    // === 루틴 삭제 테스트 ===

    @Test
    @DisplayName("루틴 삭제 성공 (Soft Delete)")
    void deleteRoutine_Success() {
        // given
        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willReturn(testRoutine);

        // when
        dailyRoutineService.deleteRoutine(1L, 1L);

        // then
        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    // === 루틴 완료 테스트 ===

    @Test
    @DisplayName("루틴 완료 처리 성공")
    void completeRoutine_Success() {
        // given
        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willAnswer(invocation -> {
            DailyRoutine routine = invocation.getArgument(0);
            return DailyRoutine.builder()
                .id(routine.getId())
                .user(routine.getUser())
                .category(routine.getCategory())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .focusTime(routine.getFocusTime())
                .actualFocusTime(routine.getActualFocusTime())
                .breakTime(routine.getBreakTime())
                .status("COMPLETED")
                .startedAt(routine.getStartedAt())
                .completedAt(LocalDateTime.now())
                .isDeleted(routine.getIsDeleted())
                .createdAt(routine.getCreatedAt())
                .build();
        });

        // when
        DailyRoutineDto result = dailyRoutineService.completeRoutine(1L, 1L);

        // then
        assertThat(result.getStatus()).isEqualTo("COMPLETED");
        assertThat(result.getCompletedAt()).isNotNull();

        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    @Test
    @DisplayName("루틴 PASS 처리 성공")
    void passRoutine_Success() {
        // given
        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willAnswer(invocation -> {
            DailyRoutine routine = invocation.getArgument(0);
            return DailyRoutine.builder()
                .id(routine.getId())
                .user(routine.getUser())
                .category(routine.getCategory())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .focusTime(routine.getFocusTime())
                .breakTime(routine.getBreakTime())
                .status("PASS")
                .startedAt(routine.getStartedAt())
                .completedAt(routine.getCompletedAt())
                .isDeleted(routine.getIsDeleted())
                .createdAt(routine.getCreatedAt())
                .build();
        });

        // when
        DailyRoutineDto result = dailyRoutineService.passRoutine(1L, 1L);

        // then
        assertThat(result.getStatus()).isEqualTo("PASS");
        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    @Test
    @DisplayName("루틴 완료 취소 성공")
    void cancelCompletion_Success() {
        // given
        DailyRoutine completedRoutine = DailyRoutine.builder()
            .id(1L)
            .user(testUser)
            .category("알고리즘")
            .title("프로그래머스 Level 2")
            .status("COMPLETED")
            .completedAt(LocalDateTime.now())
            .build();

        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(completedRoutine));
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willAnswer(invocation -> {
            DailyRoutine routine = invocation.getArgument(0);
            return DailyRoutine.builder()
                .id(routine.getId())
                .user(routine.getUser())
                .category(routine.getCategory())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .focusTime(routine.getFocusTime())
                .breakTime(routine.getBreakTime())
                .status("ACTIVE")
                .startedAt(routine.getStartedAt())
                .completedAt(null)
                .isDeleted(routine.getIsDeleted())
                .createdAt(routine.getCreatedAt())
                .build();
        });

        // when
        DailyRoutineDto result = dailyRoutineService.cancelCompletion(1L, 1L);

        // then
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
        assertThat(result.getCompletedAt()).isNull();
        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    // === 포모도로 세션 테스트 ===

    @Test
    @DisplayName("포모도로 세션 시작 성공")
    void startPomodoroSession_Success() {
        // given
        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willReturn(testRoutine);
        given(pomodoroSessionRepository.save(any(PomodoroSession.class))).willReturn(testSession);

        // when
        PomodoroSessionDto result = dailyRoutineService.startPomodoroSession(1L, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRoutineId()).isEqualTo(1L);
        assertThat(result.getDurationMinutes()).isEqualTo(25);
        assertThat(result.getStartedAt()).isNotNull();

        verify(pomodoroSessionRepository).save(any(PomodoroSession.class));
    }

    @Test
    @DisplayName("포모도로 세션 종료 성공")
    void endPomodoroSession_Success() {
        // given
        given(pomodoroSessionRepository.findById(1L)).willReturn(Optional.of(testSession));
        given(pomodoroSessionRepository.save(any(PomodoroSession.class))).willReturn(testSession);

        // when
        PomodoroSessionDto result = dailyRoutineService.endPomodoroSession(1L, 1L);

        // then
        assertThat(result).isNotNull();
        verify(pomodoroSessionRepository).save(any(PomodoroSession.class));
    }

    @Test
    @DisplayName("포모도로 세션 권한 체크")
    void endPomodoroSession_NoPermission() {
        // given
        User otherUser = User.builder().id(2L).build();
        DailyRoutine otherRoutine = DailyRoutine.builder()
            .id(1L)
            .user(otherUser)
            .build();
        PomodoroSession otherSession = PomodoroSession.builder()
            .id(1L)
            .routine(otherRoutine)
            .build();

        given(pomodoroSessionRepository.findById(1L)).willReturn(Optional.of(otherSession));

        // when & then
        assertThatThrownBy(() -> dailyRoutineService.endPomodoroSession(1L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("You don't have permission to end this session");
    }

    // === 실제 집중 시간 업데이트 테스트 ===

    @Test
    @DisplayName("실제 집중 시간 업데이트 성공")
    void updateActualFocusTime_Success() {
        // given
        int additionalMinutes = 23;
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willReturn(testRoutine);

        // when
        dailyRoutineService.updateActualFocusTime(1L, additionalMinutes, 1L);

        // then
        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    @Test
    @DisplayName("실제 집중 시간 누적 계산")
    void updateActualFocusTime_Accumulation() {
        // given
        DailyRoutine routineWithTime = DailyRoutine.builder()
            .id(1L)
            .user(testUser)
            .actualFocusTime(30) // 이미 30분 있음
            .build();

        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(routineWithTime));
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willAnswer(invocation -> {
            DailyRoutine saved = invocation.getArgument(0);
            // 30(기존) + 25(추가) = 55분이어야 함
            assertThat(saved.getActualFocusTime()).isEqualTo(55);
            return saved;
        });

        // when
        dailyRoutineService.updateActualFocusTime(1L, 25, 1L);

        // then
        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    // === 회고 관련 테스트 ===

    @Test
    @DisplayName("코딩테스트 회고 저장 성공")
    void saveCodingTestReviews_Success() {
        // given
        List<CodingTestReviewDto> reviewDtos = List.of(
            CodingTestReviewDto.builder()
                .problemTitle("두 정수 사이의 합")
                .problemDescription("a와 b 사이 모든 정수의 합")
                .mySolution("for문 사용")
                .correctSolution("수학 공식 사용")
                .problemType("수학")
                .build()
        );

        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(codingTestReviewRepository.saveAll(any())).willReturn(List.of());

        // when
        List<CodingTestReviewDto> result = dailyRoutineService.saveCodingTestReviews(1L, 1L, reviewDtos);

        // then
        verify(codingTestReviewRepository).saveAll(any());
    }

    @Test
    @DisplayName("면접 회고 저장 성공")
    void saveInterviewReviews_Success() {
        // given
        List<InterviewReviewDto> reviewDtos = List.of(
            InterviewReviewDto.builder()
                .techCategory("자료구조")
                .studyContent("스택과 큐 학습")
                .difficultParts("LIFO/FIFO 개념 혼동")
                .nextStudyPlan("트리 구조 학습")
                .build()
        );

        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(interviewReviewRepository.saveAll(any())).willReturn(List.of());

        // when
        List<InterviewReviewDto> result = dailyRoutineService.saveInterviewReviews(1L, 1L, reviewDtos);

        // then
        verify(interviewReviewRepository).saveAll(any());
    }

    // === 루틴 타입 확인 테스트 ===

    @Test
    @DisplayName("코딩테스트 루틴 확인")
    void isCodingTestRoutine_True() {
        // given
        DailyRoutineDto codingRoutine = DailyRoutineDto.builder()
            .category("코딩테스트 준비")
            .build();

        // when
        boolean result = dailyRoutineService.isCodingTestRoutine(codingRoutine);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("면접준비 루틴 확인")
    void isInterviewRoutine_True() {
        // given
        DailyRoutineDto interviewRoutine = DailyRoutineDto.builder()
            .category("면접준비")
            .build();

        // when
        boolean result = dailyRoutineService.isInterviewRoutine(interviewRoutine);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("일반 루틴 확인 - 코딩테스트 아님")
    void isCodingTestRoutine_False() {
        // given
        DailyRoutineDto normalRoutine = DailyRoutineDto.builder()
            .category("알고리즘")
            .build();

        // when
        boolean result = dailyRoutineService.isCodingTestRoutine(normalRoutine);

        // then
        assertThat(result).isFalse();
    }

    // === 통합 루틴 완료 테스트 ===

    @Test
    @DisplayName("코딩테스트 루틴 완료 (회고 포함)")
    void completeCodingTestRoutine_Success() {
        // given
        List<CodingTestReviewDto> reviewDtos = List.of(
            CodingTestReviewDto.builder()
                .problemTitle("테스트 문제")
                .problemDescription("설명")
                .mySolution("내 풀이")
                .correctSolution("정답 풀이")
                .problemType("알고리즘")
                .build()
        );

        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(codingTestReviewRepository.saveAll(any())).willReturn(List.of());
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willReturn(testRoutine);

        // when
        DailyRoutineDto result = dailyRoutineService.completeCodingTestRoutine(1L, 1L, reviewDtos);

        // then
        verify(codingTestReviewRepository).saveAll(any());
        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    @Test
    @DisplayName("면접준비 루틴 완료 (회고 포함)")
    void completeInterviewRoutine_Success() {
        // given
        List<InterviewReviewDto> reviewDtos = List.of(
            InterviewReviewDto.builder()
                .techCategory("데이터베이스")
                .studyContent("SQL 인덱스 학습")
                .difficultParts("복합 인덱스 이해")
                .nextStudyPlan("트랜잭션 격리 수준")
                .build()
        );

        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(testRoutine));
        given(interviewReviewRepository.saveAll(any())).willReturn(List.of());
        given(dailyRoutineRepository.save(any(DailyRoutine.class))).willReturn(testRoutine);

        // when
        DailyRoutineDto result = dailyRoutineService.completeInterviewRoutine(1L, 1L, reviewDtos);

        // then
        verify(interviewReviewRepository).saveAll(any());
        verify(dailyRoutineRepository).save(any(DailyRoutine.class));
    }

    // === 예외 상황 테스트 ===

    @Test
    @DisplayName("존재하지 않는 포모도로 세션 종료 시도")
    void endPomodoroSession_SessionNotFound() {
        // given
        given(pomodoroSessionRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> dailyRoutineService.endPomodoroSession(999L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Session not found with id: 999");
    }

    @Test
    @DisplayName("권한 없는 사용자의 루틴 수정 시도")
    void updateRoutine_NoPermission() {
        // given
        User otherUser = User.builder().id(2L).build();
        DailyRoutine otherRoutine = DailyRoutine.builder()
            .id(1L)
            .user(otherUser)
            .build();

        given(userService.getUserById(1L)).willReturn(testUser);
        given(dailyRoutineRepository.findById(1L)).willReturn(Optional.of(otherRoutine));

        // when & then
        assertThatThrownBy(() -> dailyRoutineService.updateRoutine(1L, testRoutineDto, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("You don't have permission to update this routine");
    }
}