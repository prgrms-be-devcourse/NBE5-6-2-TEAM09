package com.grepp.codemap.user.service;

import com.grepp.codemap.infra.error.exceptions.CommonException;
import com.grepp.codemap.infra.response.ResponseCode;
import com.grepp.codemap.infra.auth.Role;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.dto.UserDto;
import com.grepp.codemap.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Transactional
    public void signup(UserDto dto, Role role) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        // dto → entity 변환
        User user = mapper.map(dto, User.class);

        // 비밀번호 인코딩
        user.encodePassword(passwordEncoder.encode(dto.getPassword()));
        user.assignRole(role.name());

        userRepository.save(user);
    }

    public boolean isDuplicatedEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));

        return mapper.map(user, UserDto.class);
    }
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new CommonException(ResponseCode.BAD_REQUEST));
    }
}