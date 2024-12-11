package nova.mysub.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import org.springframework.security.oauth2.jwt.JwtException;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
    public String generateAccessToken(Long userId) {
        return generateToken(userId, accessTokenValidity);
    }

    // Refresh Token 생성
    public String generateRefreshToken(Long userId) {
        return generateToken(userId, refreshTokenValidity);
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
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 검증 실패
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey) // verifyWith 메서드로 SecretKey 설정
                .build()
                .parseSignedClaims(token) // 토큰 검증 및 클레임 추출
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }
}
