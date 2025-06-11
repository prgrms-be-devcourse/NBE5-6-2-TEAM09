package com.grepp.codemap.user.controller;

import com.grepp.codemap.infra.auth.Role;
import com.grepp.codemap.infra.error.exceptions.CommonException;
import com.grepp.codemap.routine.dto.DailyRoutineDto;
import com.grepp.codemap.routine.service.DailyRoutineService;
import com.grepp.codemap.todo.dto.TodoResponse;
import com.grepp.codemap.todo.service.TodoService;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.form.SigninForm;
import com.grepp.codemap.user.form.SignupForm;
import com.grepp.codemap.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final DailyRoutineService dailyRoutineService;
    private final TodoService todoService;


    @GetMapping("signin")
    public String signin(SigninForm form, Model model) {
        return "user/signin";
    }

    @GetMapping("/main")
    public String main(HttpSession session, Model model) {
        // 세션 확인
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/signin";
        }

        try {
            // 사용자 정보 조회
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);

            // 오늘 날짜 기준으로 루틴 조회
            LocalDate today = LocalDate.now();
            Map<String, List<DailyRoutineDto>> routinesByStatus =
                dailyRoutineService.getRoutinesByDate(userId, today);

            // 오늘의 투두 조회
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
            List<TodoResponse> todayTodos = todoService.getTodosByDate(userId, startOfDay, endOfDay);

            // 통계 데이터 계산
            List<DailyRoutineDto> completedRoutines = routinesByStatus.get("completed");
            List<DailyRoutineDto> activeRoutines = routinesByStatus.get("active");
            List<DailyRoutineDto> passedRoutines = routinesByStatus.get("passed");

            // 오늘의 총 집중시간 계산 (완료된 루틴들의 집중시간 합)
            int totalFocusMinutes = completedRoutines.stream()
                .mapToInt(routine -> routine.getFocusTime() != null ? routine.getFocusTime() : 0)
                .sum();

            // 전체 루틴 수와 완료된 루틴 수
            int totalRoutines = activeRoutines.size() + completedRoutines.size() + passedRoutines.size();
            int completedRoutineCount = completedRoutines.size();

            // 완료율 계산
            double completionRate = totalRoutines > 0 ? (double) completedRoutineCount / totalRoutines * 100 : 0;

            // 모델에 데이터 추가
            model.addAttribute("activeRoutines", activeRoutines);
            model.addAttribute("completedRoutines", completedRoutines);
            model.addAttribute("passedRoutines", passedRoutines);
            model.addAttribute("todayTodos", todayTodos);
            model.addAttribute("totalFocusMinutes", totalFocusMinutes);
            model.addAttribute("totalRoutines", totalRoutines);
            model.addAttribute("completedRoutineCount", completedRoutineCount);
            model.addAttribute("completionRate", Math.round(completionRate));
            model.addAttribute("today", today);

        } catch (Exception e) {
            log.error("메인 페이지 로딩 중 오류 발생", e);
            return "redirect:/user/signin";
        }

        return "user/main";
    }

    @PostMapping("signin")
    public String signin(
            @Valid SigninForm form,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "user/signin";
        }

        try {
            // 로그인 성공 시
            User user = userService.login(form);

            // ✅ 세션 저장
            session.setAttribute("userId", user.getId());

            // 역할에 따른 SimpleGrantedAuthority 생성 - 조건부 접두사 추가
            SimpleGrantedAuthority authority;

            if (user.getRole().startsWith("ROLE_")) {
                authority = new SimpleGrantedAuthority(user.getRole());
            } else {
                authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
            }

            // ✅ SecurityContext 수동 인증 설정
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    null,
                    List.of(authority) // 예: ROLE_USER
                );
            // ✅ 핵심: SecurityContext를 세션에 직접 저장해야 유지됨
            SecurityContextImpl securityContext = new SecurityContextImpl(authentication);
            session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
            );

            // 사용자 역할 체크 및 리다이렉트
            if (user.getRole().equals("ROLE_ADMIN") ||
                user.getRole().equals(Role.ROLE_ADMIN.name())) {
                return "redirect:/admin/users";
            }
            return "redirect:/user/main";
        } catch (CommonException e) {
            model.addAttribute("loginError", "이메일 또는 비밀번호가 일치하지 않습니다.");
            return "user/signin";
        }
    }


    @GetMapping("signup")
    public String signup(SignupForm form){
        return "user/signup";
    }

    @PostMapping("signup")
    public String signup(
            @Valid SignupForm form,
            BindingResult bindingResult,
            Model model){


        if(bindingResult.hasErrors()){
            return "user/signup";
        }

        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "error.passwordConfirm", "비밀번호가 일치하지 않습니다.");
            return "user/signup";
        }

        log.info("회원가입 요청: email={}, password={}, passwordConfirm={}",
            form.getEmail(), form.getPassword(), form.getPasswordConfirm());

        userService.signup(form.toDto(), Role.ROLE_USER);
        return "redirect:/";
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 삭제
        }

        SecurityContextHolder.clearContext(); // Security 인증 정보 제거
        return "redirect:/user/signin";
    }
}