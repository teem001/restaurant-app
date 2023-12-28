package com.food.ordering.system.domain.event.pusblisher;

import com.food.ordering.system.domain.event.DomainEvent;

public interface DomainEventPublisher <T extends DomainEvent>{

    void publish(T domainEvent);
}
