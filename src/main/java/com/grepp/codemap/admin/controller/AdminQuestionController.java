package com.grepp.codemap.admin.controller;

import com.grepp.codemap.admin.dto.InterviewQuestionFormDto;
import com.grepp.codemap.interview.domain.InterviewQuestion;
import com.grepp.codemap.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/contents")
@RequiredArgsConstructor
@Slf4j
public class AdminQuestionController {

    private final InterviewService interviewService;

    @GetMapping
    public String redirectToManage() {
        return "redirect:/admin/contents/category-select";
    }

    // 1️⃣ 카테고리 선택 페이지 (GET)
    @GetMapping("/category-select")
    public String showCategorySelectPage(Model model) {
        List<String> categories = interviewService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/content/category-select";
    }

    // 2️⃣ 카테고리 선택 후 질문 목록으로 이동 (POST)
    @PostMapping("/category-select")
    public String handleCategorySelection(@RequestParam("category") String category,
                                          RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("category", category);
        return "redirect:/admin/contents/question-list";
    }

    // 3️⃣ 질문 목록 조회 (선택된 카테고리 기준)
    @GetMapping("/question-list")
    public String showQuestionList(@RequestParam("category") String category, Model model) {
        String cleaned = category.trim().replace(",", "");
        log.info("💡 클린 카테고리: {}", cleaned); // 🔍 확인용 로그

        List<InterviewQuestion> questions = interviewService.findAll().stream()
                .filter(q -> q.getCategory().equals(category))
                .toList();

        model.addAttribute("category", category);
        model.addAttribute("questions", questions);

        return "admin/content/question-list";
    }

    // 4️⃣ 질문 등록 폼
    @GetMapping("/questions/new")
    public String showCreateForm(@RequestParam("category") String category, Model model) {
        InterviewQuestionFormDto form = new InterviewQuestionFormDto();
        form.setCategory(category);
        model.addAttribute("form", form);
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/admin/contents/questions");
        return "admin/content/question-form";
    }

    // 5️⃣ 질문 등록 처리
    @PostMapping("/questions")
    public String registerQuestion(@ModelAttribute("form") InterviewQuestionFormDto formDto,
                                   RedirectAttributes redirectAttributes) {

        log.info("💬 등록 요청 들어옴: {}", formDto);

        try {
            interviewService.save(formDto.toEntity());
        } catch (Exception e) {
            log.error("❌ 등록 중 오류 발생", e);
            throw e;
        }

        redirectAttributes.addAttribute("category", formDto.getCategory());
        redirectAttributes.addFlashAttribute("message", "질문이 등록되었습니다.");
        return "redirect:/admin/contents/question-list";
    }

    // 6️⃣ 질문 수정 폼
    @GetMapping("/questions/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        InterviewQuestion question = interviewService.findById(id);
        InterviewQuestionFormDto formDto = InterviewQuestionFormDto.from(question);
        model.addAttribute("form", formDto);
        model.addAttribute("isEdit", true);
        model.addAttribute("formAction", "/admin/contents/questions/" + id + "/edit");
        return "admin/content/question-form";
    }

    // 7️⃣ 질문 수정 처리
    @PatchMapping("/questions/{id}/edit")
    public String updateQuestion(@PathVariable Long id,
                                 @ModelAttribute("form") InterviewQuestionFormDto formDto,
                                 RedirectAttributes redirectAttributes) {
        interviewService.update(id, formDto.toEntity());
        redirectAttributes.addAttribute("category", formDto.getCategory());
        redirectAttributes.addFlashAttribute("message", "질문이 수정되었습니다.");
        return "redirect:/admin/contents/question-list";
    }

    // 8️⃣ 질문 삭제 처리
    @DeleteMapping("/questions/{id}/delete")
    public String deleteQuestion(@PathVariable Long id,
                                 @RequestParam("category") String category,
                                 RedirectAttributes redirectAttributes) {
        String cleanedCategory = category.trim().replace(",", "");
        log.info("✅ 삭제 요청 들어옴: id={}, cleanedCategory={}", id, cleanedCategory);

        interviewService.deleteById(id);
        redirectAttributes.addAttribute("category", cleanedCategory);
        redirectAttributes.addFlashAttribute("message", "질문이 삭제되었습니다.");
        return "redirect:/admin/contents/question-list";
    }

    // 🔁 관리 메뉴 진입 시 카테고리 선택 페이지로 리다이렉트
    @GetMapping("/manage-questions")
    public String redirectToCategorySelect() {
        return "redirect:/admin/contents/category-select";
    }

}
