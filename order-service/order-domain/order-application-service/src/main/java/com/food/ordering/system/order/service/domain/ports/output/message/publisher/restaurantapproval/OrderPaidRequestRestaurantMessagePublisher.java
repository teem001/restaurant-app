package com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.food.ordering.system.domain.event.pusblisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRequestRestaurantMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
