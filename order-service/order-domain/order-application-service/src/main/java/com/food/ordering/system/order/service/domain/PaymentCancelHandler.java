package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.mesage.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Slf4j
@Validated
public class PaymentCancelHandler {



    public void cancelPayment(PaymentResponse paymentResponse){

    }
}
