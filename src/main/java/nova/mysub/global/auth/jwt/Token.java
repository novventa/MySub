package nova.mysub.global.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 1209600)
@AllArgsConstructor
@Getter
@Builder
public class Token {

    @Id
    private Long id;

    private String refreshToken;

    public static Token of(
            final Long id,
            final String refreshToken
    ) {
        return Token.builder()
                .id(id)
                .refreshToken(refreshToken)
                .build();
    }
}
