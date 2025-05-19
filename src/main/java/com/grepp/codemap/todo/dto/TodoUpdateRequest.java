package com.grepp.codemap.todo.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 투두 수정 요청 DTO
 */
public record TodoUpdateRequest(
    @NotBlank(message = "제목을 입력해주세요.")
    String title,

    String description,

    @NotNull
    String startTime,

    @NotNull(message = "완료 시간을 입력해주세요.")
    String completedAt
) {}