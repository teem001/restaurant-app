package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.mesage.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {


    private final OrderApprovedHandler orderApprovedHandler;

    private final OrderRejectedHandler orderRejectedHandler;

    public RestaurantApprovalResponseMessageListenerImpl(OrderApprovedHandler orderApprovedHandler, OrderRejectedHandler orderRejectedHandler) {
        this.orderApprovedHandler = orderApprovedHandler;
        this.orderRejectedHandler = orderRejectedHandler;
    }

    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

        orderApprovedHandler.approveOrder(restaurantApprovalResponse);
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

        orderRejectedHandler.rejectOrder(restaurantApprovalResponse);
    }
}
