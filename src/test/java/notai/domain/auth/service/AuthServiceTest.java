package notai.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import notai.domain.auth.dto.request.PasswordCheckRequest;
import notai.domain.auth.dto.request.RegisterRequest;
import notai.domain.user.dto.response.UserDetailResponse;
import notai.domain.user.entity.User;
import notai.domain.user.repository.UserRepository;
import notai.global.dto.MessageResponse;
import notai.global.enums.Role;
import notai.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void beforeEach() {

        // 기본 유저 초기화
        user = User.builder().id(1L).email("a@test.com").address("a-address").role(Role.USER)
            .phoneNumber("010-1111-1111").password(passwordEncoder.encode("1234")).nickname("userA-nick").name("userA")
            .build();
    }

    @DisplayName("회원가입")
    @Test
    void testRegisterUser() {

        // given
        RegisterRequest request = RegisterRequest.builder().email("a@test.com").address("a-address")
            .phoneNumber("010-1111-1111").password("1234").nickname("userA-nick").name("userA")
            .build();

        given(userRepository.save(any())).willReturn(user);

        // when
        UserDetailResponse response = authService.registerUser(request);

        // then
        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getPassword()).isEqualTo(user.getPassword());
        assertThat(response.getNickname()).isEqualTo(user.getNickname());
        assertThat(response.getAddress()).isEqualTo(user.getAddress());
        assertThat(response.getName()).isEqualTo(user.getName());
        assertThat(response.getRole()).isEqualTo(user.getRole().name());
    }

    @DisplayName("비밀번호 중복")
    @Test
    void testCheckPassword_중복() {

        // given
        PasswordCheckRequest request = PasswordCheckRequest.builder().password("1234").build();

        given(userRepository.existsByPassword(
            passwordEncoder.encode(request.getPassword()))).willReturn(true);

        // when
        Throwable throwable = catchThrowable(() -> authService.checkPassword(request));

        // then
        assertThat(throwable).isInstanceOf(CustomException.class)
            .hasMessage("이미 사용 중인 비밀번호입니다");
    }

    @DisplayName("비밀번호 미중복")
    @Test
    void testCheckPassword_미중복() {

        // given
        PasswordCheckRequest noDuplicated = PasswordCheckRequest.builder().password("0000").build();

        given(userRepository.existsByPassword(
            passwordEncoder.encode(noDuplicated.getPassword()))).willReturn(false);

        // when
        MessageResponse response = authService.checkPassword(noDuplicated);

        // then
        assertThat(response.getMessage()).isEqualTo("사용 가능한 비밀번호입니다");
    }
}