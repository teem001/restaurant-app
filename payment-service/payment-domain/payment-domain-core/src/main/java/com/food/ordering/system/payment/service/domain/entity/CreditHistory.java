package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryID;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;


public class CreditHistory extends BaseEntity<CreditHistoryID> {

    private final Money amount;
    private final CustomerId customerId;
    private final TransactionType transactionType;

    private CreditHistory(Builder builder) {
        setId(builder.creditHistoryID);
        customerId = builder.customerId;
        amount = builder.amount;
        transactionType = builder.transactionType;
    }

    public static Builder builder(){
        return new Builder();
    }


    public Money getAmount() {
        return amount;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public static final class Builder {
        private  CreditHistoryID creditHistoryID;
        private  CustomerId customerId;
        private  Money amount;
        private TransactionType transactionType;

        public Builder() {
        }


        public Builder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public  Builder amount(Money amount) {
            this.amount = amount;
            return this;
        }

        public Builder transactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder creditHistoryID(CreditHistoryID creditHistoryID) {
            this.creditHistoryID = creditHistoryID;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }
}
