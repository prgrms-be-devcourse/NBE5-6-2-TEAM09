package com.grepp.codemap.interview.repository;

import com.grepp.codemap.interview.domain.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterviewRepository extends JpaRepository<InterviewQuestion,Long> {

    List<InterviewQuestion> findByCategory(String category);

    @Query("SELECT DISTINCT iq.category FROM InterviewQuestion iq")
    List<String> findDistinctCategory();
}
