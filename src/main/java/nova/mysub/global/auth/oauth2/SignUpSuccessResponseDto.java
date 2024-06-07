package nova.mysub.global.auth.oauth2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpSuccessResponseDto {
    private String token;
}
