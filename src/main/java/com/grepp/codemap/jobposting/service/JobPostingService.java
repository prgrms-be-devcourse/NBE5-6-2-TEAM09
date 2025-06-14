package com.grepp.codemap.jobposting.service;

import com.grepp.codemap.jobposting.domain.JobPosting;
import com.grepp.codemap.jobposting.dto.JobPostingRequest;
import com.grepp.codemap.jobposting.repository.JobPostingRepository;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;

    // ✅ 1. 목표기업 등록
    public void createJobPosting(Long userId, JobPostingRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        JobPosting jobPosting = JobPosting.builder()
            .user(user)
            .title(request.getCompanyName())
            .dueDate(request.getDeadline())
            .isTarget(true)
            .build();

        jobPostingRepository.save(jobPosting);
    }

    // ✅ 2. 목표기업 목록 조회
    public List<JobPosting> getJobPostings(Long userId) {
        return jobPostingRepository.findByUser_Id(userId);
    }

    // ✅ 3. 목표기업 삭제
    public void deleteJobPosting(Long postingId, Long userId) {
        JobPosting posting = jobPostingRepository.findByIdAndUser_Id(postingId, userId)
            .orElseThrow(() -> new IllegalArgumentException("해당 공고를 찾을 수 없습니다."));
        jobPostingRepository.delete(posting);
    }
}
