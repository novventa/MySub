package nova.mysub.domain.subscription.repository;

import nova.mysub.domain.subscription.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
