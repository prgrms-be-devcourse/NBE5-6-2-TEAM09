package com.grepp.codemap.mypage.controller;

import com.grepp.codemap.mypage.dto.UserStatDto;
import com.grepp.codemap.mypage.service.UserStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserStatService userStatService;

    @GetMapping("/stats/{userId}")
    public UserStatDto getUserStats(@PathVariable Long userId) {
        return userStatService.getStatForUser(userId);
    }

    @PatchMapping("/settings/notification")
    public String updateNotification(@SessionAttribute("userId") Long userId,
        @RequestParam("notificationEnabled") Boolean enabled) {
        userStatService.updateNotificationSetting(userId, enabled);
        return "redirect:/settings"; // ← 명세서에 따라 settings 페이지로 리다이렉트
    }
}
