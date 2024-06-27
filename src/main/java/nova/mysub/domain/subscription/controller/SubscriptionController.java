package nova.mysub.domain.subscription.controller;

import lombok.RequiredArgsConstructor;
import nova.mysub.domain.subscription.model.dto.SubscriptionDto;
import nova.mysub.domain.subscription.service.SubscriptionService;
import nova.mysub.domain.user_subscription.model.UserSubscriptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionDto> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getSubscriptionById(@PathVariable Long id) {
        Optional<SubscriptionDto> subscription = subscriptionService.getSubscriptionById(id);
        return subscription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public SubscriptionDto createSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        return subscriptionService.createSubscription(subscriptionDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto> updateSubscription(@PathVariable Long id, @RequestBody SubscriptionDto subscriptionDto) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(id, subscriptionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{subscriptionId}/users/{userId}")
    public ResponseEntity<UserSubscriptionDto> addUserSubscription(@PathVariable Long userId, @PathVariable Long subscriptionId, @RequestParam LocalDate renewalDate) {
        UserSubscriptionDto userSubscription = subscriptionService.addUserSubscription(userId, subscriptionId, renewalDate);
        return ResponseEntity.ok(userSubscription);
    }

    @GetMapping("/users/{userId}")
    public List<UserSubscriptionDto> getUserSubscriptions(@PathVariable Long userId) {
        return subscriptionService.getUserSubscriptions(userId);
    }
}
