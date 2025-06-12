package com.grepp.codemap.interview.controller;


import com.grepp.codemap.interview.domain.InterviewQuestion;
import com.grepp.codemap.interview.domain.UserAnswer;
import com.grepp.codemap.interview.service.KeywordCompareService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/interview")
public class  InterviewController {

    private final InterviewService interviewService;
    private final UserService userService;
    private final UserAnswerService userAnswerService;
    private final KeywordCompareService keywordCompareService;


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
            @RequestParam("categoryList") List<String> categoryList,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {


        List<InterviewQuestion> questions = interviewService.pickFiveRandomByCategories(categoryList);

        if (questions == null || questions.isEmpty()) {
            model.addAttribute("error", "선택한 카테고리에 해당하는 질문이 없습니다.");
            model.addAttribute("categories", interviewService.getAllCategories());
            return "interview/interview-select"; // 리다이렉트 ❌, 그대로 다시 보여줌
        }

        session.setAttribute("questions", questions);
        session.setAttribute("selectedCategories", categoryList); // ✅ 추후 보여주기용

        model.addAttribute("question", questions.get(0));
        model.addAttribute("page", 0);
        model.addAttribute("totalPages", questions.size());

        return "interview/interview-random";
    }

    @GetMapping("/question")
    public String showQuestionPage(
            @RequestParam("category") List<String> categories,
            @RequestParam("page") int page,
            HttpSession session,
            Model model) {

        List<InterviewQuestion> questions = (List<InterviewQuestion>) session.getAttribute("questions");

        if (questions == null || questions.isEmpty()) {
            questions = interviewService.pickFiveRandomByCategories(categories); // ✅ 서비스 메서드 사용
            session.setAttribute("questions", questions);
            session.setAttribute("selectedCategories", categories);
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
        model.addAttribute("category", currentQuestion.getCategory());

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
            Model model) {

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        InterviewQuestion question = interviewService.findById(questionId);
        UserAnswer userAnswer = userAnswerService.findLatestAnswer(user, question);

        String modelAnswer = question.getAnswerText();
        String userAnswerText = userAnswer.getAnswerText();

        // 키워드 추출 및 상세 분석
        List<String> keywordList = keywordCompareService.extractKeywords(modelAnswer);
        Map<String, Object> analysis = keywordCompareService.generateDetailedAnalysis(userAnswerText, keywordList);

        // 하이라이트된 모범 답안 생성
        List<String> matchedKeywords = (List<String>) analysis.get("matchedKeywords");
        List<String> missingKeywords = (List<String>) analysis.get("missingKeywords");
        String highlightedModelAnswer = keywordCompareService.generateHighlightedAnswer(
                modelAnswer, matchedKeywords, missingKeywords);

        // 모델에 데이터 추가
        model.addAttribute("question", question);
        model.addAttribute("userAnswer", userAnswer);
        model.addAttribute("category", question.getCategory());
        model.addAttribute("page", page);

        // 분석 결과 추가
        model.addAttribute("analysis", analysis);
        model.addAttribute("highlightedModelAnswer", highlightedModelAnswer);
        model.addAttribute("keywordList", keywordList); // 전체 키워드 목록

        int totalPages = ((List<InterviewQuestion>) session.getAttribute("questions")).size();
        model.addAttribute("totalPages", totalPages);

        return "interview/interview-result";
    }

    @GetMapping("/next")
    public String goToNextQuestion(HttpSession session, @RequestParam("page") int currentPage, Model model) {
        List<InterviewQuestion> questions = (List<InterviewQuestion>) session.getAttribute("questions");
        List<String> categories = (List<String>) session.getAttribute("selectedCategories");

        if (questions == null || questions.isEmpty() || currentPage + 1 >= questions.size()) {
            return "redirect:/interview/select"; // ✅ 모든 질문 완료 시
        }

        int nextPage = currentPage + 1;
        InterviewQuestion nextQuestion = questions.get(nextPage);

        model.addAttribute("question", nextQuestion);
        model.addAttribute("page", nextPage);
        model.addAttribute("totalPages", questions.size());
        model.addAttribute("category", nextQuestion.getCategory()); // ✅ 질문마다 다름

        log.info("✅ [NEXT] 현재 페이지: {}", currentPage);
        log.info("✅ [NEXT] 총 질문 수: {}", questions.size());
        log.info("✅ [NEXT] 다음 질문: {}", nextQuestion.getQuestionText());

        return "interview/interview-random";
    }

    @PostMapping("/complete")
    public String completeInterview(RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("message", "모든 질문을 완료했습니다!");
        return "redirect:/interview/select";
    }








}
