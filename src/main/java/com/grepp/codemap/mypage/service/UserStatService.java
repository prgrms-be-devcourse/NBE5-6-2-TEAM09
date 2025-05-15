package com.grepp.codemap.mypage.service;

import com.grepp.codemap.infra.error.exceptions.CommonException;
import com.grepp.codemap.infra.response.ResponseCode;
import com.grepp.codemap.mypage.dto.UserStatDto;
import com.grepp.codemap.mypage.dto.UserUpdateRequestDto;
import com.grepp.codemap.mypage.repository.UserStatRepository;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserStatService {

    private final UserStatRepository userStatRepository;
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserStatDto getStatForUser(Long userId) {
        return userStatRepository.findByUserId(userId)
            .map(stat -> UserStatDto.builder()
                .totalFocusMinutes(stat.getTotalFocusMinutes())
                .dailyCompletionRateJson(stat.getDailyCompletionRate())
                .build())
            .orElse(UserStatDto.builder()
                .totalFocusMinutes(0)
                .dailyCompletionRateJson("{}")
                .build());
    }

    @Transactional
    public void updateUserInfo(Long userId, UserUpdateRequestDto dto) {

    }

    @Transactional
    public void updateNotificationSetting(Long userId, Boolean enabled) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));
        user.setNotificationEnabled(enabled);
    }
}
