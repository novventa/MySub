package nova.mysub.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nova.mysub.global.auth.oauth2.SignUpRequestDto;
import nova.mysub.global.auth.oauth2.SignUpSuccessResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final KakaoSocialService kakaoSocialService;

    @PostMapping("/kakao/signup")
    public ResponseEntity<SignUpSuccessResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequest) {
        SignUpSuccessResponseDto response = kakaoSocialService.signUp(signUpRequest);
        return ResponseEntity.ok(response);
    }

}
