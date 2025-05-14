package com.grepp.codemap.mypage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserStatDto {

    private int totalFocusMinutes;          // 총 순공 시간
    private String dailyCompletionRateJson; // 카테고리별 완료율(Json)
}
