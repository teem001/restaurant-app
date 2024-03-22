package com.food.ordering.system.order.service.messaging.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class OrderKafkaHelper {



    public <T> ListenableFutureCallback<SendResult<String, T>> getCallBackMethod(String responseTopicName, T requestAvroModel, String ID) {

        return new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending payment Request Approval with message {}, and topic {} ", responseTopicName, requestAvroModel, ex);
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();

                log.info("Received successful response from kafka for order id {}, topic {}, partition {}, offset {}, timestamp {}",
                        ID,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp());

            }
        };
    }

}
