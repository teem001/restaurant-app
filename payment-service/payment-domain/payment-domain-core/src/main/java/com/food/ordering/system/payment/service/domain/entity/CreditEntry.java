package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntryID;

public class CreditEntry extends BaseEntity<CreditEntryID> {

    private final CustomerId customerId;

    private Money totalCreditAmount;


    public void addCreditAmount(Money amount){
        totalCreditAmount =  totalCreditAmount.add(amount);
    }
    public void subtractCreditAmount(Money amount){
        totalCreditAmount = totalCreditAmount.subtract(amount);
    }


    private CreditEntry(Builder builder) {
        setId(builder.id);
        customerId = builder.customerId;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public static Builder newBuilder(){
        return new Builder();
    }


    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public static final class Builder {
        private CreditEntryID id;
        private CustomerId customerId;
        private Money totalCreditAmount;

        public Builder() {
        }

        public Builder creditId(CreditEntryID val) {
            id = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
