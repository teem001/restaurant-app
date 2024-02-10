package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> orderItems;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessage;

    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItem();
    }

    public void validateOrder() throws OrderDomainException {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING)
            throw new OrderDomainException("Order is not in correct state for pay operation");
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID)
            throw new OrderDomainException("Order is not in correct state for approve");

        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID)
            throw new OrderDomainException("Order is not correct state for cancelling");

        orderStatus = OrderStatus.CANCELLING;

        updateFailMessages(failureMessages);
    }

    private void updateFailMessages(List<String> failureMessages) {
        if (this.failureMessage != null && failureMessages != null) {
            failureMessage.addAll(failureMessages.stream().filter(p -> !p.isEmpty()).toList());
        }
        if (this.failureMessage == null) {
            failureMessage = failureMessages;
        }
    }

    public void cancel(List<String> failureMessages) {
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING))
            throw new OrderDomainException("The state not correct for cancel");

        orderStatus = OrderStatus.CANCELED;

        updateFailMessages(failureMessages);
    }


    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null)
            throw new OrderDomainException("Order not in the correct state for initialization");
    }


    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero())
            throw new OrderDomainException("Order price must be greater than zero");

    }

    private void validateItemsPrice() {
        Money orderItemTotal = orderItems.stream().map(p -> {
            validateItemPrice(p);
            return p.getSubtotal();
        }).reduce(Money.ZERO, Money::add);

        if (price.equals(orderItemTotal)) {
            throw new OrderDomainException("The total price ");
        }
    }

    private void validateItemPrice(OrderItem p) {
        if (p.isPriceValid()) throw new OrderDomainException("The order price is not valid");
    }

    private void initializeOrderItem() {
        long itemID = 1L;
        for (OrderItem orderItem : orderItems) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemID++));
        }
    }

    private Order(Builder builder) {
        super.setId(builder.id);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        streetAddress = builder.streetAddress;
        price = builder.money;
        orderItems = builder.orderItems;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessage = builder.failureMessage;
    }


    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getStreetAddress() {
        return streetAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessage() {
        return failureMessage;
    }

    public static final class Builder {
        private OrderId id;
        private final CustomerId customerId;
        private final RestaurantId restaurantId;
        private final StreetAddress streetAddress;
        private final Money money;
        private final List<OrderItem> orderItems;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessage;

        public Builder(CustomerId customerId, RestaurantId restaurantId, StreetAddress streetAddress, Money money, List<OrderItem> orderItems) {
            this.customerId = customerId;
            this.restaurantId = restaurantId;
            this.streetAddress = streetAddress;
            this.money = money;
            this.orderItems = orderItems;
        }

        public Builder id(OrderId val) {
            id = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessage(List<String> val) {
            failureMessage = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
