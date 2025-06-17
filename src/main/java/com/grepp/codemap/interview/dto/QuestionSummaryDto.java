package com.grepp.codemap.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSummaryDto {

    private Long id;
    private String category;
    private String difficulty;
    private String questionText;

}
