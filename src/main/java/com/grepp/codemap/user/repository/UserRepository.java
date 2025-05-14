package com.grepp.codemap.user.repository;

import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByEmail(String email); // 이메일 중복 확인
    Optional<User> findByEmail(String email); // 로그인용

}