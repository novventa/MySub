package nova.mysub.global.auth.oauth2;

import lombok.RequiredArgsConstructor;
import nova.mysub.domain.user.model.entity.User;
import nova.mysub.domain.user.repository.UserRepository;
import nova.mysub.domain.user.service.CustomUserDetailsService;
import nova.mysub.global.auth.dto.KakaoUserResponse;
import nova.mysub.global.auth.dto.SignUpSuccessResponseDto;
import nova.mysub.global.auth.jwt.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserRepository userRepository;
    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Transactional
    public SignUpSuccessResponseDto signUp(final String accessToken) {
        KakaoUserResponse userResponse = kakaoApiClient.getUserInformation("Bearer " + accessToken);

        User user = userRepository.findById(userResponse.id())
                .orElseGet(() -> createUser(userResponse));

        UserDetails userDetails = customUserDetailsService.loadUserById(user.getId());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = jwtTokenProvider.generateToken(authentication);
        return new SignUpSuccessResponseDto(token);
    }

    private User createUser(KakaoUserResponse userResponse) {
        User user = new User();
        user.setUsername(userResponse.kakaoAccount().profile().nickname());
        user.setEmail(userResponse.kakaoAccount().profile().accountEmail());
        user.setProfileImageUrl(userResponse.kakaoAccount().profile().profileImageUrl());
        user.setRole("USER");
        user.setKakaoId(userResponse.id());
        return userRepository.save(user);
    }
}
