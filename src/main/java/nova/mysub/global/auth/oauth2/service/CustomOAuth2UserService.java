package nova.mysub.global.auth.oauth2.service;

import lombok.RequiredArgsConstructor;
import nova.mysub.domain.user.entity.User;
import nova.mysub.global.auth.oauth2.KakaoUserDetail;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final KakaoUserService kakaoUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        User user = kakaoUserService.processKakaoUser(oAuth2User.getAttributes());

        return new KakaoUserDetail(
                user.getEmail(),
                user.getNickname(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())),
                oAuth2User.getAttributes()
        );
    }
}