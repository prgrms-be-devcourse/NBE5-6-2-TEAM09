package com.grepp.codemap.interview.controller;


import com.grepp.codemap.interview.domain.InterviewQuestion;
import com.grepp.codemap.interview.domain.UserAnswer;
import com.grepp.codemap.interview.service.InterviewService;
import com.grepp.codemap.interview.service.UserAnswerService;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/interview")
public class  InterviewController {

    private final InterviewService interviewService;
    private final UserService userService;
    private final UserAnswerService userAnswerService;


    @GetMapping("/select")
    public String showCategory(Model model){
        List<String> categories = interviewService.getAllCategories();
        model.addAttribute("categories", categories);
        return "interview/interview-select";
    }

    @PostMapping("/select") // 카테 선택 후 질문 3개 랜덤 추출
    public String showRandomQuestion(@RequestParam("category") String category, Model model) {
        List<InterviewQuestion> questions = interviewService.pickThreeByCategory(category);
        model.addAttribute("questions", questions);
        model.addAttribute("category", category);
        model.addAttribute("currentPage", 0); // 0번째 질문부터
        return "interview/interview-random";
    }

    @GetMapping("/question")
    public String showQuestionPage(
            @RequestParam("category") String category,
            @RequestParam("page") int page,
            HttpSession session,
            Model model) {

        List<InterviewQuestion> questions = (List<InterviewQuestion>) session.getAttribute("questions");

        if (questions == null || questions.isEmpty()) {
            // 🔽 테스트용 데이터는 주석 처리
        /*
                                                                                                     questions = List.of(
                InterviewQuestion.builder()
                        .questionText("네트워크에서 TCP와 UDP의 차이점은?")
                        .category("네트워크")
                        .difficulty("중")
                        .answerText("TCP는 연결지향, UDP는 비연결지향이다.")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),
                InterviewQuestion.builder()
                        .questionText("OSI 7계층을 설명하세요.")
                        .category("네트워크")
                        .difficulty("상")
                        .answerText("7계층 각각의 역할과 예시를 들어 설명합니다.")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),
                InterviewQuestion.builder()
                        .questionText("HTTP와 HTTPS의 차이점은?")
                        .category("네트워크")
                        .difficulty("하")
                        .answerText("HTTPS는 SSL/TLS 암호화를 적용한 HTTP입니다.")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
        */

            // 🔽 실제 질문을 서비스에서 불러옴
            questions = interviewService.pickThreeByCategory(category);
            session.setAttribute("questions", questions);
        }

        InterviewQuestion currentQuestion = questions.get(page);
        model.addAttribute("question", currentQuestion);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", questions.size());
        model.addAttribute("category", category);

        return "interview/interview-random";
    }


    @PostMapping("/{questionId}/answer")
    public String submitAnswer(
            @PathVariable("questionId") Long questionId,
            @RequestParam("answer") String answer,
            Authentication authentication,
            Model model){



        String email = authentication.getName();
        User user = userService.findByEmail(email);
        InterviewQuestion question = interviewService.findById(questionId);

        userAnswerService.saveUserAnswer(user, question, answer);

        model.addAttribute("question", question); // 질문 다시 보여주고 "답안 보기" 버튼 포함해서 렌더링
        model.addAttribute("answerSubmitted", true);
        return "interview/interview-random";
    }


    @GetMapping("/{questionId}/result")
    public String showResult(
            @RequestParam("questionId") Long questionId,
            Authentication authentication,
            Model model){

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        InterviewQuestion question = interviewService.findById(questionId);
        UserAnswer userAnswer = userAnswerService.findByUserAndQuestion(user, question);

        model.addAttribute("question", question);
        model.addAttribute("userAnswer", userAnswer);
        return "interview/interview-result";
    }





}
