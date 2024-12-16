package nova.mysub.global.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}