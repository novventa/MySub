package nova.mysub.domain.subscription.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto {

    private Long id;
    private String serviceName;
    private BigDecimal fee;
    private String description;

}
