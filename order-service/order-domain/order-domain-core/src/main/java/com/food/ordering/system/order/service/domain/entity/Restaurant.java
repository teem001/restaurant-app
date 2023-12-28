package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

    private final List<Product> products;

    private boolean isActive;

    private Restaurant(Builder builder) {
        super.setId( builder.id);
        products = builder.products;
        isActive = builder.isActive;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static final class Builder {
        private RestaurantId id;
        private final List<Product> products;
        private boolean isActive;

        public Builder(List<Product> products) {
            this.products = products;
        }

        public Builder id(RestaurantId val) {
            id = val;
            return this;
        }

        public Builder isActive(boolean val) {
            isActive = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }

}
