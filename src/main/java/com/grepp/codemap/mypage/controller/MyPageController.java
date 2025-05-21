package com.grepp.codemap.mypage.controller;

import com.grepp.codemap.mypage.dto.RoutineCategoryCompletionDto;
import com.grepp.codemap.mypage.dto.RoutineCompletionDto;
import com.grepp.codemap.mypage.dto.UserStatDto;
import com.grepp.codemap.mypage.service.MyPageService;
import com.grepp.codemap.mypage.service.UserStatService;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserStatService userStatService;
    private final MyPageService myPageService;

    @GetMapping("/stats/{userId}")
    public UserStatDto getUserStats(@PathVariable Long userId) {
        return userStatService.getStatForUser(userId);
    }

    @GetMapping("completion-rate")
    public ResponseEntity<RoutineCompletionDto> getRoutineCompletionRate(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(myPageService.getRoutineCompletionStats(user.getId()));
    }

    @GetMapping("completion-rate/category")
    public ResponseEntity<List<RoutineCategoryCompletionDto>> getRoutineCompletionRateByCategory(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(myPageService.getRoutineCompletionStatsByCategory(user.getId()));
    }
}
