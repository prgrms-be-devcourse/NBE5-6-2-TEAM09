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
public class InterviewController {

    private final InterviewService interviewService;
    private final UserService userService;
    private final UserAnswerService userAnswerService;
    private final KeywordCompareService keywordCompareService;

    @GetMapping("/select")
    public String showCategory(Model model,
                               @SessionAttribute(name = "userId", required = false) Long userId) {
        List<String> categories = interviewService.getAllCategories();
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        return "interview/interview-select";
    }

    @PostMapping("/select") // 카테고리 선택 후 질문 5개 랜덤 추출
    public String showRandomQuestion(
            @RequestParam("categoryList") List<String> categoryList,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        List<InterviewQuestion> questions = interviewService.pickFiveRandomByCategories(categoryList);

        if (questions == null || questions.isEmpty()) {
            model.addAttribute("error", "선택한 카테고리에 해당하는 질문이 없습니다.");
            model.addAttribute("categories", interviewService.getAllCategories());
            return "interview/interview-select";
        }

        session.setAttribute("questions", questions);
        session.setAttribute("selectedCategories", categoryList);

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
            questions = interviewService.pickFiveRandomByCategories(categories);
            session.setAttribute("questions", questions);
            session.setAttribute("selectedCategories", categories);
        }

        if (page < 0 || page >= questions.size()) {
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
        String dbKeywords = question.getKeywords(); // DB에서 keywords 컬럼 가져오기

        // DB 키워드 우선 사용하여 상세 분석 수행
        Map<String, Object> analysis = keywordCompareService.generateDetailedAnalysisWithDBKeywords(
                userAnswerText, dbKeywords, modelAnswer);

        // 실제 사용된 키워드 리스트 가져오기 (분석에 사용된 키워드)
        List<String> coreKeywords;
        if (dbKeywords != null && !dbKeywords.trim().isEmpty()) {
            coreKeywords = keywordCompareService.parseKeywordsFromDB(dbKeywords);
        } else {
            coreKeywords = keywordCompareService.extractCoreKeywords(modelAnswer);
        }

        // 하이라이트된 모범 답안 생성
        List<String> matchedKeywords = (List<String>) analysis.get("matchedKeywords");
        List<String> missingKeywords = (List<String>) analysis.get("missingKeywords");
        String highlightedModelAnswer = keywordCompareService.generateHighlightedAnswer(
                modelAnswer, matchedKeywords, missingKeywords);

        // 디버깅 로그 추가
        log.info("🔍 [KEYWORD ANALYSIS] 질문 ID: {}", questionId);
        log.info("📝 [MODEL ANSWER] {}", modelAnswer);
        log.info("🎯 [DB KEYWORDS] {}", dbKeywords);
        log.info("🔑 [FINAL CORE KEYWORDS] {}", coreKeywords);
        log.info("✅ [MATCHED] {}", matchedKeywords);
        log.info("❌ [MISSING] {}", missingKeywords);
        log.info("📊 [ACCURACY] {}%", analysis.get("accuracy"));
        log.info("🎓 [GRADE] {}", analysis.get("grade"));

        // 모델에 데이터 추가
        model.addAttribute("question", question);
        model.addAttribute("userAnswer", userAnswer);
        model.addAttribute("category", question.getCategory());
        model.addAttribute("page", page);

        // 분석 결과 추가
        model.addAttribute("analysis", analysis);
        model.addAttribute("highlightedModelAnswer", highlightedModelAnswer);
        model.addAttribute("coreKeywords", coreKeywords); // 핵심 키워드 목록
        model.addAttribute("keywordSource", dbKeywords != null && !dbKeywords.trim().isEmpty() ? "DB" : "EXTRACTED"); // 키워드 출처 표시

        int totalPages = ((List<InterviewQuestion>) session.getAttribute("questions")).size();
        model.addAttribute("totalPages", totalPages);

        return "interview/interview-result";
    }

    @GetMapping("/next")
    public String goToNextQuestion(HttpSession session, @RequestParam("page") int currentPage, Model model) {
        List<InterviewQuestion> questions = (List<InterviewQuestion>) session.getAttribute("questions");
        List<String> categories = (List<String>) session.getAttribute("selectedCategories");

        if (questions == null || questions.isEmpty() || currentPage + 1 >= questions.size()) {
            return "redirect:/interview/select"; // 모든 질문 완료 시
        }

        int nextPage = currentPage + 1;
        InterviewQuestion nextQuestion = questions.get(nextPage);

        model.addAttribute("question", nextQuestion);
        model.addAttribute("page", nextPage);
        model.addAttribute("totalPages", questions.size());
        model.addAttribute("category", nextQuestion.getCategory());

        log.info("➡️ [NEXT QUESTION] 현재 페이지: {} → 다음 페이지: {}", currentPage, nextPage);
        log.info("📋 [TOTAL QUESTIONS] {}", questions.size());
        log.info("❓ [NEXT QUESTION] {}", nextQuestion.getQuestionText());

        return "interview/interview-random";
    }

    @PostMapping("/complete")
    public String completeInterview(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "🎉 모든 질문을 완료했습니다! 수고하셨습니다.");
        return "redirect:/interview/select";
    }
}