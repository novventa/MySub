package nova.mysub.domain.subscription.service;

import nova.mysub.domain.subscription.model.dto.SubscriptionDto;
import nova.mysub.domain.subscription.model.entity.Subscription;
import nova.mysub.domain.user_subscription.model.UserSubscriptionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    List<SubscriptionDto> getAllSubscriptions();
    Optional<SubscriptionDto> getSubscriptionById(Long id);
    SubscriptionDto createSubscription(SubscriptionDto subscriptionDto);
    SubscriptionDto updateSubscription(Long id, SubscriptionDto subscriptionDto);
    void deleteSubscription(Long id);
    UserSubscriptionDto addUserSubscription(Long userId, Long subscriptionId, LocalDate renewalDate);
    List<UserSubscriptionDto> getUserSubscriptions(Long userId);

}
