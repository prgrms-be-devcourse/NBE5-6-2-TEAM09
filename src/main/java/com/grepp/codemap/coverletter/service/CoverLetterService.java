package com.grepp.codemap.coverletter.service;

import com.grepp.codemap.coverletter.domain.CoverLetter;
import com.grepp.codemap.coverletter.dto.CoverLetterRequestDto;
import com.grepp.codemap.coverletter.dto.CoverLetterResponseDto;
import com.grepp.codemap.coverletter.repository.CoverLetterRepository;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoverLetterService {

    private final CoverLetterRepository coverLetterRepository;
    private final UserService userService;

    @Transactional
    public Long create(CoverLetterRequestDto dto, Long userId) {
        User user = userService.getUserById(userId);

        CoverLetter entity = CoverLetter.builder()
            .user(user)
            .title(dto.getTitle())
            .isCompleted(false)
            .q1(dto.getQ1())
            .q2(dto.getQ2())
            .q3(dto.getQ3())
            .q4(dto.getQ4())
            .q5(dto.getQ5())
            .q6(dto.getQ6())
            .q7(dto.getQ7())
            .q8(dto.getQ8())
            .build();

        return coverLetterRepository.save(entity).getId();
    }

    public List<CoverLetterResponseDto> findAllByUser(Long userId) {
        User user = userService.getUserById(userId);
        return coverLetterRepository.findByUser(user).stream()
            .map(CoverLetterResponseDto::new)
            .collect(Collectors.toList());
    }


    public CoverLetterResponseDto findById(Long id, Long userId) {
        CoverLetter entity = coverLetterRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("자기소개서를 찾을 수 없습니다."));
        if (!entity.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        return new CoverLetterResponseDto(entity);
    }

    @Transactional
    public void update(Long id, CoverLetterRequestDto dto, Long userId) {
        CoverLetter entity = coverLetterRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("자기소개서를 찾을 수 없습니다."));
        if (!entity.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        CoverLetter updated = CoverLetter.builder()
            .id(id)
            .user(entity.getUser())
            .title(dto.getTitle())
            .isCompleted(entity.isCompleted())
            .q1(dto.getQ1())
            .q2(dto.getQ2())
            .q3(dto.getQ3())
            .q4(dto.getQ4())
            .q5(dto.getQ5())
            .q6(dto.getQ6())
            .q7(dto.getQ7())
            .q8(dto.getQ8())
            .build();

        coverLetterRepository.save(updated);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        CoverLetter entity = coverLetterRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("자기소개서를 찾을 수 없습니다."));
        if (!entity.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        coverLetterRepository.delete(entity);
    }

    @Transactional
    public void toggleComplete(Long id, Long userId) {
        CoverLetter entity = coverLetterRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("자기소개서를 찾을 수 없습니다."));
        if (!entity.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        entity.setCompleted(!entity.isCompleted());
    }
}
