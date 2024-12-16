package nova.mysub.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenValidity = 1000L * 60 * 15; // 15분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일

    public JwtTokenProvider() {
        Dotenv dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .ignoreIfMissing()
                .load();
        String secret = dotenv.get("JWT_SECRET");
        if (secret == null) {
            throw new IllegalArgumentException("JWT_SECRET is missing in .env file");
        }

        // HMAC SHA256 알고리즘으로 SecretKey 생성
        this.secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }
    // Access Token 생성
    public String createAccessToken(Long userId) {
        return generateToken(userId, accessTokenValidity);
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId) {
        return generateToken(userId, refreshTokenValidity);
    }

    // Access Token과 Refresh Token 동시 생성
    public TokenDto createTokens(Long userId) {
        String accessToken = createAccessToken(userId);
        String refreshToken = createRefreshToken(userId);
        return TokenDto.of(accessToken, refreshToken);
    }

    private String generateToken(Long userId, long validity) {
        try {
            JwtBuilder builder = Jwts.builder()
                    .subject(String.valueOf(userId))  // 사용자 ID를 subject로 설정
                    .issuedAt(new Date())            // 발급 시간
                    .expiration(new Date(System.currentTimeMillis() + validity)) // 만료 시간
                    .signWith(secretKey);

            return builder.compact();
        } catch (InvalidKeyException e) {
            throw new IllegalStateException("Invalid Secret Key", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey) // verifyWith 메서드로 SecretKey 설정
                    .build()
                    .parseSignedClaims(token); // 토큰 검증 및 클레임 파싱
            return true;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            return false; // 검증 실패
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey) // SecretKey 설정
                .build()
                .parseSignedClaims(token) // 토큰 검증 및 클레임 파싱
                .getPayload(); // Payload(클레임) 가져오기

        return Long.valueOf(claims.getSubject()); // Subject는 userId로 설정됨
    }

    public Authentication getAuthentication(String token) {
        Long userId = getUserIdFromToken(token); // 토큰에서 userId 추출
        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // 권한
        );
    }
}
