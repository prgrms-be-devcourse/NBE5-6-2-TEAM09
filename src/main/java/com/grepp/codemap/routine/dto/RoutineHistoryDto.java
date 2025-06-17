package com.grepp.codemap.routine.dto;

import com.grepp.codemap.routine.domain.RoutineHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineHistoryDto {
    private Long id;
    private Long userId;
    private Long originalRoutineId;
    private String category;
    private String title;
    private String description;
    private String status;
    private Integer focusTime;
    private Integer actualFocusTime;
    private Integer breakTime;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDate routineDate;
    private LocalDateTime archivedAt;
    private LocalDateTime createdAt;

    public static RoutineHistoryDto fromEntity(RoutineHistory entity) {
        return RoutineHistoryDto.builder()
            .id(entity.getId())
            .userId(entity.getUser().getId())
            .originalRoutineId(entity.getOriginalRoutineId())
            .category(entity.getCategory())
            .title(entity.getTitle())
            .description(entity.getDescription())
            .status(entity.getStatus())
            .focusTime(entity.getFocusTime())
            .actualFocusTime(entity.getActualFocusTime())
            .breakTime(entity.getBreakTime())
            .startedAt(entity.getStartedAt())
            .completedAt(entity.getCompletedAt())
            .routineDate(entity.getRoutineDate())
            .archivedAt(entity.getArchivedAt())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}