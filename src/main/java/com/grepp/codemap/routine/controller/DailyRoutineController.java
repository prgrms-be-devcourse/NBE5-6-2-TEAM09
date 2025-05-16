package com.grepp.codemap.routine.controller;

import com.grepp.codemap.routine.dto.DailyRoutineDto;
import com.grepp.codemap.routine.dto.PomodoroSessionDto;
import com.grepp.codemap.routine.service.DailyRoutineService;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public String getRoutineList(Model model, @SessionAttribute("userId") Long userId) {
        User user = userService.getUserById(userId);

        // 활성 루틴 목록
        List<DailyRoutineDto> activeRoutines = dailyRoutineService.getActiveRoutinesByUser(userId);
        // 완료된 루틴 목록
        List<DailyRoutineDto> completedRoutines = dailyRoutineService.getCompletedRoutinesByUser(userId);

        model.addAttribute("user", user);
        model.addAttribute("activeRoutines", activeRoutines);
        model.addAttribute("completedRoutines", completedRoutines);
        model.addAttribute("newRoutine", new DailyRoutineDto()); // 모달 폼용

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
    public String showEditForm(@PathVariable Long id, Model model,
        @SessionAttribute("userId") Long userId) {

        // 서비스 계층의 메서드를 사용하여 루틴 조회
        DailyRoutineDto routine = dailyRoutineService.getRoutineById(id, userId);

        model.addAttribute("routine", routine);
        return "routine/routine-form :: routineFormModal";
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
    public String deleteRoutine(@PathVariable Long id,
        @SessionAttribute("userId") Long userId,
        RedirectAttributes redirectAttributes) {

        try {
            dailyRoutineService.deleteRoutine(id, userId);
            redirectAttributes.addFlashAttribute("successMessage", "루틴이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("루틴 삭제 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "루틴 삭제에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/routines";
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

        try {
            // 루틴 정보 조회
            DailyRoutineDto routine = dailyRoutineService.getRoutineById(id, userId);

            // 포모도로 세션 시작
            PomodoroSessionDto session = dailyRoutineService.startPomodoroSession(id, userId);

            model.addAttribute("routine", routine);
            model.addAttribute("session", session);
            model.addAttribute("focusTime", routine.getFocusTime());
            model.addAttribute("breakTime", 10); // 기본 쉬는 시간 10분

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
        // 실제로는 클라이언트 측에서 일시정지 상태를 관리하고,
        // 필요한 경우 서버에 진행 상황을 저장할 수 있습니다.
        return ResponseEntity.ok().build();
    }

    //타이머 완료 처리
    @PatchMapping("/timer/complete")
    public String completeTimer(@RequestParam Long sessionId,
        @RequestParam Long routineId,
        @SessionAttribute("userId") Long userId,
        RedirectAttributes redirectAttributes) {

        try {
            // 포모도로 세션 종료
            PomodoroSessionDto endedSession = dailyRoutineService.endPomodoroSession(sessionId, userId);

            // 루틴 완료 처리
            DailyRoutineDto completedRoutine = dailyRoutineService.completeRoutine(routineId, userId);

            redirectAttributes.addFlashAttribute("successMessage", "루틴이 성공적으로 완료되었습니다!");
        } catch (Exception e) {
            log.error("타이머 완료 처리 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "타이머 완료 처리에 실패했습니다: " + e.getMessage());
        }

        return "redirect:/routines";
    }
}