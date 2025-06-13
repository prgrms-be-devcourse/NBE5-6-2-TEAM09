package com.grepp.codemap.mypage.service;

import com.grepp.codemap.mypage.dto.RoutineCategoryCompletionDto;
import com.grepp.codemap.mypage.dto.RoutineCompletionDto;
import com.grepp.codemap.routine.repository.DailyRoutineRepository;
import com.grepp.codemap.todo.domain.Todo;
import com.grepp.codemap.todo.repository.TodoRepository;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final DailyRoutineRepository dailyRoutineRepository;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    private static final Map<Integer, String> WEEKDAY_MAP = Map.of(
        2, "월", 3, "화", 4, "수", 5, "목", 6, "금", 7, "토", 1, "일"
    );

    public RoutineCompletionDto getRoutineCompletionStats(Long userId) {
        long total = dailyRoutineRepository.countAllByUserId(userId);
        long completed = dailyRoutineRepository.countCompletedByUserId(userId);
        return new RoutineCompletionDto(completed, total);
    }

    public List<RoutineCategoryCompletionDto> getRoutineCompletionStatsByCategory(Long userId) {
        List<Object[]> results = dailyRoutineRepository.countByCategory(userId);
        return results.stream()
            .map(row -> new RoutineCategoryCompletionDto(
                (String) row[0],
                ((Number) row[1]).longValue(),
                ((Number) row[2]).longValue()
            ))
            .toList();
    }

    public Map<String, Integer> getFocusTimePerDay(Long userId) {
        List<Object[]> result = dailyRoutineRepository.sumActualFocusTimeGroupedByWeekday(userId);
        Map<String, Integer> focusMap = new LinkedHashMap<>();

        // 요일 순서대로 초기화
        List<String> order = List.of("월", "화", "수", "목", "금", "토", "일");
        for (String day : order) {
            focusMap.put(day, 0);
        }

        for (Object[] row : result) {
            Integer weekdayNumber = ((Number) row[0]).intValue(); // 1~7
            Integer focusTime = row[1] != null ? ((Number) row[1]).intValue() : 0;
            String weekday = WEEKDAY_MAP.get(weekdayNumber);
            focusMap.put(weekday, focusTime);
        }

        return focusMap;
    }
    public Integer getTotalActualFocusTime(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Integer total = dailyRoutineRepository.getTotalActualFocusTimeByUser(user);
        return total != null ? total : 0;
    }

    public Map<String, Integer> getActualFocusTimeByCategory(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Object[]> results = dailyRoutineRepository.getTotalActualFocusTimeByCategory(user);
        Map<String, Integer> categoryMap = new HashMap<>();

        for (Object[] row : results) {
            String category = (String) row[0];
            Integer actualTime = row[1] != null ? ((Number) row[1]).intValue() : 0;
            categoryMap.put(category, actualTime);
        }

        return categoryMap;
    }

    public List<Todo> getTodayTodos(Long userId) {
        return todoRepository.findTodayTodos(LocalDate.now(), userId);
    }
}
