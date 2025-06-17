package com.grepp.codemap.interview.repository;


import com.grepp.codemap.interview.domain.InterviewQuestion;
import com.grepp.codemap.interview.domain.UserAnswer;
import com.grepp.codemap.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    Optional<UserAnswer> findTopByUserAndQuestionOrderByIdDesc(User user, InterviewQuestion question);


    void deleteByQuestion(InterviewQuestion question);

    // 사용자의 모든 답변 다 가져옴.
    @EntityGraph(attributePaths = "question")
    List<UserAnswer> findByUserIdOrderByIdDesc(Long userId);

    @Query("SELECT ua FROM UserAnswer ua " +
            "WHERE ua.id IN (" +
            "   SELECT MAX(uaSub.id) FROM UserAnswer uaSub " +
            "   WHERE uaSub.user = :user " +
            "   GROUP BY uaSub.question" +
            ")")
    List<UserAnswer> findLatestAnswersByUser(@Param("user") User user);


}
