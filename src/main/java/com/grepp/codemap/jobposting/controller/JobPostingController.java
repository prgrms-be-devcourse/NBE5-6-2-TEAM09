package com.grepp.codemap.jobposting.controller;

import com.grepp.codemap.jobposting.domain.JobPosting;
import com.grepp.codemap.jobposting.dto.JobPostingRequest;
import com.grepp.codemap.jobposting.service.JobPostingService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    // ✅ 1. 목표기업 등록
    @PostMapping
    public ResponseEntity<Void> createJobPosting(@RequestBody JobPostingRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        jobPostingService.createJobPosting(userId, request);
        return ResponseEntity.ok().build();
    }

    // ✅ 2. 목표기업 목록 조회
    @GetMapping
    public ResponseEntity<List<JobPosting>> getJobPostings(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<JobPosting> postings = jobPostingService.getJobPostings(userId);
        return ResponseEntity.ok(postings);
    }

    // ✅ 3. 목표기업 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        jobPostingService.deleteJobPosting(id, userId);
        return ResponseEntity.ok().build();
    }
}
