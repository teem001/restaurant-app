package com.food.ordering.system.payment.service.domain.execption;

import com.food.ordering.system.domain.exception.DomainException;

public class PaymentDomainException extends DomainException {
     public PaymentDomainException(String message) {
        super(message);
    }
    public PaymentDomainException(String message, Throwable throwable){
         super(message, throwable);
    }
}
