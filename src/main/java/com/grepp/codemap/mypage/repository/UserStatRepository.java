package com.grepp.codemap.mypage.repository;

import com.grepp.codemap.mypage.domain.UserStat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatRepository extends JpaRepository<UserStat, Long> {

    // 특정 사용자의 통계 조회
    Optional<UserStat> findByUserId(Long userId);
}
