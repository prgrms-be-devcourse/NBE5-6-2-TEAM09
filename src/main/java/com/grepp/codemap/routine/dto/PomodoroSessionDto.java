package com.grepp.codemap.routine.dto;

import com.grepp.codemap.routine.domain.PomodoroSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PomodoroSessionDto {
    private Long id;
    private Long routineId;
    private Integer durationMinutes;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public static PomodoroSessionDto fromEntity(PomodoroSession entity) {
        return PomodoroSessionDto.builder()
            .id(entity.getId())
            .routineId(entity.getRoutine().getId())
            .durationMinutes(entity.getDurationMinutes())
            .startedAt(entity.getStartedAt())
            .endedAt(entity.getEndedAt())
            .build();
    }
}