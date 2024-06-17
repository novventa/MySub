package nova.mysub.global.auth;

import nova.mysub.domain.user.model.entity.User;
import nova.mysub.domain.user.repository.UserRepository;

import nova.mysub.domain.user.service.CustomUserDetailsService;
import nova.mysub.global.auth.dto.KakaoAccount;
import nova.mysub.global.auth.dto.KakaoUserProfile;
import nova.mysub.global.auth.dto.KakaoUserResponse;
import nova.mysub.global.auth.dto.SignUpSuccessResponseDto;
import nova.mysub.global.auth.jwt.JwtTokenProvider;
import nova.mysub.global.auth.oauth2.KakaoApiClient;
import nova.mysub.global.auth.oauth2.KakaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KakaoSocialServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KakaoApiClient kakaoApiClient;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private KakaoService kakaoService;

    private KakaoUserResponse kakaoUserResponse;
    private User user;

    @BeforeEach
    public void setUp() {
        KakaoUserProfile profile = new KakaoUserProfile("nickname", "profileImageUrl", "accountEmail");
        KakaoAccount kakaoAccount = new KakaoAccount(profile);
        kakaoUserResponse = new KakaoUserResponse(1L, kakaoAccount);

        user = new User();
        user.setId(1L);
        user.setUsername("nickname");
        user.setEmail("accountEmail");
        user.setProfileImageUrl("profileImageUrl");
        user.setRole("USER");
    }

    @Test
    public void testSignUpNewUser() {
        when(kakaoApiClient.getUserInformation(any())).thenReturn(kakaoUserResponse);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(customUserDetailsService.loadUserById(anyLong())).thenReturn(user);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("token");

        SignUpSuccessResponseDto response = kakaoService.signUp("accessToken");

        assertNotNull(response);
        assertEquals("token", response.getToken());
        verify(kakaoApiClient).getUserInformation(any());
        verify(userRepository).findById(anyLong());
        verify(userRepository).save(any(User.class));
        verify(jwtTokenProvider).generateToken(any(Authentication.class));
    }

    @Test
    public void testSignUpExistingUser() {
        when(kakaoApiClient.getUserInformation(any())).thenReturn(kakaoUserResponse);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(customUserDetailsService.loadUserById(anyLong())).thenReturn(user);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("token");

        SignUpSuccessResponseDto response = kakaoService.signUp("accessToken");

        assertNotNull(response);
        assertEquals("token", response.getToken());
        verify(kakaoApiClient).getUserInformation(any());
        verify(userRepository).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
        verify(jwtTokenProvider).generateToken(any(Authentication.class));
    }
}