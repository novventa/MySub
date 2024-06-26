package nova.mysub.domain.user_subscription.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nova.mysub.domain.subscription.model.dto.SubscriptionDto;
import nova.mysub.domain.user.model.dto.UserDto;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSubscriptionDto {

    private Long id;
    private UserDto user;
    private SubscriptionDto subscription;
    private LocalDate renewalDate;

}
