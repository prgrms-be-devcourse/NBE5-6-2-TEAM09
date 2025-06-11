package com.grepp.codemap.coverletter.repository;

import com.grepp.codemap.coverletter.domain.CoverLetter;
import com.grepp.codemap.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoverLetterRepository extends JpaRepository<CoverLetter, Long> {
    List<CoverLetter> findByUser(User user);
}
