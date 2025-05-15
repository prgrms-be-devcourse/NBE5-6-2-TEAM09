package com.grepp.codemap.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserUpdateDto {

    private String nickname;
    private String email;

    private String currentPassword; // 현재 비밀번호 (검증용)
    private String newPassword;     // 새로 저장할 비밀번호
}