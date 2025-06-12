package com.grepp.codemap.routine.dto;

import com.grepp.codemap.routine.domain.CodingTestReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingTestReviewDto {
    private Long id;
    private Long routineId;
    private String problemTitle;
    private String problemDescription;
    private String mySolution;
    private String correctSolution;
    private String problemType;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CodingTestReviewDto fromEntity(CodingTestReview entity) {
        return CodingTestReviewDto.builder()
            .id(entity.getId())
            .routineId(entity.getRoutine().getId())
            .problemTitle(entity.getProblemTitle())
            .problemDescription(entity.getProblemDescription())
            .mySolution(entity.getMySolution())
            .correctSolution(entity.getCorrectSolution())
            .problemType(entity.getProblemType())
            .isDeleted(entity.getIsDeleted())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}