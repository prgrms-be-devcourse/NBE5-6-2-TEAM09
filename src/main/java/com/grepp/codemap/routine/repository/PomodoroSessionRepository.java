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

    @Query("SELECT COALESCE(FLOOR(SUM(TIMESTAMPDIFF(SECOND, ps.startedAt, COALESCE(ps.endedAt, CURRENT_TIMESTAMP))) / 60), 0) " +
        "FROM PomodoroSession ps WHERE ps.routine.id = :routineId")
    Long getTotalSessionTimeByRoutineId(@Param("routineId") Long routineId);


    @Modifying
    @Query("DELETE FROM PomodoroSession ps WHERE ps.routine.id IN (SELECT r.id FROM DailyRoutine r WHERE r.user.id = :userId)")
    void deleteByUserId(@Param("userId") Long userId);
}