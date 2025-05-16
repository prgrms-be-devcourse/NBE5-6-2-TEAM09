package com.grepp.codemap.mypage.service;

import com.grepp.codemap.mypage.dto.RoutineCategoryCompletionDto;
import com.grepp.codemap.mypage.dto.RoutineCompletionDto;
import com.grepp.codemap.routine.repository.DailyRoutineRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final DailyRoutineRepository dailyRoutineRepository;

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
}
