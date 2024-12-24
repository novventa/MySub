package nova.mysub.global.auth.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getTokenFromHeader(request, "Authorization");

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            // Access Token이 유효하면 인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(accessToken));
        } else if (accessToken != null && !jwtTokenProvider.validateToken(accessToken)) {
            // Access Token이 만료된 경우
            String refreshToken = getTokenFromHeader(request, "Refresh-Token");
            if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                // Refresh Token이 유효한 경우 재발급
                TokenDto newTokens = jwtTokenProvider.reIssueTokens(refreshToken);

                // 새로운 Access Token으로 인증 객체 생성
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(newTokens.getAccessToken()));

                // 새 토큰을 응답 헤더에 추가
                response.setHeader("Authorization", "Bearer " + newTokens.getAccessToken());
                response.setHeader("Refresh-Token", newTokens.getRefreshToken());
            } else {
                throw new JwtException("Refresh Token is invalid or expired");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request, String headerName) {
        String bearerToken = request.getHeader(headerName);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        return null;
    }

    private boolean isRequestPassURI(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/auth") || requestURI.startsWith("/api/sign/reissue")) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }
}
