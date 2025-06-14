package com.grepp.codemap.jobposting.repository;

import com.grepp.codemap.jobposting.domain.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    // ✅ 사용자의 모든 목표기업 조회
    List<JobPosting> findByUser_Id(Long userId);

    // ✅ 특정 사용자의 목표기업 단건 조회
    Optional<JobPosting> findByIdAndUser_Id(Long id, Long userId);
}
