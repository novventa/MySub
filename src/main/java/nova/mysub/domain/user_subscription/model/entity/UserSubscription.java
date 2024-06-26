package nova.mysub.domain.user_subscription.model.entity;

import jakarta.persistence.*;
import lombok.*;
import nova.mysub.domain.subscription.model.entity.Subscription;
import nova.mysub.domain.user.model.entity.User;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    private LocalDate renewalDate;

}
