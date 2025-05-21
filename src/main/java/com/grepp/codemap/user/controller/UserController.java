package com.grepp.codemap.user.controller;

import com.grepp.codemap.infra.auth.Role;
import com.grepp.codemap.infra.error.exceptions.CommonException;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.form.SigninForm;
import com.grepp.codemap.user.form.SignupForm;
import com.grepp.codemap.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
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


    @GetMapping("signin")
    public String signin(SigninForm form, Model model) {
        return "user/signin";
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
            return "redirect:/routines";
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