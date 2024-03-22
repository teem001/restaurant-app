package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.OrderApproveStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.kafka.order.avro.model.*;
import com.food.ordering.system.order.service.domain.dto.mesage.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.mesage.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreateEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreateEvent orderCreateEvent){

        Order order = orderCreateEvent.getOrder();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount().toString())
                .setCreatedAt(orderCreateEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelEvent orderCreateEvent){

        Order order = orderCreateEvent.getOrder();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount().toString())
                .setCreatedAt(orderCreateEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventTORestaurantApprovalAvroModel(OrderPaidEvent domainEvent) {

        Order order = domainEvent.getOrder();

        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId("")
                .setOrderId(order.getId().getValue())
                .setRestaurantId(order.getRestaurantId().getValue())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(order.getOrderItems().stream().map(orderItem -> Product.newBuilder()
                        .setId(orderItem.getOrderId().getValue().toString())
                        .setQuantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList()))
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(domainEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();

    }

    public PaymentResponse paymentResponseAvroModelTOPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {

        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failMessages(paymentResponseAvroModel.getFailureMessage())
                .build();

    }

    public RestaurantApprovalResponse restaurantApprovedAvroResponseToRestaurantResponse(RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {

        return RestaurantApprovalResponse.builder()
                .id(restaurantApprovalResponseAvroModel.getId())
                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
                .orderId(restaurantApprovalResponseAvroModel.getOrderId())
                .orderApproveStatus(OrderApproveStatus.valueOf(restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .failMessages(restaurantApprovalResponseAvroModel.getFailureMessage())
                .build();
    }
}
