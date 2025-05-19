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
    public String showCategory(Model model,
        @SessionAttribute(name = "userId", required = false) Long userId){
        List<String> categories = interviewService.getAllCategories();
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        return "interview/interview-select";
    }

    @PostMapping("/select") // 카테 선택 후 질문 3개 랜덤 추출
    public String showRandomQuestion(
            @RequestParam("category") String category,
            Model model,
            HttpSession session) {

        List<InterviewQuestion> questions = interviewService.pickFiveByCategory(category);

        if (questions == null || questions.isEmpty()) {
            log.warn("questions가 null 또는 비어 있음");
            return "redirect:/interview/select"; // 또는 에러 페이지
        }

        session.setAttribute("questions", questions);
        session.setAttribute("category", category);


        model.addAttribute("questions", questions);
        model.addAttribute("category", category);
        model.addAttribute("page", 0); // ✅ 추가
        model.addAttribute("totalPages", questions.size()); // ✅ 추가

        model.addAttribute("question", questions.get(0)); // ✅ 질문 1개 보여주기
        return "interview/interview-random";
    }

    @GetMapping("/question")
    public String showQuestionPage(
            @RequestParam("category") String category,
            @RequestParam("page") int page,
            HttpSession session,
            Model model) {

        List<InterviewQuestion> questions = (List<InterviewQuestion>) session.getAttribute("questions");
        log.info("선택된 category = {}", category);
        log.info("뽑힌 질문 개수 = {}", questions.size());

        if (questions == null || questions.isEmpty()) {
            questions = interviewService.pickFiveByCategory(category);
            session.setAttribute("questions", questions);
        }

        if (page < 0 || page >= questions.size()) {
            // 범위 초과 방어
            model.addAttribute("error", "올바르지 않은 페이지 번호입니다.");
            return "redirect:/interview/select";
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
            @RequestParam("page") int page,
            Authentication authentication,
            HttpSession session,
            Model model) {

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        InterviewQuestion question = interviewService.findById(questionId);

        UserAnswer userAnswer = userAnswerService.saveUserAnswer(user, question, answer);

        List<InterviewQuestion> questions = (List<InterviewQuestion>) session.getAttribute("questions");

        model.addAttribute("question", question);
        model.addAttribute("userAnswer", userAnswer);
        model.addAttribute("category", question.getCategory());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", questions.size());

        return "interview/interview-answer";
    }



    @GetMapping("/{questionId}/result")
    public String showResult(
            @PathVariable("questionId") Long questionId,
            @RequestParam("page") int page,
            Authentication authentication,
            HttpSession session,
            Model model){

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        InterviewQuestion question = interviewService.findById(questionId);
        UserAnswer userAnswer = userAnswerService.findLatestAnswer(user, question);

        log.info("🔍 [showResult] userAnswer 객체 = {}", userAnswer);
        log.info("🔍 [showResult] userAnswer.answerText = {}", userAnswer.getAnswerText());


        model.addAttribute("question", question);
        model.addAttribute("userAnswer", userAnswer);
        model.addAttribute("category", question.getCategory());
        model.addAttribute("page", page);

        int totalPages = ((List<InterviewQuestion>) session.getAttribute("questions")).size();
        model.addAttribute("totalPages", totalPages);

        return "interview/interview-result";
    }

    @GetMapping("/next")
    public String goToNextQuestion(HttpSession session, @RequestParam("page") int currentPage, Model model) {
        List<InterviewQuestion> questions = (List<InterviewQuestion>) session.getAttribute("questions");
        String category = (String) session.getAttribute("category");

        if (questions == null || questions.isEmpty() || currentPage + 1 >= questions.size()) {
            return "redirect:/interview/select"; // 모든 질문 완료 시 선택 페이지로 이동
        }

        int nextPage = currentPage + 1;
        InterviewQuestion nextQuestion = questions.get(nextPage);

        model.addAttribute("question", nextQuestion);
        model.addAttribute("page", nextPage);
        model.addAttribute("totalPages", questions.size());
        model.addAttribute("category", category);

        return "interview/interview-random";
    }





}
