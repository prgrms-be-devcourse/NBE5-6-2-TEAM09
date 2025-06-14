package com.grepp.codemap.jobposting.controller;

import com.grepp.codemap.jobposting.service.JobPostingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class JobPostingPageController {

    private final JobPostingService jobPostingService;

    /**
     * 채용 공고 안내 페이지 출력 (목표기업 리스트 포함)
     */
    @GetMapping("/jobposting/list")
    public String showJobPostingList(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("jobPostings", jobPostingService.getJobPostings(userId));
        return "jobposting/jobposting-list";  // templates/jobposting/jobposting-list.html
    }
}
