package com.grepp.codemap.routine.controller;

import com.grepp.codemap.routine.dto.DailyRoutineDto;
import com.grepp.codemap.routine.dto.PomodoroSessionDto;
import com.grepp.codemap.routine.service.DailyRoutineService;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.service.UserService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/routines")
@Slf4j
public class DailyRoutineController {

    private final DailyRoutineService dailyRoutineService;
    private final UserService userService;


    //루틴 목록 조회 페이지
    @GetMapping
    public String getRoutineList(Model model,
        @SessionAttribute(name = "userId", required = false) Long userId,
        @RequestParam(name = "date", required = false) String dateStr) {
        if (userId == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/user/signin";
        }

        // 날짜가 없으면 오늘 날짜를 기본값으로 사용
        LocalDate selectedDate = LocalDate.now();
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                selectedDate = LocalDate.parse(dateStr);
            } catch (Exception e) {
                log.error("Invalid date format: " + dateStr, e);
            }
        }

        // 서비스를 통해 해당 날짜의 루틴 데이터 가져오기
        Map<String, List<DailyRoutineDto>> routinesByStatus =
            dailyRoutineService.getRoutinesByDate(userId, selectedDate);

        User user = userService.getUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("activeRoutines", routinesByStatus.get("active"));
        model.addAttribute("completedRoutines", routinesByStatus.get("completed"));
        model.addAttribute("passedRoutines", routinesByStatus.get("passed"));
        model.addAttribute("newRoutine", new DailyRoutineDto());
        model.addAttribute("selectedDate", selectedDate);

        return "routine/routine-list";

    }


    //루틴 추가 폼 모달
    @GetMapping("/new")
    public String showAddForm(Model model, @SessionAttribute("userId") Long userId) {
        model.addAttribute("routine", new DailyRoutineDto());
        return "routine/routine-form :: routineFormModal";
    }

    //루틴 추가 처리
    @PostMapping
    public String addRoutine(@ModelAttribute("routine") DailyRoutineDto routineDto,
        @SessionAttribute("userId") Long userId,
        RedirectAttributes redirectAttributes) {

        try {
            DailyRoutineDto savedRoutine = dailyRoutineService.createRoutine(routineDto, userId);
            redirectAttributes.addFlashAttribute("successMessage", "루틴이 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            log.error("루틴 추가 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "루틴 추가에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/routines";
    }

    ///루틴 수정 폼 모달
    @GetMapping("/{id}/edit")
    @ResponseBody
    public ResponseEntity<?> showEditForm(@PathVariable Long id,
        @SessionAttribute("userId") Long userId) {
        try {
            // 서비스 계층의 메서드를 사용하여 루틴 조회
            DailyRoutineDto routine = dailyRoutineService.getRoutineById(id, userId);
            return ResponseEntity.ok(routine);  // JSON으로 응답
        } catch (Exception e) {
            log.error("루틴 조회 중 오류 발생", e);
            return ResponseEntity.status(500).body("루틴 정보를 가져오는데 실패했습니다");
        }


    }


    //루틴 수정 처리
    @PatchMapping("/{id}")
    public String updateRoutine(@PathVariable Long id,
        @ModelAttribute("routine") DailyRoutineDto routineDto,
        @SessionAttribute("userId") Long userId,
        RedirectAttributes redirectAttributes) {

        try {
            DailyRoutineDto updatedRoutine = dailyRoutineService.updateRoutine(id, routineDto, userId);
            redirectAttributes.addFlashAttribute("successMessage", "루틴이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("루틴 수정 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "루틴 수정에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/routines";
    }


    //루틴 삭제 처리
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteRoutine(@PathVariable Long id,
        @SessionAttribute("userId") Long userId) {

        try {
            dailyRoutineService.deleteRoutine(id, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "루틴이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("루틴 삭제 중 오류 발생", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "루틴 삭제에 실패했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    //루틴 완료 처리
    @PatchMapping("/{id}/complete")
    public String completeRoutine(@PathVariable Long id,
        @SessionAttribute("userId") Long userId,
        RedirectAttributes redirectAttributes) {

        try {
            DailyRoutineDto completedRoutine = dailyRoutineService.completeRoutine(id, userId);
            redirectAttributes.addFlashAttribute("successMessage", "루틴이 완료 처리되었습니다.");
        } catch (Exception e) {
            log.error("루틴 완료 처리 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "루틴 완료 처리에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/routines";
    }


    //루틴 완료 취소 처리
    @PatchMapping("/{id}/cancel")
    public String cancelRoutineCompletion(@PathVariable Long id,
        @SessionAttribute("userId") Long userId,
        RedirectAttributes redirectAttributes) {

        try {
            DailyRoutineDto canceledRoutine = dailyRoutineService.cancelCompletion(id, userId);
            redirectAttributes.addFlashAttribute("successMessage", "루틴 완료가 취소되었습니다.");
        } catch (Exception e) {
            log.error("루틴 완료 취소 처리 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "루틴 완료 취소에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/routines";
    }


    //루틴 쉬어가기(Skip) 처리
    @PatchMapping("/{id}/skip")
    public String skipRoutine(@PathVariable Long id,
        @SessionAttribute("userId") Long userId,
        RedirectAttributes redirectAttributes) {

        try {
            DailyRoutineDto passedRoutine = dailyRoutineService.passRoutine(id, userId);
            redirectAttributes.addFlashAttribute("successMessage", "루틴을 쉬어가기로 설정했습니다.");
        } catch (Exception e) {
            log.error("루틴 쉬어가기 처리 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "루틴 쉬어가기 설정에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/routines";
    }


    //타이머 페이지 로드 및 포모도로 세션 시작
    @GetMapping("/{id}/timer")
    public String startTimer(@PathVariable Long id, Model model,
        @SessionAttribute("userId") Long userId) {

        User user = userService.getUserById(userId);

        model.addAttribute("user", user);
        try {
            // 루틴 정보 조회
            DailyRoutineDto routine = dailyRoutineService.getRoutineById(id, userId);

            // 포모도로 세션 시작
            PomodoroSessionDto pomodoroSession  = dailyRoutineService.startPomodoroSession(id, userId);

            // 다음 루틴 정보 조회
            DailyRoutineDto nextRoutine = dailyRoutineService.getNextRoutine(id, userId);

            model.addAttribute("routine", routine);
            model.addAttribute("pomodoroSession", pomodoroSession );
            model.addAttribute("focusTime", routine.getFocusTime());
            model.addAttribute("nextRoutine", nextRoutine);

            return "routine/timer";
        } catch (Exception e) {
            log.error("타이머 시작 중 오류 발생", e);
            return "redirect:/routines";
        }
    }

    //타이머 일시정지 처리 (AJAX)
    @PatchMapping("/timer/pause")
    @ResponseBody
    public ResponseEntity<?> pauseTimer(@RequestParam Long sessionId,
        @SessionAttribute("userId") Long userId) {
        // 타이머 일시정지는 js로 구현

        return ResponseEntity.ok().build();
    }

    //타이머 완료 처리
    @PatchMapping("/timer/complete")
    public String completeTimer(@RequestParam Long sessionId,
        @RequestParam Long routineId,
        @RequestParam(required = false) Long nextRoutineId,
        @SessionAttribute("userId") Long userId,
        RedirectAttributes redirectAttributes) {

        try {
            // 포모도로 세션 종료
            PomodoroSessionDto endedSession = dailyRoutineService.endPomodoroSession(sessionId, userId);

            // 루틴 완료 처리
            DailyRoutineDto completedRoutine = dailyRoutineService.completeRoutine(routineId, userId);

            redirectAttributes.addFlashAttribute("successMessage", "루틴이 성공적으로 완료되었습니다!");

            // 다음 루틴이 있는 경우 해당 루틴의 타이머 페이지로 이동
            if (nextRoutineId != null) {
                return "redirect:/routines/" + nextRoutineId + "/timer";
            }
        } catch (Exception e) {
            log.error("타이머 완료 처리 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "타이머 완료 처리에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/routines";
    }
}