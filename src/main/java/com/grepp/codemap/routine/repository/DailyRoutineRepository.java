package com.grepp.codemap.routine.repository;

import com.grepp.codemap.routine.domain.DailyRoutine;
import com.grepp.codemap.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyRoutineRepository extends JpaRepository<DailyRoutine, Long>,
    DailyRoutineRepositoryCustom,
    QuerydslPredicateExecutor<DailyRoutine> {

    List<DailyRoutine> findByUserAndIsDeletedFalseOrderByCreatedAtDesc(User user);

    @Query("SELECT dr FROM DailyRoutine dr WHERE dr.user = :user AND dr.isDeleted = false AND dr.status = :status ORDER BY dr.createdAt DESC")
    List<DailyRoutine> findByUserAndStatusAndNotDeleted(@Param("user") User user, @Param("status") String status);

    @Query("SELECT SUM(dr.focusTime) FROM DailyRoutine dr WHERE dr.user = :user AND dr.status = 'COMPLETED' AND dr.isDeleted = false")
    Integer getTotalFocusTimeByUser(@Param("user") User user);

    @Query("SELECT dr.category, SUM(dr.focusTime) FROM DailyRoutine dr WHERE dr.user = :user AND dr.status = 'COMPLETED' AND dr.isDeleted = false GROUP BY dr.category")
    List<Object[]> getTotalFocusTimeByCategory(@Param("user") User user);
}