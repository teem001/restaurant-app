package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.kafka.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant.RestaurantApprovalResponseMessageListener;
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
public class RestaurantResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final OrderMessagingMapper orderMessagingMapper;
    private final RestaurantApprovalResponseMessageListener restaurantResponseKafkaListener;

    public RestaurantResponseKafkaListener(OrderMessagingMapper orderMessagingMapper, RestaurantApprovalResponseMessageListener restaurantResponseKafkaListener) {
        this.orderMessagingMapper = orderMessagingMapper;
        this.restaurantResponseKafkaListener = restaurantResponseKafkaListener;
    }


    @Override
    @KafkaListener(id = "${kafka-consumer.restaurant-approval-consumer-group-id}",
    topics = "${order-service.restaurant-approval-response-topic}")
    public void receive(
            @Payload List<RestaurantApprovalResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offset) {

        log.info("Restaurant approve with number of response {}, key {}, partition {} and offset {}", messages.size(),
                keys.toString(), partitions.toString(), offset.toString());
        messages.forEach(
                p-> {
                    if(p.getOrderApprovalStatus() == OrderApprovalStatus.APPROVED) {
                        log.info("Response id Approved for order id {}", p.getOrderId());
                        restaurantResponseKafkaListener.orderApproved(orderMessagingMapper.restaurantApprovedAvroResponseToRestaurantResponse(p));

                    }
                    else if (p.getOrderApprovalStatus() == OrderApprovalStatus.REJECTED){
                        log.info("Response for failed restaurant approval for order id {}", p.getOrderId());

                        restaurantResponseKafkaListener.orderRejected(orderMessagingMapper.restaurantApprovedAvroResponseToRestaurantResponse(p));
                    }
                }
        );

    }
}
