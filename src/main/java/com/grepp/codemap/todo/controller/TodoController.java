package com.grepp.codemap.todo.controller;

import com.grepp.codemap.todo.dto.TodoCreateRequest;
import com.grepp.codemap.todo.dto.TodoUpdateRequest;
import com.grepp.codemap.todo.dto.TodoResponse;
import com.grepp.codemap.todo.domain.Todo;
import com.grepp.codemap.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    /** ✅ 1. 캘린더 진입 화면 */
    @GetMapping("/calender")
    public String showCalendarPage() {
        return "todo/calender";
    }

    /** ✅ 2. 선택 날짜 투두 조회 */
    @GetMapping
    public String getTodoList(@RequestParam("date") String date,
        @SessionAttribute(value = "userId", required = false) Long userId,
        Model model) {

        // 지금은 로그인 안 한 경우 그냥 빈 목록 넘기자
        if (userId == null) {
            model.addAttribute("todos", List.of());  // 빈 리스트
            model.addAttribute("selectedDate", date);
            return "todo/todo-list";
        }

        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        List<TodoResponse> todos = todoService.getTodosByDate(userId, startOfDay, endOfDay);
        model.addAttribute("todos", todos);
        model.addAttribute("selectedDate", date);
        return "todo/todo-list";
    }

    /** ✅ 3. 추가 폼 (모달에서 불러옴) */
    @GetMapping("/new")
    public String showCreateForm(@RequestParam("date") String date, Model model) {
        model.addAttribute("selectedDate", date);
        return "todo/todo-form";
    }

    /** ✅ 4. 투두 생성 */
    @PostMapping
    public String createTodo(@SessionAttribute("userId") Long userId,
        @ModelAttribute @Valid TodoCreateRequest request) {
        Todo created = todoService.createTodo(
            userId,
            request.title(),
            request.description(),
            request.startTime(),
            request.completedAt()
        );
        return "redirect:/todos?date=" + created.getCompletedAt().toLocalDate();
    }

    /** ✅ 5. 수정 폼 (모달에서 불러옴) */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id,
        @SessionAttribute("userId") Long userId,
        @RequestParam("date") String date,
        Model model) {
        Todo todo = todoService.findByIdAndUser(id, userId);
        model.addAttribute("todo", todo);
        model.addAttribute("selectedDate", date);
        return "todo/todo-form";
    }

    /** ✅ 6. 투두 수정 */
    @PatchMapping("/{id}")
    public String updateTodo(@PathVariable Long id,
        @SessionAttribute("userId") Long userId,
        @ModelAttribute @Valid TodoUpdateRequest request) {
        Todo updated = todoService.updateTodo(
            id,
            userId,
            request.title(),
            request.description(),
            request.completedAt()
        );
        return "redirect:/todos?date=" + updated.getCompletedAt().toLocalDate();
    }

    /** ✅ 7. 투두 삭제 */
    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable Long id,
        @SessionAttribute("userId") Long userId,
        @RequestParam("date") String date) {
        todoService.deleteTodo(id, userId);
        return "redirect:/todos?date=" + date;
    }

    /** ✅ 8. 완료 상태 토글 (체크박스 클릭) */
    @PatchMapping("/{id}/complete")
    public String toggleComplete(@PathVariable Long id,
        @SessionAttribute("userId") Long userId,
        @RequestParam("date") String date) {
        todoService.toggleComplete(id, userId);
        return "redirect:/todos?date=" + date;
    }

    /** ✅ 9. 완료 취소 */
    @PatchMapping("/{id}/cancel")
    public String cancelComplete(@PathVariable Long id,
        @SessionAttribute("userId") Long userId,
        @RequestParam("date") String date) {
        todoService.toggleComplete(id, userId); // 같은 toggle 메서드 사용
        return "redirect:/todos?date=" + date;
    }
}
