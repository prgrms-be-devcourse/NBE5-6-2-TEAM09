package com.grepp.codemap.routine.domain;

import com.grepp.codemap.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "routines_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoutineHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "original_routine_id")
    private Long originalRoutineId; // 원본 루틴의 ID

    private String category;
    private String title;
    private String description;
    private String status; // ACTIVE, COMPLETED, PASS
    private Integer focusTime;
    private Integer actualFocusTime;
    private Integer breakTime;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @Column(name = "routine_date")
    private LocalDate routineDate; // 해당 루틴이 수행된 날짜

    @Column(name = "archived_at")
    private LocalDateTime archivedAt; // 히스토리로 옮겨진 시간

    @Column(updatable = false)
    private LocalDateTime createdAt; // 원본 루틴의 생성 시간

    @PrePersist
    protected void onCreate() {
        this.archivedAt = LocalDateTime.now();
    }

    public static RoutineHistory fromDailyRoutine(DailyRoutine dailyRoutine, User user, LocalDate routineDate) {
        return RoutineHistory.builder()
            .user(user) // 매개변수로 받은 영속화된 User 객체 사용
            .originalRoutineId(dailyRoutine.getId())
            .category(dailyRoutine.getCategory())
            .title(dailyRoutine.getTitle())
            .description(dailyRoutine.getDescription())
            .status(dailyRoutine.getStatus())
            .focusTime(dailyRoutine.getFocusTime())
            .actualFocusTime(dailyRoutine.getActualFocusTime())
            .breakTime(dailyRoutine.getBreakTime())
            .startedAt(dailyRoutine.getStartedAt())
            .completedAt(dailyRoutine.getCompletedAt())
            .routineDate(routineDate)
            .createdAt(dailyRoutine.getCreatedAt())
            .build();
    }

    // 🔧 기존 메서드는 deprecated로 표시 (하위 호환성을 위해 유지)
    @Deprecated
    public static RoutineHistory fromDailyRoutine(DailyRoutine dailyRoutine, LocalDate routineDate) {
        throw new UnsupportedOperationException("Use fromDailyRoutine(DailyRoutine, User, LocalDate) instead");
    }
}