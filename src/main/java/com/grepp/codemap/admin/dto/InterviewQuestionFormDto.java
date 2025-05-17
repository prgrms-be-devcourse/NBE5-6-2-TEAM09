package com.grepp.codemap.admin.dto;

import com.grepp.codemap.interview.domain.InterviewQuestion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewQuestionFormDto {
    private Long id;
    private String category;
    private String difficulty;
    private String questionText;
    private String answerText;

    public InterviewQuestionFormDto() {}

    public static InterviewQuestionFormDto from(InterviewQuestion entity) {
        InterviewQuestionFormDto dto = new InterviewQuestionFormDto();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setDifficulty(entity.getDifficulty());
        dto.setQuestionText(entity.getQuestionText());
        dto.setAnswerText(entity.getAnswerText());
        return dto;
    }

    public InterviewQuestion toEntity() {
        return InterviewQuestion.builder()
                .id(this.id)
                .category(this.category)
                .difficulty(this.difficulty)
                .questionText(this.questionText)
                .answerText(this.answerText)
                .build();
    }
}

