package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreateEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {
    private static final String UTC = "UTC";

    @Override
    public OrderCreateEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id : {} is initiated", order.getId());
        return new OrderCreateEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getOrderItems().forEach(p-> restaurant.getProducts().forEach(q-> {
            Product currentProduct = p.getProduct();
            if(currentProduct.equals(q)) currentProduct.updateWithConfirmedNameAndPrice(q.getName(), q.getPrice());
        }));
    }

    private void validateRestaurant(Restaurant restaurant) {

        if(!restaurant.isActive()) throw new  OrderDomainException("The restaurant with id"+ restaurant.getId().getValue() + "is not active");

    }

    @Override
    public OrderPaidEvent payOrder(Order order) {

        order.pay();
        log.info("Order with payment id {} is paid", order.getId().getValue());

        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {

        order.approve();
        log.info("Order with id {} is approved", order.getId());
    }

    @Override
    public OrderCancelEvent cancelOrderPayment(Order order, List<String> failedMessages) {

        order.initCancel(failedMessages);
        return new OrderCancelEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failMessage) {

    }
}
