package com.grepp.codemap.mypage.controller;

import com.grepp.codemap.mypage.dto.UserStatDto;
import com.grepp.codemap.mypage.service.UserStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserStatService userStatService;

    @GetMapping("/stats/{userId}")
    public UserStatDto getUserStats(@PathVariable Long userId) {
        return userStatService.getStatForUser(userId);
    }
}
