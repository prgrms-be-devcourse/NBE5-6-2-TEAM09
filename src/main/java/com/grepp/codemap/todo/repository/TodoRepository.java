package com.grepp.codemap.todo.repository;

import com.grepp.codemap.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Todo 엔티티에 대한 CRUD 및 커스텀 조회 메서드를 제공하는 리포지토리
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * 특정 사용자가 지정한 기간(start~end) 동안 시작 예정인 Todo 리스트 조회
     *
     * @param userId 조회 대상 사용자 ID
     * @param start  조회 기간 시작 시각 (inclusive)
     * @param end    조회 기간 종료 시각 (inclusive)
     * @return 기간 내 해당 사용자의 Todo 리스트
     */
    List<Todo> findAllByUser_IdAndStartTimeBetween(
        Long userId,
        LocalDateTime start,
        LocalDateTime end
    );

    /**
     * 단건 조회 시, 사용자 권한 검증을 위해 ID와 user_id를 함께 조회
     *
     * @param id     조회할 Todo ID
     * @param userId 조회 대상 사용자 ID
     * @return Todo (존재하지 않으면 null 반환)
     */

    Todo findByIdAndUser_Id(Long id, Long userId);
}