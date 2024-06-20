package nova.mysub.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpSuccessResponseDto {
    private String accessToken;
    private String refreshToken;
}
