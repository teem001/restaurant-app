package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducerInterface;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRequestRestaurantMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PayOrderKafkaMessagePublisher implements OrderPaidRequestRestaurantMessagePublisher {

    private final OrderMessagingMapper orderMessagingMapper;

    private final KafkaProducerInterface<String, RestaurantApprovalRequestAvroModel> kafkaProducerConfig;

    private final OrderServiceConfigData orderServiceConfigData;

    private final OrderKafkaHelper orderKafkaHelper;

    public PayOrderKafkaMessagePublisher(OrderMessagingMapper orderMessagingMapper, KafkaProducerInterface<String, RestaurantApprovalRequestAvroModel> kafkaProducerConfig, OrderServiceConfigData orderServiceConfigData, OrderKafkaHelper orderKafkaHelper) {
        this.orderMessagingMapper = orderMessagingMapper;
        this.kafkaProducerConfig = kafkaProducerConfig;
        this.orderServiceConfigData = orderServiceConfigData;
        this.orderKafkaHelper = orderKafkaHelper;
    }

    @Override
    public void publish(OrderPaidEvent domainEvent) {

        String orderId = domainEvent.getOrder().getId().getValue().toString();

        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingMapper.orderPaidEventTORestaurantApprovalAvroModel(domainEvent);

            kafkaProducerConfig.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(), orderId, restaurantApprovalRequestAvroModel,
                    orderKafkaHelper.getCallBackMethod(orderServiceConfigData.getRestaurantApprovalRequestTopicName(), restaurantApprovalRequestAvroModel, orderId));

            log.info("Restaurant approval sent to kafka for order id {}", orderId);
        } catch (Exception e) {
            log.error("Error sending the restaurant approval response to kafka ", e.fillInStackTrace());
        }

    }
}
