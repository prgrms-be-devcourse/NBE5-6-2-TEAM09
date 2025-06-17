package com.grepp.codemap.routine.config;

import com.grepp.codemap.routine.service.RoutineHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationStartupListener {

    private final RoutineHistoryService routineHistoryService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("=== Application Started - Checking routine archive status ===");

        try {
            // 어제 날짜 확인
            LocalDate yesterday = LocalDate.now().minusDays(1);

            routineHistoryService.checkAndArchiveIfNeeded(yesterday);

            log.info("=== Routine archive check completed successfully ===");
        } catch (Exception e) {
            log.error("Error during startup routine archive check: {}", e.getMessage(), e);
            log.warn("Application will continue despite archive check failure");
        }
    }
}