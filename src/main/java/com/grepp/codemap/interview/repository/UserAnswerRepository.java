package com.grepp.codemap.interview.repository;


import com.grepp.codemap.interview.domain.InterviewQuestion;
import com.grepp.codemap.interview.domain.UserAnswer;
import com.grepp.codemap.interview.dto.QuestionSummaryDto;
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

    @Query("""
                SELECT new com.grepp.codemap.interview.dto.QuestionSummaryDto(
                    ua.question.id,
                    ua.question.questionText,
                    ua.question.category,
                    ua.question.difficulty
                )
                FROM UserAnswer ua
                WHERE ua.user.id = :userId
                  AND ua.answeredAt = (
                      SELECT MAX(subUa.answeredAt)
                      FROM UserAnswer subUa
                      WHERE subUa.user.id = :userId
                        AND subUa.question.id = ua.question.id
                  )
            """)
    List<QuestionSummaryDto> findLatestAnswersByUserId(@Param("userId") Long userId);



}
