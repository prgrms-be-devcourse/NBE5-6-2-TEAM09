package com.grepp.codemap.interview.service;

import com.grepp.codemap.interview.domain.InterviewQuestion;
import com.grepp.codemap.interview.domain.UserAnswer;
import com.grepp.codemap.interview.repository.UserAnswerRepository;
import com.grepp.codemap.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;

    // 사용자 답변 저장
    public void saveUserAnswer(User user, InterviewQuestion question, String answerText) {
        UserAnswer userAnswer = UserAnswer.builder()
                .user(user)
                .question(question)
                .answerText(answerText)
                .answeredAt(LocalDateTime.now())
                .build();

        userAnswerRepository.save(userAnswer);
    }

    // 사용자 답변 조회 (결과 보기용)
    public UserAnswer findByUserAndQuestion(User user, InterviewQuestion question) {
        return userAnswerRepository.findByUserAndQuestion(user, question)
                .orElseThrow(() -> new RuntimeException("답변이 존재하지 않습니다."));
    }
}
