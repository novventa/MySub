package nova.mysub.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nova.mysub.domain.user.model.dto.UserDto;
import nova.mysub.domain.user.service.UserService;
import nova.mysub.global.auth.dto.SignUpRequestDto;
import nova.mysub.global.auth.dto.SignUpSuccessResponseDto;
import nova.mysub.global.auth.oauth2.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final KakaoService kakaoService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> userDto = userService.getUserById(id);
        return userDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/kakao/signup")
    public ResponseEntity<SignUpSuccessResponseDto> signUp(@RequestParam String accessToken) {
        SignUpSuccessResponseDto response = kakaoService.signUp(accessToken);
        return ResponseEntity.ok(response);
    }

}
