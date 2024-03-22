package com.food.ordering.system.order.service.dataaccess.order.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order) {

        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(deliveryAddressToAddressEntity(order.getStreetAddress()))
                .price(order.getPrice().getAmount())
                .orderItemEntities(orderItemsToOrderItemEntities(order.getOrderItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessage() != null ? String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessage()) : "")
                .build();
        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getOrderItemEntities().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));

        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        Order order = Order.builder()
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .streetAddress(addressEntityToDeliveryAddress(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .orderItems(orderItemEtityToOrderItem(orderEntity.getOrderItemEntities()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessage(orderEntity.getFailureMessages().isEmpty() ?
                        new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages().split(FAILURE_MESSAGE_DELIMITER))))
                .build();
        order.setId(new OrderId(orderEntity.getId()));

        return order;
    }

    private List<OrderItem> orderItemEtityToOrderItem(List<OrderItemEntity> orderItemEntities) {

        return orderItemEntities.stream()
                .map(orderItem -> OrderItem.builder()
                        .id(new OrderItemId(orderItem.getId()))
                        .product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subtotal(new Money(orderItem.getSubTotal()))
                        .build()).collect(Collectors.toList());
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {

        return new StreetAddress(address.getId(), address.getStreet(), address.getPostalCode(), address.getCity());
    }

    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> orderItems) {

        return orderItems
                .stream()
                .map(orderItem -> OrderItemEntity.builder()
                        .id(orderItem.getId().getValue())
                        .productId(orderItem.getProduct().getId().getValue())
                        .price(orderItem.getPrice().getAmount())
                        .quantity(orderItem.getQuantity())
                        .subTotal(orderItem.getSubtotal().getAmount())
                        .build()).collect(Collectors.toList());
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress streetAddress) {
        return OrderAddressEntity.builder()
                .id(streetAddress.getId())
                .street(streetAddress.getStreet())
                .city(streetAddress.getCity())
                .postalCode(streetAddress.getPostalCode())
                .build();
    }
}
