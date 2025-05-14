package com.grepp.codemap.interview.controller;


import com.grepp.codemap.interview.domain.InterviewQuestion;
import com.grepp.codemap.interview.domain.UserAnswer;
import com.grepp.codemap.interview.service.InterviewService;
import com.grepp.codemap.interview.service.UserAnswerService;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/interview")
public class InterviewController {

    private final InterviewService interviewService;
    private final UserService userService;
    private final UserAnswerService userAnswerService;


    @GetMapping("/select")
    public String showCategory(Model model){
        List<String> categories = interviewService.getAllCategories();
        model.addAttribute("categories", categories);
        return "interview-select";
    }

    @PostMapping("/select") // 카테 선택 후 질문 3개 랜덤 추출
    public String showRandomQuestion(@RequestParam("category") String category, Model model) {
        List<InterviewQuestion> questions = interviewService.pickThreeByCategory(category);
        model.addAttribute("questions", questions);
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
        return "interview/interview-result";
    }





}
