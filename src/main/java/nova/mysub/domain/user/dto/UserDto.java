package nova.mysub.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import nova.mysub.domain.user.entity.User;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String nickname;
    private String role;
    private String profileImageUrl;
    private Long kakaoId;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .kakaoId(user.getKakaoId())
                .build();
    }

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .nickname(this.nickname)
                .role(this.role)
                .profileImageUrl(this.profileImageUrl)
                .kakaoId(this.kakaoId)
                .build();
    }
}
