package com.grepp.codemap.user.controller;

import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.dto.NicknameUpdateDto;
import com.grepp.codemap.user.dto.PasswordUpdateDto;
import com.grepp.codemap.user.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class UserSettingController {

    private final UserService userService;

    @PostMapping("/nickname")
    public String updateNickname(
        Principal principal,
        @ModelAttribute NicknameUpdateDto dto,
        RedirectAttributes redirectAttributes) {

        String email = principal.getName();
        User user = userService.findByEmail(email);
        userService.updateNickname(user.getId(), dto.getNickname());

        redirectAttributes.addFlashAttribute("message", "닉네임이 변경되었습니다.");
        return "redirect:/settings";
    }

    @PostMapping("/password")
    public String updatePassword(
        Principal principal,
        @ModelAttribute PasswordUpdateDto dto,
        RedirectAttributes redirectAttributes) {

        String email = principal.getName();
        User user = userService.findByEmail(email);
        userService.updatePassword(user.getId(), dto.getCurrentPassword(), dto.getNewPassword());

        redirectAttributes.addFlashAttribute("message", "비밀번호가 변경되었습니다.");
        return "redirect:/settings";
    }
}
