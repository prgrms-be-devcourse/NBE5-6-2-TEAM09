package com.grepp.codemap.routine.repository;

import com.grepp.codemap.routine.domain.PomodoroSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PomodoroSessionRepository extends JpaRepository<PomodoroSession, Long> {

    @Query("SELECT ps FROM PomodoroSession ps WHERE ps.routine.id = :routineId ORDER BY ps.startedAt DESC")
    List<PomodoroSession> findByRoutineIdOrderByStartedAtDesc(@Param("routineId") Long routineId);

    @Query("SELECT COALESCE(SUM(COALESCE(ps.durationMinutes, 0)), 0) " +
        "FROM PomodoroSession ps WHERE ps.routine.id = :routineId")
    Integer getTotalSessionMinutesByRoutineId(@Param("routineId") Long routineId);

    // 실제 진행 시간을 초 단위로 계산하는 쿼리 (더 정확한 계산)
    @Query(value = """
        SELECT COALESCE(
            SUM(
                CASE 
                    WHEN ps.ended_at IS NOT NULL 
                    THEN TIMESTAMPDIFF(SECOND, ps.started_at, ps.ended_at)
                    ELSE 0 
                END
            ), 0
        ) as total_seconds
        FROM pomodoro_sessions ps 
        WHERE ps.routine_id = :routineId
        """, nativeQuery = true)
    Long getTotalActualSessionSeconds(@Param("routineId") Long routineId);


    @Modifying
    @Query("DELETE FROM PomodoroSession ps WHERE ps.routine.id IN (SELECT r.id FROM DailyRoutine r WHERE r.user.id = :userId)")
    void deleteByUserId(@Param("userId") Long userId);
}