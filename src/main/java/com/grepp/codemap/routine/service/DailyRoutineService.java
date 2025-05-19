package com.grepp.codemap.routine.service;

import com.grepp.codemap.routine.domain.DailyRoutine;
import com.grepp.codemap.routine.domain.PomodoroSession;
import com.grepp.codemap.routine.dto.DailyRoutineDto;
import com.grepp.codemap.routine.dto.PomodoroSessionDto;
import com.grepp.codemap.routine.repository.DailyRoutineRepository;
import com.grepp.codemap.routine.repository.PomodoroSessionRepository;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyRoutineService {

    private final DailyRoutineRepository dailyRoutineRepository;
    private final PomodoroSessionRepository pomodoroSessionRepository;
    private final UserService userService;

    // 사용자의 모든 활성 루틴 조회
    public List<DailyRoutineDto> getActiveRoutinesByUser(Long userId) {
        User user = userService.getUserById(userId);
        return dailyRoutineRepository.findActiveRoutinesByUser(user)
            .stream()
            .map(DailyRoutineDto::fromEntity)
            .collect(Collectors.toList());
    }

    // 사용자의 모든 완료된 루틴 조회
    public List<DailyRoutineDto> getCompletedRoutinesByUser(Long userId) {
        User user = userService.getUserById(userId);
        return dailyRoutineRepository.findCompletedRoutinesByUser(user)
            .stream()
            .map(DailyRoutineDto::fromEntity)
            .collect(Collectors.toList());
    }

    // ID로 루틴 단건 조회
    public DailyRoutineDto getRoutineById(Long routineId, Long userId) {
        DailyRoutine routine = dailyRoutineRepository.findById(routineId)
            .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + routineId));

        // 권한 검증 - 요청한 사용자의 루틴인지 확인
        if (!routine.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to access this routine");
        }

        return DailyRoutineDto.fromEntity(routine);
    }

    // 루틴 추가
    @Transactional
    public DailyRoutineDto createRoutine(DailyRoutineDto routineDto, Long userId) {
        User user = userService.getUserById(userId);

        DailyRoutine routine = DailyRoutine.builder()
            .user(user)
            .category(routineDto.getCategory())
            .title(routineDto.getTitle())
            .description(routineDto.getDescription())
            .focusTime(routineDto.getFocusTime())
            .status("ACTIVE")
            .isDeleted(false)
            .build();

        DailyRoutine savedRoutine = dailyRoutineRepository.save(routine);
        return DailyRoutineDto.fromEntity(savedRoutine);
    }

    // 루틴 수정
    @Transactional
    public DailyRoutineDto updateRoutine(Long routineId, DailyRoutineDto routineDto, Long userId) {
        User user = userService.getUserById(userId);
        DailyRoutine routine = dailyRoutineRepository.findById(routineId)
            .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + routineId));

        // 권한 검증
        if (!routine.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You don't have permission to update this routine");
        }

        // 기존 루틴을 삭제하고 새로운 루틴 생성 (Immutable 패턴)
        DailyRoutine updatedRoutine = DailyRoutine.builder()
            .id(routine.getId())
            .user(user)
            .category(routineDto.getCategory())
            .title(routineDto.getTitle())
            .description(routineDto.getDescription())
            .focusTime(routineDto.getFocusTime())
            .status(routine.getStatus())
            .startedAt(routine.getStartedAt())
            .completedAt(routine.getCompletedAt())
            .isDeleted(routine.getIsDeleted())
            .createdAt(routine.getCreatedAt())
            .build();

        DailyRoutine saved = dailyRoutineRepository.save(updatedRoutine);
        return DailyRoutineDto.fromEntity(saved);
    }

    // 루틴 삭제 (소프트 딜리트)
    @Transactional
    public void deleteRoutine(Long routineId, Long userId) {
        User user = userService.getUserById(userId);
        DailyRoutine routine = dailyRoutineRepository.findById(routineId)
            .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + routineId));

        // 권한 검증
        if (!routine.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You don't have permission to delete this routine");
        }

        // 소프트 딜리트 처리
        DailyRoutine deletedRoutine = DailyRoutine.builder()
            .id(routine.getId())
            .user(user)
            .category(routine.getCategory())
            .title(routine.getTitle())
            .description(routine.getDescription())
            .focusTime(routine.getFocusTime())
            .status(routine.getStatus())
            .startedAt(routine.getStartedAt())
            .completedAt(routine.getCompletedAt())
            .isDeleted(true) // 삭제 표시
            .createdAt(routine.getCreatedAt())
            .build();

        dailyRoutineRepository.save(deletedRoutine);
    }

    // 루틴 완료 처리
    @Transactional
    public DailyRoutineDto completeRoutine(Long routineId, Long userId) {
        User user = userService.getUserById(userId);
        DailyRoutine routine = dailyRoutineRepository.findById(routineId)
            .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + routineId));

        // 권한 검증
        if (!routine.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You don't have permission to complete this routine");
        }

        DailyRoutine completedRoutine = DailyRoutine.builder()
            .id(routine.getId())
            .user(user)
            .category(routine.getCategory())
            .title(routine.getTitle())
            .description(routine.getDescription())
            .focusTime(routine.getFocusTime())
            .status("COMPLETED") // 완료 상태로 변경
            .startedAt(routine.getStartedAt())
            .completedAt(LocalDateTime.now()) // 완료 시간 설정
            .isDeleted(routine.getIsDeleted())
            .createdAt(routine.getCreatedAt())
            .build();

        DailyRoutine saved = dailyRoutineRepository.save(completedRoutine);
        return DailyRoutineDto.fromEntity(saved);
    }

    // 루틴 PASS 처리 (쉬어가기)
    @Transactional
    public DailyRoutineDto passRoutine(Long routineId, Long userId) {
        User user = userService.getUserById(userId);
        DailyRoutine routine = dailyRoutineRepository.findById(routineId)
            .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + routineId));

        // 권한 검증
        if (!routine.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You don't have permission to pass this routine");
        }

        DailyRoutine passedRoutine = DailyRoutine.builder()
            .id(routine.getId())
            .user(user)
            .category(routine.getCategory())
            .title(routine.getTitle())
            .description(routine.getDescription())
            .focusTime(routine.getFocusTime())
            .status("PASS") // PASS 상태로 변경
            .startedAt(routine.getStartedAt())
            .completedAt(routine.getCompletedAt())
            .isDeleted(routine.getIsDeleted())
            .createdAt(routine.getCreatedAt())
            .build();

        DailyRoutine saved = dailyRoutineRepository.save(passedRoutine);
        return DailyRoutineDto.fromEntity(saved);
    }

    // 루틴 완료 취소
    @Transactional
    public DailyRoutineDto cancelCompletion(Long routineId, Long userId) {
        User user = userService.getUserById(userId);
        DailyRoutine routine = dailyRoutineRepository.findById(routineId)
            .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + routineId));

        // 권한 검증
        if (!routine.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You don't have permission to cancel this routine");
        }

        DailyRoutine activeRoutine = DailyRoutine.builder()
            .id(routine.getId())
            .user(user)
            .category(routine.getCategory())
            .title(routine.getTitle())
            .description(routine.getDescription())
            .focusTime(routine.getFocusTime())
            .status("ACTIVE") // 활성 상태로 되돌림
            .startedAt(routine.getStartedAt())
            .completedAt(null) // 완료 시간 제거
            .isDeleted(routine.getIsDeleted())
            .createdAt(routine.getCreatedAt())
            .build();

        DailyRoutine saved = dailyRoutineRepository.save(activeRoutine);
        return DailyRoutineDto.fromEntity(saved);
    }

    // 포모도로 세션 시작
    @Transactional
    public PomodoroSessionDto startPomodoroSession(Long routineId, Long userId) {
        User user = userService.getUserById(userId);
        DailyRoutine routine = dailyRoutineRepository.findById(routineId)
            .orElseThrow(() -> new IllegalArgumentException("Routine not found with id: " + routineId));

        // 권한 검증
        if (!routine.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You don't have permission to start pomodoro for this routine");
        }

        // 루틴 시작 시간 설정
        if (routine.getStartedAt() == null) {
            DailyRoutine startedRoutine = DailyRoutine.builder()
                .id(routine.getId())
                .user(user)
                .category(routine.getCategory())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .focusTime(routine.getFocusTime())
                .status(routine.getStatus())
                .startedAt(LocalDateTime.now()) // 시작 시간 설정
                .completedAt(routine.getCompletedAt())
                .isDeleted(routine.getIsDeleted())
                .createdAt(routine.getCreatedAt())
                .build();

            dailyRoutineRepository.save(startedRoutine);
        }

        // 포모도로 세션 생성
        PomodoroSession session = PomodoroSession.builder()
            .routine(routine)
            .durationMinutes(25) // 기본 25분 설정 (변경 가능)
            .startedAt(LocalDateTime.now())
            .build();

        PomodoroSession savedSession = pomodoroSessionRepository.save(session);
        return PomodoroSessionDto.fromEntity(savedSession);
    }

    // 포모도로 세션 종료
    @Transactional
    public PomodoroSessionDto endPomodoroSession(Long sessionId, Long userId) {
        PomodoroSession session = pomodoroSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found with id: " + sessionId));

        // 권한 검증
        if (!session.getRoutine().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to end this session");
        }

        PomodoroSession endedSession = PomodoroSession.builder()
            .id(session.getId())
            .routine(session.getRoutine())
            .durationMinutes(session.getDurationMinutes())
            .startedAt(session.getStartedAt())
            .endedAt(LocalDateTime.now())
            .build();

        PomodoroSession saved = pomodoroSessionRepository.save(endedSession);
        return PomodoroSessionDto.fromEntity(saved);
    }


    public List<DailyRoutineDto> getPassedRoutinesByUser(Long userId) {
        User user = userService.getUserById(userId);
        return dailyRoutineRepository.findPassedRoutinesByUser(user)
            .stream()
            .map(DailyRoutineDto::fromEntity)
            .collect(Collectors.toList());
    }

    public DailyRoutineDto getNextRoutine(Long currentRoutineId, Long userId) {
        User user = userService.getUserById(userId);
        DailyRoutine currentRoutine = dailyRoutineRepository.findById(currentRoutineId)
            .orElseThrow(() -> new IllegalArgumentException("Routine not found"));

        List<DailyRoutine> activeRoutines = dailyRoutineRepository.findByUserAndStatusAndNotDeleted(user, "ACTIVE");
        return activeRoutines.stream()
            .filter(routine -> routine.getCreatedAt().isAfter(currentRoutine.getCreatedAt()))
            .min((r1, r2) -> r1.getCreatedAt().compareTo(r2.getCreatedAt()))
            .map(DailyRoutineDto::fromEntity)
            .orElse(null);
    }
}