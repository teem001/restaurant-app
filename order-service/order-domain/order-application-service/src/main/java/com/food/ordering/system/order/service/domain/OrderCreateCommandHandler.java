package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.event.OrderCreateEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderObjectMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j

public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderObjectMapper orderObjectMapper;

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper,
                                     OrderObjectMapper orderObjectMapper,
                                     OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderObjectMapper = orderObjectMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {

        OrderCreateEvent orderCreateEvent = orderCreateHelper.persistOrder(createOrderCommand);

        orderCreatedPaymentRequestMessagePublisher.publish(orderCreateEvent);

        return orderObjectMapper.orderToCreateOrderResponse(orderCreateEvent.getOrder(), "");


    }


}
