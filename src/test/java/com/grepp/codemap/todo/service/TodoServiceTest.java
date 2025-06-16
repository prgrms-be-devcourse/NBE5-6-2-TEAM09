package com.grepp.codemap.todo.service;

import com.grepp.codemap.todo.domain.Todo;
import com.grepp.codemap.todo.dto.TodoAlertDto;
import com.grepp.codemap.todo.dto.TodoResponse;
import com.grepp.codemap.todo.repository.TodoRepository;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoService 단위 테스트")
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoService todoService;

    private User testUser;
    private Todo testTodo;
    private Todo completedTodo;
    private Todo upcomingTodo;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .email("test@example.com")
            .nickname("테스터")
            .build();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);

        testTodo = Todo.builder()
            .id(1L)
            .user(testUser)
            .title("알고리즘 문제 풀기")
            .description("프로그래머스 Level 2 문제 3개")
            .startTime(now.plusHours(2))
            .completedAt(tomorrow)
            .isCompleted(false)
            .isDeleted(false)
            .build();

        completedTodo = Todo.builder()
            .id(2L)
            .user(testUser)
            .title("완료된 할일")
            .description("이미 완료된 작업")
            .startTime(now.minusHours(1))
            .completedAt(now)
            .isCompleted(true)
            .isDeleted(false)
            .build();

        upcomingTodo = Todo.builder()
            .id(3L)
            .user(testUser)
            .title("10분 후 시작")
            .description("곧 시작할 작업")
            .startTime(now.plusMinutes(8)) // 8분 후 시작 (10분 이내)
            .completedAt(tomorrow)
            .isCompleted(false)
            .isDeleted(false)
            .build();
    }

    // === Todo 생성 테스트 ===

    @Test
    @DisplayName("Todo 생성 성공")
    void createTodo_Success() {
        // given
        String title = "새로운 할일";
        String description = "상세 설명";
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime completedAt = LocalDateTime.now().plusDays(1);

        given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
        given(todoRepository.save(any(Todo.class))).willReturn(testTodo);

        // when
        Todo result = todoService.createTodo(1L, title, description, startTime, completedAt);

        // then
        assertThat(result).isNotNull();
        verify(userRepository).findById(1L);
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    @DisplayName("Todo 생성 실패 - 존재하지 않는 사용자")
    void createTodo_UserNotFound() {
        // given
        given(userRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
            todoService.createTodo(999L, "제목", "설명",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효하지 않은 사용자입니다.");

        verify(userRepository).findById(999L);
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("Todo 생성 시 기본값 설정 확인")
    void createTodo_DefaultValues() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
        given(todoRepository.save(any(Todo.class))).willAnswer(invocation -> {
            Todo todo = invocation.getArgument(0);
            // 기본값 확인
            assertThat(todo.getIsCompleted()).isFalse();
            assertThat(todo.getIsDeleted()).isFalse();
            assertThat(todo.getUser()).isEqualTo(testUser);
            return todo;
        });

        // when
        todoService.createTodo(1L, "제목", "설명",
            LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        // then
        verify(todoRepository).save(any(Todo.class));
    }

    // === Todo 조회 테스트 ===

    @Test
    @DisplayName("사용자별 모든 Todo 조회")
    void findAllByUser_Success() {
        // given
        List<Todo> expectedTodos = List.of(testTodo, completedTodo);
        given(todoRepository.findAllByUser_Id(1L)).willReturn(expectedTodos);

        // when
        List<Todo> result = todoService.findAllByUser(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(testTodo, completedTodo);
        verify(todoRepository).findAllByUser_Id(1L);
    }

    @Test
    @DisplayName("날짜별 Todo 조회 - 완료된 할일이 아래로 정렬")
    void getTodosByDate_CompletedTodosAtBottom() {
        // given
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        List<Todo> todos = List.of(testTodo, completedTodo);

        given(todoRepository.findAllByUser_IdAndStartTimeBetween(1L, start, end))
            .willReturn(todos);

        // when
        List<TodoResponse> result = todoService.getTodosByDate(1L, start, end);

        // then
        assertThat(result).hasSize(2);
        // 완료되지 않은 todo가 먼저, 완료된 todo가 나중에
        assertThat(result.get(0).isCompleted()).isFalse();
        assertThat(result.get(1).isCompleted()).isTrue();

        verify(todoRepository).findAllByUser_IdAndStartTimeBetween(1L, start, end);
    }

    @Test
    @DisplayName("특정 Todo 조회 성공")
    void findByIdAndUser_Success() {
        // given
        given(todoRepository.findByIdAndUser_Id(1L, 1L)).willReturn(testTodo);

        // when
        Todo result = todoService.findByIdAndUser(1L, 1L);

        // then
        assertThat(result).isEqualTo(testTodo);
        verify(todoRepository).findByIdAndUser_Id(1L, 1L);
    }

    @Test
    @DisplayName("존재하지 않는 Todo 조회 시 예외")
    void findByIdAndUser_NotFound() {
        // given
        given(todoRepository.findByIdAndUser_Id(999L, 1L)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> todoService.findByIdAndUser(999L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 Todo를 찾을 수 없습니다.");
    }

    // === Todo 수정 테스트 ===

    @Test
    @DisplayName("Todo 수정 성공")
    void updateTodo_Success() {
        // given
        String newTitle = "수정된 제목";
        String newDescription = "수정된 설명";
        LocalDateTime newStartTime = LocalDateTime.now().plusHours(3);
        LocalDateTime newCompletedAt = LocalDateTime.now().plusDays(2);

        given(todoRepository.findByIdAndUser_Id(1L, 1L)).willReturn(testTodo);
        given(todoRepository.save(any(Todo.class))).willReturn(testTodo);

        // when
        Todo result = todoService.updateTodo(1L, 1L, newTitle, newDescription,
            newStartTime, newCompletedAt);

        // then
        assertThat(result).isNotNull();
        verify(todoRepository).findByIdAndUser_Id(1L, 1L);
        verify(todoRepository).save(testTodo);
    }

    // === Todo 삭제 테스트 ===

    @Test
    @DisplayName("Todo 삭제 성공")
    void deleteTodo_Success() {
        // given
        given(todoRepository.findByIdAndUser_Id(1L, 1L)).willReturn(testTodo);

        // when
        todoService.deleteTodo(1L, 1L);

        // then
        verify(todoRepository).findByIdAndUser_Id(1L, 1L);
        verify(todoRepository).delete(testTodo);
    }

    // === Todo 완료 상태 토글 테스트 ===

    @Test
    @DisplayName("Todo 완료 상태 토글 - 미완료 → 완료")
    void toggleComplete_IncompleteToComplete() {
        // given
        given(todoRepository.findByIdAndUser_Id(1L, 1L)).willReturn(testTodo);
        given(todoRepository.save(any(Todo.class))).willAnswer(invocation -> {
            Todo todo = invocation.getArgument(0);
            assertThat(todo.getIsCompleted()).isTrue(); // false → true로 변경되었는지 확인
            return todo;
        });

        // when
        Todo result = todoService.toggleComplete(1L, 1L);

        // then
        verify(todoRepository).save(testTodo);
    }

    @Test
    @DisplayName("Todo 완료 상태 토글 - 완료 → 미완료")
    void toggleComplete_CompleteToIncomplete() {
        // given
        given(todoRepository.findByIdAndUser_Id(2L, 1L)).willReturn(completedTodo);
        given(todoRepository.save(any(Todo.class))).willAnswer(invocation -> {
            Todo todo = invocation.getArgument(0);
            assertThat(todo.getIsCompleted()).isFalse(); // true → false로 변경되었는지 확인
            return todo;
        });

        // when
        Todo result = todoService.toggleComplete(2L, 1L);

        // then
        verify(todoRepository).save(completedTodo);
    }

    // === 알림 관련 테스트 ===

    @Test
    @DisplayName("알림 대상 Todo 조회")
    void getTodosToNotify_Success() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesLater = now.plusMinutes(10);
        List<Todo> upcomingTodos = List.of(upcomingTodo);

        given(todoRepository.findAllByUser_IdAndStartTimeBetweenAndIsCompletedFalse(
            1L, now, tenMinutesLater)).willReturn(upcomingTodos);

        // when
        List<Todo> result = todoService.getTodosToNotify(1L, now, tenMinutesLater);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(upcomingTodo);
        verify(todoRepository).findAllByUser_IdAndStartTimeBetweenAndIsCompletedFalse(
            1L, now, tenMinutesLater);
    }

    @Test
    @DisplayName("10분 내 시작하는 Todo 조회 (알림용)")
    void findTodosStartingIn10Minutes_Success() {
        // given
        List<Todo> upcomingTodos = List.of(upcomingTodo);
        given(todoRepository.findByUserIdAndStartTimeBetween(
            eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(upcomingTodos);

        // when
        List<TodoAlertDto> result = todoService.findTodosStartingIn10Minutes(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("10분 후 시작");
        assertThat(result.get(0).startTime()).isNotNull();

        verify(todoRepository).findByUserIdAndStartTimeBetween(
            eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("10분 내 시작하는 Todo 조회 - 완료된 Todo 제외")
    void findTodosStartingIn10Minutes_ExcludeCompleted() {
        // given
        Todo completedUpcomingTodo = Todo.builder()
            .id(4L)
            .user(testUser)
            .title("완료된 예정 작업")
            .startTime(LocalDateTime.now().plusMinutes(5))
            .isCompleted(true) // 완료됨
            .build();

        List<Todo> todos = List.of(upcomingTodo, completedUpcomingTodo);
        given(todoRepository.findByUserIdAndStartTimeBetween(
            eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(todos);

        // when
        List<TodoAlertDto> result = todoService.findTodosStartingIn10Minutes(1L);

        // then
        assertThat(result).hasSize(1); // 완료된 todo는 제외
        assertThat(result.get(0).title()).isEqualTo("10분 후 시작");
    }

    @Test
    @DisplayName("모든 사용자 ID 조회")
    void findAllUserIds_Success() {
        // given
        List<Long> userIds = List.of(1L, 2L, 3L);
        given(todoRepository.findDistinctUserIds()).willReturn(userIds);

        // when
        List<Long> result = todoService.findAllUserIds();

        // then
        assertThat(result).containsExactlyInAnyOrder(1L, 2L, 3L);
        verify(todoRepository).findDistinctUserIds();
    }

    // === 날짜별 Todo 제목 그룹화 테스트 ===

    @Test
    @DisplayName("날짜별 Todo 제목 그룹화")
    void getTodoTitlesGroupedByDate_Success() {
        // given
        Todo todayTodo = Todo.builder()
            .title("오늘 할일")
            .startTime(LocalDate.now().atTime(10, 0))
            .build();

        Todo tomorrowTodo = Todo.builder()
            .title("내일 할일")
            .startTime(LocalDate.now().plusDays(1).atTime(14, 0))
            .build();

        List<Todo> todos = List.of(todayTodo, tomorrowTodo);
        given(todoRepository.findAllByUser_Id(1L)).willReturn(todos);

        // when
        Map<String, List<String>> result = todoService.getTodoTitlesGroupedByDate(1L);

        // then
        assertThat(result).hasSize(2);

        String today = LocalDate.now().toString();
        String tomorrow = LocalDate.now().plusDays(1).toString();

        assertThat(result.get(today)).containsExactly("오늘 할일");
        assertThat(result.get(tomorrow)).containsExactly("내일 할일");

        verify(todoRepository).findAllByUser_Id(1L);
    }

    @Test
    @DisplayName("같은 날짜에 여러 Todo가 있는 경우")
    void getTodoTitlesGroupedByDate_MultipleTodosPerDay() {
        // given
        LocalDateTime today = LocalDate.now().atTime(10, 0);

        Todo todo1 = Todo.builder()
            .title("첫 번째 할일")
            .startTime(today)
            .build();

        Todo todo2 = Todo.builder()
            .title("두 번째 할일")
            .startTime(today.plusHours(2))
            .build();

        List<Todo> todos = List.of(todo1, todo2);
        given(todoRepository.findAllByUser_Id(1L)).willReturn(todos);

        // when
        Map<String, List<String>> result = todoService.getTodoTitlesGroupedByDate(1L);

        // then
        String todayKey = LocalDate.now().toString();
        assertThat(result.get(todayKey)).containsExactly("첫 번째 할일", "두 번째 할일");
    }

    // === 경계값 테스트 ===

    @Test
    @DisplayName("빈 Todo 리스트 처리")
    void getTodosByDate_EmptyList() {
        // given
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        given(todoRepository.findAllByUser_IdAndStartTimeBetween(1L, start, end))
            .willReturn(List.of());

        // when
        List<TodoResponse> result = todoService.getTodosByDate(1L, start, end);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("알림 대상 Todo 없음")
    void findTodosStartingIn10Minutes_EmptyResult() {
        // given
        given(todoRepository.findByUserIdAndStartTimeBetween(
            eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(List.of());

        // when
        List<TodoAlertDto> result = todoService.findTodosStartingIn10Minutes(1L);

        // then
        assertThat(result).isEmpty();
    }

    // === 실제 사용 시나리오 테스트 ===

    @Test
    @DisplayName("실제 시나리오 - 하루 일정 관리")
    void realScenario_DailyScheduleManagement() {
        // given - 하루 일정 시나리오
        LocalDate today = LocalDate.now();
        LocalDateTime morning = today.atTime(9, 0);
        LocalDateTime afternoon = today.atTime(14, 0);
        LocalDateTime evening = today.atTime(18, 0);

        Todo morningTodo = Todo.builder()
            .id(1L)
            .title("아침 운동")
            .startTime(morning)
            .isCompleted(true) // 완료됨
            .build();

        Todo afternoonTodo = Todo.builder()
            .id(2L)
            .title("면접 준비")
            .startTime(afternoon)
            .isCompleted(false) // 진행 중
            .build();

        Todo eveningTodo = Todo.builder()
            .id(3L)
            .title("코딩 연습")
            .startTime(evening)
            .isCompleted(false) // 아직 시작 안함
            .build();

        List<Todo> dayTodos = List.of(morningTodo, afternoonTodo, eveningTodo);
        given(todoRepository.findAllByUser_IdAndStartTimeBetween(
            eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(dayTodos);

        // when
        List<TodoResponse> result = todoService.getTodosByDate(1L,
            today.atStartOfDay(), today.atTime(LocalTime.MAX));

        // then
        assertThat(result).hasSize(3);

        // 완료되지 않은 todo가 먼저 와야 함
        List<TodoResponse> incompleteTodos = result.stream()
            .filter(todo -> !todo.isCompleted())
            .toList();
        List<TodoResponse> completedTodos = result.stream()
            .filter(TodoResponse::isCompleted)
            .toList();

        assertThat(incompleteTodos).hasSize(2);
        assertThat(completedTodos).hasSize(1);

        // 정렬 순서 확인: 미완료가 먼저, 완료가 나중
        assertThat(result.get(0).isCompleted()).isFalse();
        assertThat(result.get(1).isCompleted()).isFalse();
        assertThat(result.get(2).isCompleted()).isTrue();
    }
}