package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.valueobject.PaymentId;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class Payment extends AggregateRoot<PaymentId> {

    private final Money price;
    private final OrderId orderId;
    private ZonedDateTime createdAt;
    private PaymentStatus paymentStatus;
    private final CustomerId customerId;

    private Payment(Builder builder) {
        setId( builder.id);
        price = builder.price;
        orderId = builder.orderId;
        createdAt = builder.createdAt;
        paymentStatus = builder.paymentStatus;
        customerId = builder.customerId;
    }
    public static Builder newBuilder(){
        return new Builder();
    }


    public void initializePayment(){
        setId(new PaymentId(UUID.randomUUID()));
        createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public void validatePayment(List<String> failMessages){

        if(price == null || !price.isGreaterThanZero()){
            failMessages.add("Total price must be greater than zero!");
        }
    }

    public void updateStatus(PaymentStatus paymentStatus){
        this.paymentStatus = paymentStatus;
    }

    public static final class Builder {
        private PaymentId id;
        private  Money price;
        private  OrderId orderId;
        private ZonedDateTime createdAt;
        private PaymentStatus paymentStatus;
        private CustomerId customerId;

        public Builder() {
        }

        public Builder paymentId(PaymentId val) {
            id = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            createdAt = val;
            return this;
        }

        public Builder paymentStatus(PaymentStatus val) {
            paymentStatus = val;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}
