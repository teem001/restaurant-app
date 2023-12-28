package com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.food.ordering.system.domain.event.pusblisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCancelEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelEvent> {
}
