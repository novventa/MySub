package nova.mysub.global.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public void saveRefreshToken(
            final Long userId,
            final String refreshToken
    ) {
        tokenRepository.save(Token.of(userId, refreshToken));
    }

    public Long findIdByRefreshToken(
            final String refreshToken
    ) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(
                        () -> new RuntimeException("Refresh Token not found")
                );
        return token.getId();
    }

    @Transactional
    public void deleteRefreshToken(
            final Long userId
    ) {
        Token token = tokenRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Refresh Token not found")
        );
        tokenRepository.delete(token);
    }
}
