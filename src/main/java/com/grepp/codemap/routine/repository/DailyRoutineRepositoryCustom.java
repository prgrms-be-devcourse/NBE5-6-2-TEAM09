package com.grepp.codemap.routine.repository;

import com.grepp.codemap.routine.domain.DailyRoutine;
import com.grepp.codemap.user.domain.User;

import java.time.LocalDate;
import java.util.List;

public interface DailyRoutineRepositoryCustom {
    List<DailyRoutine> findActiveRoutinesByUser(User user);
    List<DailyRoutine> findCompletedRoutinesByUser(User user);
    List<DailyRoutine> findPassedRoutinesByUser(User user);
    List<DailyRoutine> findRoutinesByUserAndDate(User user, LocalDate date);
}