package com.grepp.codemap.jobposting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class JobPostingRequest {

    private String companyName;
    private LocalDate deadline;

    public JobPostingRequest(String companyName, LocalDate deadline) {
        this.companyName = companyName;
        this.deadline = deadline;
    }
}
