package com.grepp.codemap.coverletter.domain;

import com.grepp.codemap.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cover_letters")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoverLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 사용자 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private boolean isCompleted;

    @Lob private String q1;
    @Lob private String q2;
    @Lob private String q3;
    @Lob private String q4;
    @Lob private String q5;
    @Lob private String q6;
    @Lob private String q7;
    @Lob private String q8;

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }
}
