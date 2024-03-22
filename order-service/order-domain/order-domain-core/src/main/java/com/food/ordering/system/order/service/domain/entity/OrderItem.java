package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderId orderId;
    private final Product product;

    private final int quantity;

    private final Money price;
    private final Money subtotal;


    private OrderItem(Builder builder) {
        super();
        super.setId(builder.id);
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subtotal = builder.subtotal;
    }

    public static Builder builder() {
        return new Builder();
    }


    public OrderId getOrderId() {
        return orderId;
    }

    public void setOrderId(OrderId orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubtotal() {
        return subtotal;
    }

    void initializeOrderItem(OrderId id, OrderItemId orderItemId) {
        this.orderId = id;
        super.setId(orderItemId);
    }

    boolean isPriceValid() {
        return price.isGreaterThanZero()
                && price.equals(product.getPrice())
                && price.multiply(quantity).equals(subtotal);
    }


    public static final class Builder {
        private OrderItemId id;
        private Product product;
        private int quantity;
        private Money price;
        private Money subtotal;

        public Builder(Product product, int quantity, Money price, Money subtotal) {
            this.product = product;
            this.quantity = quantity;
            this.price = price;
            this.subtotal = subtotal;
        }

        public Builder() {
        }

        public Builder id(OrderItemId val) {
            id = val;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }

        public Builder price(Money money) {
            this.price = money;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder subtotal(Money money) {
            this.subtotal = money;
            return this;
        }
    }
}
