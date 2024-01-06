package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.domain.event.pusblisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationEventDomainPublisher implements ApplicationEventPublisherAware, DomainEventPublisher<OrderCreateEvent> {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

        this.applicationEventPublisher = applicationEventPublisher;

    }

    @Override
    public void publish(OrderCreateEvent domainEvent) {
        this.applicationEventPublisher.publishEvent(domainEvent);
        log.info("OrderCreateEvent is published for order id : {}", domainEvent.getOrder().getId().getValue());
    }
}
