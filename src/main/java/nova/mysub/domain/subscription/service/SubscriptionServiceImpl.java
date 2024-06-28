package nova.mysub.domain.subscription.service;

import lombok.RequiredArgsConstructor;
import nova.mysub.domain.subscription.model.dto.SubscriptionDto;
import nova.mysub.domain.subscription.model.entity.Subscription;
import nova.mysub.domain.subscription.repository.SubscriptionRepository;
import nova.mysub.domain.user.model.dto.UserDto;
import nova.mysub.domain.user.model.entity.User;
import nova.mysub.domain.user.repository.UserRepository;
import nova.mysub.domain.user_subscription.model.UserSubscriptionDto;
import nova.mysub.domain.user_subscription.model.entity.UserSubscription;
import nova.mysub.domain.user_subscription.repository.UserSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserRepository userRepository;

    public List<SubscriptionDto> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<SubscriptionDto> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .map(this::convertToDto);
    }

    public SubscriptionDto createSubscription(SubscriptionDto subscriptionDto) {
        Subscription subscription = Subscription.builder()
                .serviceName(subscriptionDto.getServiceName())
                .fee(subscriptionDto.getFee())
                .description(subscriptionDto.getDescription())
                .build();
        subscription = subscriptionRepository.save(subscription);
        return convertToDto(subscription);
    }

    public SubscriptionDto updateSubscription(Long id, SubscriptionDto subscriptionDto) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscription = Subscription.builder()
                .id(id)
                .serviceName(subscriptionDto.getServiceName())
                .fee(subscriptionDto.getFee())
                .description(subscriptionDto.getDescription())
                .build();
        subscription = subscriptionRepository.save(subscription);
        return convertToDto(subscription);
    }

    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public UserSubscriptionDto addUserSubscription(Long userId, Long subscriptionId, LocalDate renewalDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        UserSubscription userSubscription = UserSubscription.builder()
                .user(user)
                .subscription(subscription)
                .renewalDate(renewalDate)
                .build();

        userSubscription = userSubscriptionRepository.save(userSubscription);
        return convertToUserSubscriptionDto(userSubscription);
    }

    public List<UserSubscriptionDto> getUserSubscriptions(Long userId) {
        return userSubscriptionRepository.findByUserId(userId).stream()
                .map(this::convertToUserSubscriptionDto)
                .collect(Collectors.toList());
    }

    private SubscriptionDto convertToDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .serviceName(subscription.getServiceName())
                .fee(subscription.getFee())
                .description(subscription.getDescription())
                .build();
    }

    private UserSubscriptionDto convertToUserSubscriptionDto(UserSubscription userSubscription) {
        return UserSubscriptionDto.builder()
                .id(userSubscription.getId())
                .user(UserDto.builder()
                        .id(userSubscription.getUser().getId())
                        .username(userSubscription.getUser().getUsername())
                        .email(userSubscription.getUser().getEmail())
                        .profileImageUrl(userSubscription.getUser().getProfileImageUrl())
                        .role(userSubscription.getUser().getRole())
                        .build())
                .subscription(convertToDto(userSubscription.getSubscription()))
                .renewalDate(userSubscription.getRenewalDate())
                .build();
    }
}
