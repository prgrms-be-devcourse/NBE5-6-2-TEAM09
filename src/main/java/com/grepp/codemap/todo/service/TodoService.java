package com.grepp.codemap.todo.service;

import com.grepp.codemap.todo.domain.Todo;
import com.grepp.codemap.todo.dto.TodoResponse;
import com.grepp.codemap.todo.repository.TodoRepository;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Todo 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    // 생성자를 통한 의존성 주입
    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    /**
     * 특정 기간 동안 사용자의 Todo 목록을 조회한다.
     *
     * @param userId 사용자 ID
     * @param start  조회 시작 시각 (inclusive)
     * @param end    조회 종료 시각 (inclusive)
     * @return 기간 내 Todo 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<TodoResponse> getTodosByDate(Long userId, LocalDateTime start, LocalDateTime end) {
        List<Todo> todos = todoRepository.findAllByUser_IdAndStartTimeBetween(userId, start, end);
        return todos.stream()
            .map(TodoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Todo getTodosForDate(Long userId, LocalDateTime start, LocalDateTime end) {
        return (Todo) todoRepository.findAllByUser_IdAndStartTimeBetween(userId, start, end);
    }

    @Transactional(readOnly = true)
    public Todo findByIdAndUser(Long id, Long userId) {
        Todo todo = todoRepository.findByIdAndUser_Id(id, userId);
        if (todo == null) {
            throw new IllegalArgumentException("해당 Todo를 찾을 수 없습니다.");
        }
        return todo;
    }

    public Todo createTodo(Long userId,
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime completedAt) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        Todo todo = Todo.builder()
            .user(user)
            .title(title)
            .description(description)
            .startTime(startTime)
            .completedAt(completedAt)
            .isCompleted(false)
            .isDeleted(false)
            .build();

        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id,
        Long userId,
        String title,
        String description,
        LocalDateTime completedAt) {
        Todo todo = findByIdAndUser(id, userId);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setCompletedAt(completedAt);
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id, Long userId) {
        Todo todo = findByIdAndUser(id, userId);
        todoRepository.delete(todo);
    }

    public Todo toggleComplete(Long id, Long userId) {
        Todo todo = findByIdAndUser(id, userId);
        boolean current = todo.getIsCompleted();
        todo.setIsCompleted(!current);
        return todoRepository.save(todo);
    }
}
