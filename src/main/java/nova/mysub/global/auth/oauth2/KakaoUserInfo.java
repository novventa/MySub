package nova.mysub.global.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class KakaoUserInfo {

    public static final String KAKAO_ACCOUNT = "kakao_account";
    public static final String EMAIL = "email";
    public static final String PROFILE = "profile";
    public static final String NICKNAME = "nickname";
    public static final String PROFILE_IMAGE_URL = "profile_image_url";

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        Map<String, Object> kakaoAccount = extractMap(attributes.get(KAKAO_ACCOUNT));
        return (String) kakaoAccount.get(EMAIL);
    }

    public String getNickname() {
        Map<String, Object> kakaoAccount = extractMap(attributes.get(KAKAO_ACCOUNT));
        Map<String, Object> profile = extractMap(kakaoAccount.get(PROFILE));
        return (String) profile.get(NICKNAME);
    }

    public String getProfileImageUrl() {
        Map<String, Object> kakaoAccount = extractMap(attributes.get(KAKAO_ACCOUNT));
        Map<String, Object> profile = extractMap(kakaoAccount.get(PROFILE));
        return (String) profile.get(PROFILE_IMAGE_URL);
    }

    private Map<String, Object> extractMap(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(object, new TypeReference<>() {});
    }
}