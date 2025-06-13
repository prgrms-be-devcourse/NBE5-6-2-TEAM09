package com.grepp.codemap.coverletter.dto;

import com.grepp.codemap.coverletter.domain.CoverLetter;
import lombok.Getter;

@Getter
public class CoverLetterResponseDto {

    private Long id;
    private String title;
    private boolean isCompleted;

    private String q1;
    private String q2;
    private String q3;
    private String q4;
    private String q5;
    private String q6;
    private String q7;
    private String q8;

    public CoverLetterResponseDto(CoverLetter entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.isCompleted = entity.isCompleted();
        this.q1 = entity.getQ1();
        this.q2 = entity.getQ2();
        this.q3 = entity.getQ3();
        this.q4 = entity.getQ4();
        this.q5 = entity.getQ5();
        this.q6 = entity.getQ6();
        this.q7 = entity.getQ7();
        this.q8 = entity.getQ8();
    }
}
