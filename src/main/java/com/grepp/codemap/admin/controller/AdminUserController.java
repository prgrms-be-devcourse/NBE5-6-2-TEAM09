package com.grepp.codemap.admin.controller;

import com.grepp.codemap.admin.dto.AdminUserUpdateDto;
import com.grepp.codemap.admin.service.AdminUserService;
import com.grepp.codemap.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminService;

    @GetMapping() // 회원 목록 조회 -> 기본 진입
    public String showUserList(Model model) {
        List<User> users = adminService.findAllUsers(); // 모든 회원 조회
        model.addAttribute("users", users);
        return "admin/manage-users";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = adminService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + id));
        model.addAttribute("user", user);
        return "admin/edit-users";
    }

    @PatchMapping("/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute AdminUserUpdateDto dto,
                             RedirectAttributes redirectAttributes) {
        try {
            adminService.updateUser(id, dto);
            redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "회원이 삭제되었습니다.");
        return "redirect:/admin/users";
    }
}
