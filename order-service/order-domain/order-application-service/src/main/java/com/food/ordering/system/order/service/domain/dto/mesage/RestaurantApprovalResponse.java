package com.food.ordering.system.order.service.domain.dto.mesage;

import com.food.ordering.system.domain.valueobject.OrderApproveStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
@Builder
@Getter
@AllArgsConstructor
public class RestaurantApprovalResponse {
    private String id;
    private String sagaId;
    private String orderId;
    private String restaurantId;
    private String customerId;
    private BigDecimal price;
    private Instant createdAt;
    private OrderApproveStatus orderApproveStatus;
    private List<String> failMessages;
}
