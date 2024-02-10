package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.kafka.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;

    private final OrderMessagingMapper orderMessagingMapper;

    public PaymentResponseKafkaListener(PaymentResponseMessageListener paymentResponseMessageListener, OrderMessagingMapper orderMessagingMapper) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingMapper = orderMessagingMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-topic-name}")
    public void receive(
            @Payload List<PaymentResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offset) {

        log.info("{} number of payment responses received with keys: {}, partition {}, and offset {}",
                messages.size(), keys.toString(), partitions.toString(), offset.toString() );

        messages.forEach(
                p-> {
                    if(p.getPaymentStatus() == PaymentStatus.COMPLETED){
                        log.info("processing completed for order is {}", p.getOrderId());

                        paymentResponseMessageListener.paymentCompleted(orderMessagingMapper.paymentResponseAvroModelTOPaymentResponse(p));
                    } else if (p.getPaymentStatus()== PaymentStatus.CANCELLED || p.getPaymentStatus() == PaymentStatus.FAILED) {
                        log.info("processing falled or cancelled for order {}", p.getOrderId());

                        paymentResponseMessageListener.paymentCancelled( orderMessagingMapper.paymentResponseAvroModelTOPaymentResponse(p));
                        
                    }
                }
        );

    }
}
