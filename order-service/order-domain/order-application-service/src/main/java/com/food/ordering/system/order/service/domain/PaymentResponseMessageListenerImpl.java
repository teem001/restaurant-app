package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.mesage.PaymentResponse;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final PaymentCompletedHandler paymentCompletedHandler;

    private final PaymentCancelHandler paymentCancelHandler;

    public PaymentResponseMessageListenerImpl(PaymentCompletedHandler paymentCompletedHandler, PaymentCancelHandler paymentCancelHandler) {
        this.paymentCompletedHandler = paymentCompletedHandler;
        this.paymentCancelHandler = paymentCancelHandler;
    }

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

        paymentCompletedHandler.completePayment(paymentResponse);
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {

        paymentCancelHandler.cancelPayment(paymentResponse);



    }
}
