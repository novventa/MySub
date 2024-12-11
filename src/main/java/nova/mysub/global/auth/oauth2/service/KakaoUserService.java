package nova.mysub.global.auth.oauth2.service;

import lombok.RequiredArgsConstructor;
import nova.mysub.domain.user.entity.User;
import nova.mysub.domain.user.repository.UserRepository;
import nova.mysub.global.auth.oauth2.KakaoUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoUserService {

    private final UserRepository userRepository;

    @Transactional
    public User processKakaoUser(Map<String, Object> attributes) {
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

        return userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseGet(() -> userRepository.save(User.builder()
                        .kakaoId(Long.parseLong(attributes.get("id").toString()))
                        .email(kakaoUserInfo.getEmail())
                        .nickname(kakaoUserInfo.getNickname())
                        .profileImageUrl(kakaoUserInfo.getProfileImageUrl())
                        .role("USER")
                        .build()));
    }
}