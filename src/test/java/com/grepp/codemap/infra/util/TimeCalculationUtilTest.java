package com.grepp.codemap.infra.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TimeCalculationUtil 단위 테스트")
class TimeCalculationUtilTest {

    @Test
    @DisplayName("정확히 1분 계산")
    void calculateDurationInMinutes_ExactlyOneMinute() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 10, 1, 0);

        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("1분 미만은 0분으로 계산")
    void calculateDurationInMinutes_LessThanOneMinute() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 10, 0, 59);

        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("1분 1초는 2분으로 올림 계산")
    void calculateDurationInMinutes_OneMinuteOneSecond() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 10, 1, 1);

        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("포모도로 25분 정확히 계산")
    void calculateDurationInMinutes_Pomodoro25Minutes() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 10, 25, 0);

        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(25);
    }

    @Test
    @DisplayName("25분 30초는 26분으로 올림 계산")
    void calculateDurationInMinutes_25Minutes30Seconds() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 10, 25, 30);

        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(26);
    }

    @Test
    @DisplayName("시작 시간이 null인 경우 0분 반환")
    void calculateDurationInMinutes_StartTimeNull() {
        // given
        LocalDateTime startTime = null;
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 10, 25, 0);

        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("종료 시간이 null인 경우 0분 반환")
    void calculateDurationInMinutes_EndTimeNull() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime endTime = null;

        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("둘 다 null인 경우 0분 반환")
    void calculateDurationInMinutes_BothNull() {
        // given
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("하루를 넘는 긴 시간도 정확히 계산")
    void calculateDurationInMinutes_LongDuration() {
        // given - 25시간 30분
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 2, 11, 30, 0);

        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(1530); // 25 * 60 + 30 = 1530분
    }

    // === secondsToMinutes 메서드 테스트 ===

    @Test
    @DisplayName("60초는 1분으로 계산")
    void secondsToMinutes_ExactlyOneMinute() {
        // given
        Long seconds = 60L;

        // when
        Integer result = TimeCalculationUtil.secondsToMinutes(seconds);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("59초는 0분으로 계산")
    void secondsToMinutes_LessThanOneMinute() {
        // given
        Long seconds = 59L;

        // when
        Integer result = TimeCalculationUtil.secondsToMinutes(seconds);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("61초는 2분으로 올림 계산")
    void secondsToMinutes_61Seconds() {
        // given
        Long seconds = 61L;

        // when
        Integer result = TimeCalculationUtil.secondsToMinutes(seconds);

        // then
        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("포모도로 25분(1500초) 계산")
    void secondsToMinutes_Pomodoro25Minutes() {
        // given
        Long seconds = 1500L; // 25 * 60

        // when
        Integer result = TimeCalculationUtil.secondsToMinutes(seconds);

        // then
        assertThat(result).isEqualTo(25);
    }

    @Test
    @DisplayName("null 초는 0분 반환")
    void secondsToMinutes_NullSeconds() {
        // given
        Long seconds = null;

        // when
        Integer result = TimeCalculationUtil.secondsToMinutes(seconds);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("0초는 0분 반환")
    void secondsToMinutes_ZeroSeconds() {
        // given
        Long seconds = 0L;

        // when
        Integer result = TimeCalculationUtil.secondsToMinutes(seconds);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("음수 초는 0분 반환")
    void secondsToMinutes_NegativeSeconds() {
        // given
        Long seconds = -100L;

        // when
        Integer result = TimeCalculationUtil.secondsToMinutes(seconds);

        // then
        assertThat(result).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("provideSecondsAndExpectedMinutes")
    @DisplayName("다양한 초 값에 대한 분 계산 테스트")
    void secondsToMinutes_VariousValues(Long seconds, Integer expectedMinutes) {
        // when
        Integer result = TimeCalculationUtil.secondsToMinutes(seconds);

        // then
        assertThat(result).isEqualTo(expectedMinutes);
    }

    static Stream<Arguments> provideSecondsAndExpectedMinutes() {
        return Stream.of(
            Arguments.of(30L, 0),     // 30초 → 0분
            Arguments.of(60L, 1),     // 1분 → 1분
            Arguments.of(90L, 2),     // 1분 30초 → 2분
            Arguments.of(120L, 2),    // 2분 → 2분
            Arguments.of(150L, 3),    // 2분 30초 → 3분
            Arguments.of(1500L, 25),  // 25분 → 25분
            Arguments.of(1501L, 26),  // 25분 1초 → 26분
            Arguments.of(3600L, 60),  // 1시간 → 60분
            Arguments.of(3661L, 62)   // 1시간 1분 1초 → 62분
        );
    }

    @ParameterizedTest
    @MethodSource("provideDurationTestCases")
    @DisplayName("다양한 시간 구간에 대한 분 계산 테스트")
    void calculateDurationInMinutes_VariousDurations(
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer expectedMinutes) {
        // when
        Integer result = TimeCalculationUtil.calculateDurationInMinutes(startTime, endTime);

        // then
        assertThat(result).isEqualTo(expectedMinutes);
    }

    static Stream<Arguments> provideDurationTestCases() {
        LocalDateTime baseTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);

        return Stream.of(
            // 기본 케이스들
            Arguments.of(baseTime, baseTime.plusSeconds(30), 0),     // 30초
            Arguments.of(baseTime, baseTime.plusMinutes(1), 1),      // 정확히 1분
            Arguments.of(baseTime, baseTime.plusSeconds(90), 2),     // 1분 30초
            Arguments.of(baseTime, baseTime.plusMinutes(5), 5),      // 5분 (휴식시간)
            Arguments.of(baseTime, baseTime.plusMinutes(25), 25),    // 25분 (포모도로)
            Arguments.of(baseTime, baseTime.plusSeconds(1501), 26),  // 25분 1초
            Arguments.of(baseTime, baseTime.plusHours(1), 60),       // 1시간
            Arguments.of(baseTime, baseTime.plusHours(2).plusMinutes(30), 150), // 2시간 30분

            // 경계값 테스트
            Arguments.of(baseTime, baseTime.plusSeconds(59), 0),     // 59초 (1분 미만)
            Arguments.of(baseTime, baseTime.plusSeconds(61), 2),     // 61초 (올림)
            Arguments.of(baseTime, baseTime.plusSeconds(119), 2),    // 119초 (2분 미만)
            Arguments.of(baseTime, baseTime.plusSeconds(121), 3)     // 121초 (올림)
        );
    }

    @Test
    @DisplayName("실제 포모도로 시나리오 - 25분 계획했지만 23분만 집중")
    void calculateDurationInMinutes_RealPomodoroScenario() {
        // given - 실제 포모도로 사용 시나리오
        LocalDateTime sessionStart = LocalDateTime.of(2025, 1, 15, 14, 30, 0);
        LocalDateTime sessionEnd = LocalDateTime.of(2025, 1, 15, 14, 53, 0); // 23분 후

        // when
        Integer actualMinutes = TimeCalculationUtil.calculateDurationInMinutes(sessionStart, sessionEnd);

        // then
        assertThat(actualMinutes).isEqualTo(23);
        assertThat(actualMinutes).isLessThan(25); // 계획한 25분보다 적음
    }

    @Test
    @DisplayName("실제 포모도로 시나리오 - 25분 계획했지만 27분 집중")
    void calculateDurationInMinutes_ExtendedPomodoroScenario() {
        // given - 계획보다 더 오래 집중한 경우
        LocalDateTime sessionStart = LocalDateTime.of(2025, 1, 15, 9, 0, 0);
        LocalDateTime sessionEnd = LocalDateTime.of(2025, 1, 15, 9, 27, 30); // 27분 30초

        // when
        Integer actualMinutes = TimeCalculationUtil.calculateDurationInMinutes(sessionStart, sessionEnd);

        // then
        assertThat(actualMinutes).isEqualTo(28); // 올림 처리
        assertThat(actualMinutes).isGreaterThan(25); // 계획한 25분보다 많음
    }
}