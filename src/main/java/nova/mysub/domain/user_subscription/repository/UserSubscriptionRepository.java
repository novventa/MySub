package nova.mysub.domain.user_subscription.repository;

import nova.mysub.domain.user_subscription.model.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    List<UserSubscription> findByUserId(Long userId);
}
