package com.food.ordering.system.order.service.domain.exception;

public class OrderNotFoundExeception extends OrderDomainException {
    public OrderNotFoundExeception(String message) {
        super(message);
    }

    public OrderNotFoundExeception(String message, Throwable cause) {
        super(message, cause);
    }
}
