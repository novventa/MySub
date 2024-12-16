package nova.mysub.global.auth.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nova.mysub.global.auth.jwt.JwtTokenProvider;
import nova.mysub.global.auth.jwt.TokenDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Long userId = Long.valueOf(Objects.requireNonNull(oAuth2User.getAttribute("id")));

        TokenDto tokenDto = jwtTokenProvider.createTokens(userId);

        String redirectUrl = String.format("http://localhost:8080/api/sign/login/kakao?accessToken=%s&refreshToken=%s",
                tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
