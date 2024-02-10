package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducerInterface;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderCreateEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingMapper orderMessagingMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducerInterface<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderKafkaHelper orderKafkaHelper;

    public CreateOrderKafkaMessagePublisher(OrderMessagingMapper orderMessagingMapper, OrderServiceConfigData orderServiceConfigData, KafkaProducerInterface<String, PaymentRequestAvroModel> kafkaProducer, OrderKafkaHelper orderKafkaHelper) {
        this.orderMessagingMapper = orderMessagingMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.orderKafkaHelper = orderKafkaHelper;
    }

    @Override
    public void publish(OrderCreateEvent domainEvent) {

        String orderId = domainEvent.getOrder().getId().toString();
        try {


            log.info("Received OrderCreatedEvent for order in id : {}", orderId);
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingMapper.orderCreatedEventToPaymentRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(), orderId, paymentRequestAvroModel, orderKafkaHelper.getCallBackMethod(orderServiceConfigData.getPaymentResponseTopicName(), paymentRequestAvroModel, orderId));

            log.info("Payment Request approval sent to Kafka for order id {}", paymentRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending payment request Approval message to kafka with order id {}, error {}", orderId, e.getMessage());
        }
    }


}
