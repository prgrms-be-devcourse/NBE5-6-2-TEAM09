package com.grepp.codemap.user.service;

import com.grepp.codemap.infra.auth.Role;
import com.grepp.codemap.infra.error.exceptions.CommonException;
import com.grepp.codemap.infra.event.EventPublisher;
import com.grepp.codemap.infra.response.ResponseCode;
import com.grepp.codemap.user.domain.User;
import com.grepp.codemap.user.dto.UserDto;
import com.grepp.codemap.user.form.SigninForm;
import com.grepp.codemap.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 단위 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper mapper;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User user;
    private SigninForm signinForm;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setNickname("테스터");

        user = User.builder()
            .id(1L)
            .email("test@example.com")
            .password("encodedPassword")
            .nickname("테스터")
            .role("ROLE_USER")
            .isActive(true)
            .build();

        signinForm = new SigninForm();
        signinForm.setEmail("test@example.com");
        signinForm.setPassword("password123");
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // given
        given(userRepository.existsByEmail(userDto.getEmail())).willReturn(false);
        given(mapper.map(userDto, User.class)).willReturn(user);
        given(passwordEncoder.encode(userDto.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(user);
        willDoNothing().given(eventPublisher).publishSignupCompleteEvent(any(String.class));

        // when
        assertThatNoException().isThrownBy(() ->
            userService.signup(userDto, Role.ROLE_USER)
        );

        // then
        then(userRepository).should().existsByEmail(userDto.getEmail());
        then(mapper).should().map(userDto, User.class);
        then(passwordEncoder).should().encode(userDto.getPassword());
        then(userRepository).should().save(any(User.class));
        then(eventPublisher).should().publishSignupCompleteEvent(user.getEmail());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void signup_Fail_DuplicateEmail() {
        // given
        given(userRepository.existsByEmail(userDto.getEmail())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.signup(userDto, Role.ROLE_USER))
            .isInstanceOf(CommonException.class)
            .hasFieldOrPropertyWithValue("code", ResponseCode.BAD_REQUEST);

        then(userRepository).should().existsByEmail(userDto.getEmail());
        then(userRepository).should(never()).save(any(User.class));
        then(eventPublisher).should(never()).publishSignupCompleteEvent(any(String.class));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        given(userRepository.findByEmail(signinForm.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signinForm.getPassword(), user.getPassword())).willReturn(true);

        // when
        User result = userService.login(signinForm);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(signinForm.getEmail());
        assertThat(result.getId()).isEqualTo(user.getId());

        then(userRepository).should().findByEmail(signinForm.getEmail());
        then(passwordEncoder).should().matches(signinForm.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void login_Fail_UserNotFound() {
        // given
        given(userRepository.findByEmail(signinForm.getEmail())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.login(signinForm))
            .isInstanceOf(CommonException.class)
            .hasFieldOrPropertyWithValue("code", ResponseCode.USER_NOT_FOUND);

        then(userRepository).should().findByEmail(signinForm.getEmail());
        then(passwordEncoder).should(never()).matches(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_Fail_WrongPassword() {
        // given
        given(userRepository.findByEmail(signinForm.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signinForm.getPassword(), user.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.login(signinForm))
            .isInstanceOf(CommonException.class)
            .hasFieldOrPropertyWithValue("code", ResponseCode.BAD_REQUEST);

        then(userRepository).should().findByEmail(signinForm.getEmail());
        then(passwordEncoder).should().matches(signinForm.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("사용자 ID로 조회 성공")
    void getUserById_Success() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        User result = userService.getUserById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");

        then(userRepository).should().findById(1L);
    }

    @Test
    @DisplayName("사용자 ID로 조회 실패 - 존재하지 않는 사용자")
    void getUserById_Fail_UserNotFound() {
        // given
        given(userRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserById(999L))
            .isInstanceOf(CommonException.class)
            .hasFieldOrPropertyWithValue("code", ResponseCode.BAD_REQUEST);

        then(userRepository).should().findById(999L);
    }

    @Test
    @DisplayName("닉네임 변경 성공")
    void updateNickname_Success() {
        // given
        String newNickname = "새로운닉네임";
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        assertThatNoException().isThrownBy(() ->
            userService.updateNickname(1L, newNickname)
        );

        // then
        then(userRepository).should().findById(1L);
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void updatePassword_Success() {
        // given
        String currentPassword = "oldPassword";
        String newPassword = "newPassword123";
        String encodedNewPassword = "encodedNewPassword";

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(currentPassword, user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(newPassword)).willReturn(encodedNewPassword);
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        userService.updatePassword(1L, currentPassword, newPassword);

        // then
        verify(userRepository).findById(1L);
        verify(passwordEncoder).matches(currentPassword, "encodedPassword"); // user.getPassword() = "encodedPassword"
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 현재 비밀번호 불일치")
    void updatePassword_Fail_WrongCurrentPassword() {
        // given
        String wrongCurrentPassword = "wrongPassword";
        String newPassword = "newPassword123";
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(wrongCurrentPassword, user.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() ->
            userService.updatePassword(1L, wrongCurrentPassword, newPassword)
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("현재 비밀번호가 일치하지 않습니다.");

        then(userRepository).should().findById(1L);
        then(passwordEncoder).should().matches(wrongCurrentPassword, user.getPassword());
        then(passwordEncoder).should(never()).encode(any(String.class));
        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    @DisplayName("알림 설정 변경 성공")
    void updateNotificationSetting_Success() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        assertThatNoException().isThrownBy(() ->
            userService.updateNotificationSetting(1L, false)
        );

        // then
        then(userRepository).should().findById(1L);
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복 확인 - 중복됨")
    void isDuplicatedEmail_True() {
        // given
        given(userRepository.existsByEmail("duplicate@example.com")).willReturn(true);

        // when
        boolean result = userService.isDuplicatedEmail("duplicate@example.com");

        // then
        assertThat(result).isTrue();
        then(userRepository).should().existsByEmail("duplicate@example.com");
    }

    @Test
    @DisplayName("이메일 중복 확인 - 중복되지 않음")
    void isDuplicatedEmail_False() {
        // given
        given(userRepository.existsByEmail("unique@example.com")).willReturn(false);

        // when
        boolean result = userService.isDuplicatedEmail("unique@example.com");

        // then
        assertThat(result).isFalse();
        then(userRepository).should().existsByEmail("unique@example.com");
    }
}