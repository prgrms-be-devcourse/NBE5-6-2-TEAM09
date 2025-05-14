package com.grepp.codemap.user.dto;

import com.grepp.codemap.infra.auth.Role;
import com.grepp.codemap.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto { // 회원가입, 로그인용

    private String email;
    private String nickname;
    private String password;
    private Role role;
    private LocalDateTime createdAt = LocalDateTime.now();

    public UserDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
//        this.password = password;
    }
}

