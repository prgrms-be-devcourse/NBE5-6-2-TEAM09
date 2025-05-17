package com.grepp.codemap.admin.service;


import com.grepp.codemap.admin.dto.AdminUserUpdateDto;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void updateUserByAdmin(Long id, AdminUserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + id));

        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());

        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        userRepository.save(user); // 변경 감지 방식이라면 생략 가능
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
