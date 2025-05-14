package com.grepp.codemap.user.controller;

import com.grepp.codemap.infra.auth.Role;
import com.grepp.codemap.user.form.SigninForm;
import com.grepp.codemap.user.form.SignupForm;
import com.grepp.codemap.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("signin")
    public String signin(SigninForm form, Model model) {
        return "user/signin";
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

//        if (!form.getPassword().equals(form.getPasswordConfirm())) {
//            bindingResult.rejectValue("passwordConfirm", "error.passwordConfirm", "비밀번호가 일치하지 않습니다.");
//            return "user/signup";
//        }

//        userService.signup(form.toDto(), Role.ROLE_USER);
        return "redirect:/";
    }
}