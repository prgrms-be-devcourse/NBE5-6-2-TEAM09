package com.grepp.codemap.user.controller;

import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.dto.NicknameUpdateDto;
import com.grepp.codemap.user.dto.PasswordUpdateDto;
import com.grepp.codemap.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class UserSettingController {

    private final UserService userService;

    @PostMapping("/nickname")
    public ResponseEntity<String> updateNickname(
        @AuthenticationPrincipal
        User user,
        @ModelAttribute
        NicknameUpdateDto dto) {
        userService.updateNickname(user.getId(), dto.getNickname());
        return ResponseEntity.ok("닉네임이 변경되었습니다.");
    }

    @PostMapping("/password")
    public ResponseEntity<String> updatePassword(
        @AuthenticationPrincipal
        User user,
        @RequestBody
        PasswordUpdateDto dto) {
        userService.updatePassword(user.getId(), dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }
}
