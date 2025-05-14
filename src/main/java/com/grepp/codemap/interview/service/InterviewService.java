package com.grepp.codemap.interview.service;

import com.grepp.codemap.interview.domain.InterviewQuestion;
import com.grepp.codemap.interview.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;

//    public InterviewService(InterviewRepository interviewRepository) {
//        this.interviewRepository = interviewRepository;
//    }

    public List<String> getAllCategories() { // 전체 카테고리 목록 갖고오기
        return interviewRepository.findDistinctCategory();
    }

    public List<InterviewQuestion> pickThreeByCategory(String category) {
        List<InterviewQuestion> list = interviewRepository.findByCategory(category);
        Collections.shuffle(list);
        return list.stream().limit(3).collect(Collectors.toList());
    }

    public InterviewQuestion findById(Long questionId) {
        return interviewRepository.findById(questionId).orElse(null);
    }
}
