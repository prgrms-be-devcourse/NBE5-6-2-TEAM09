package com.grepp.codemap.routine.domain;

import com.grepp.codemap.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "coding_test_reviews_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CodingTestReviewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "original_review_id")
    private Long originalReviewId; // 원본 리뷰의 ID

    @Column(name = "original_routine_id")
    private Long originalRoutineId; // 원본 루틴의 ID

    private String problemTitle;

    @Column(columnDefinition = "TEXT")
    private String problemDescription;

    @Column(columnDefinition = "TEXT")
    private String mySolution;

    @Column(columnDefinition = "TEXT")
    private String correctSolution;

    private String problemType; // 문제 종류

    @Column(name = "routine_date")
    private LocalDate routineDate; // 해당 루틴이 수행된 날짜

    @Column(name = "archived_at")
    private LocalDateTime archivedAt; // 히스토리로 옮겨진 시간

    @Column(updatable = false)
    private LocalDateTime createdAt; // 원본 리뷰의 생성 시간

    private LocalDateTime updatedAt; // 원본 리뷰의 수정 시간

    @PrePersist
    protected void onCreate() {
        this.archivedAt = LocalDateTime.now();
    }

    // CodingTestReview를 CodingTestReviewHistory로 변환하는 정적 메서드
    public static CodingTestReviewHistory fromCodingTestReview(CodingTestReview review, User user, LocalDate routineDate) {
        return CodingTestReviewHistory.builder()
            .user(user)
            .originalReviewId(review.getId())
            .originalRoutineId(review.getRoutine().getId())
            .problemTitle(review.getProblemTitle())
            .problemDescription(review.getProblemDescription())
            .mySolution(review.getMySolution())
            .correctSolution(review.getCorrectSolution())
            .problemType(review.getProblemType())
            .routineDate(routineDate)
            .createdAt(review.getCreatedAt())
            .updatedAt(review.getUpdatedAt())
            .build();
    }
}