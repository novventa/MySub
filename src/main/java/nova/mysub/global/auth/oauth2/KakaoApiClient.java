package nova.mysub.global.auth.oauth2;

import nova.mysub.global.auth.dto.KakaoUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.http.HttpHeaders;

@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com")
public class KakaoApiClient {

    @GetMapping(value = "/v2/user/me")
    KakaoUserResponse getUserInformation(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}
