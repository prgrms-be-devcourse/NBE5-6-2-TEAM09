package com.grepp.codemap.todo.dto;

import com.grepp.codemap.todo.domain.Todo;
import java.time.LocalDateTime;

/**
 * 투두 조회 응답 DTO
 */
public record TodoResponse(
    Long id,
    String title,
    String description,
    Boolean isCompleted,
    LocalDateTime startTime,
    LocalDateTime completedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static TodoResponse of(Todo todo) {
        return new TodoResponse(
            todo.getId(),
            todo.getTitle(),
            todo.getDescription(),
            todo.getIsCompleted(),
            todo.getStartTime(),
            todo.getCompletedAt(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );
    }
}
