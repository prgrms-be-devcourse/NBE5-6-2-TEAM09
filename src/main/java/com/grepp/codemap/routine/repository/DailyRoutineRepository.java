package com.grepp.codemap.routine.repository;

import com.grepp.codemap.routine.domain.DailyRoutine;
import com.grepp.codemap.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyRoutineRepository extends JpaRepository<DailyRoutine, Long>,
    DailyRoutineRepositoryCustom,
    QuerydslPredicateExecutor<DailyRoutine> {

    List<DailyRoutine> findByUserAndIsDeletedFalseOrderByCreatedAtDesc(User user);

    @Query("SELECT dr FROM DailyRoutine dr WHERE dr.user = :user AND dr.isDeleted = false AND dr.status = :status ORDER BY dr.createdAt DESC")
    List<DailyRoutine> findByUserAndStatusAndNotDeleted(@Param("user") User user,
        @Param("status") String status);

    @Query("SELECT SUM(dr.focusTime) FROM DailyRoutine dr WHERE dr.user = :user AND dr.status = 'COMPLETED' AND dr.isDeleted = false")
    Integer getTotalFocusTimeByUser(@Param("user") User user);

    @Query("SELECT dr.category, SUM(dr.focusTime) FROM DailyRoutine dr WHERE dr.user = :user AND dr.status = 'COMPLETED' AND dr.isDeleted = false GROUP BY dr.category")
    List<Object[]> getTotalFocusTimeByCategory(@Param("user") User user);

    // 전체 루틴 수
    @Query("SELECT COUNT(r) FROM DailyRoutine r WHERE r.user.id = :userId AND r.isDeleted = false")
    long countAllByUserId(@Param("userId") Long userId);

    // 완료된 루틴 수
    @Query("SELECT COUNT(r) FROM DailyRoutine r WHERE r.user.id = :userId AND r.status = 'COMPLETED' AND r.isDeleted = false")
    long countCompletedByUserId(@Param("userId") Long userId);

    // 카테고리별 완료 수 & 전체 수
    @Query("""
            SELECT r.category, 
                   SUM(CASE WHEN r.status = 'COMPLETED' THEN 1 ELSE 0 END),
                   COUNT(r)
            FROM DailyRoutine r
            WHERE r.user.id = :userId AND r.isDeleted = false
            GROUP BY r.category
        """)
    List<Object[]> countByCategory(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM DailyRoutine dr WHERE dr.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Query(value = """
            SELECT DAYOFWEEK(r.created_at) AS weekday, SUM(r.focus_time)
            FROM daily_routines r
            WHERE r.user_id = :userId AND r.status = 'COMPLETED' AND r.is_deleted = false
            GROUP BY DAYOFWEEK(r.created_at)
        """, nativeQuery = true)
    List<Object[]> sumFocusTimeGroupedByWeekday(@Param("userId") Long userId);


}