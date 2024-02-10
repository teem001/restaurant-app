package com.food.ordering.system.order.service.domain.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "order-service")
public class OrderServiceConfigData {

    @Value("${payment-request-topic-name}")
    private String paymentRequestTopicName;
    @Value("${payment-response-topic-name}")
    private String paymentResponseTopicName;
    @Value("${restaurant-approval-request-topic-name}")
    private String restaurantApprovalRequestTopicName;
    @Value("${restaurant-approval-response-topic-name}")
    private String restaurantApprovalResponseTopicName;


}
