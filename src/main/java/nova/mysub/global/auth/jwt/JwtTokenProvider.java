package nova.mysub.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nova.mysub.domain.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    private static final long TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L;

    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    protected void init() {
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
        Claims claims = (Claims) Jwts.claims().setSubject(userId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserById(getUserIdFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
