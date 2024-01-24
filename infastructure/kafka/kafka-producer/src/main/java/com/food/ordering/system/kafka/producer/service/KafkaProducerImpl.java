package com.food.ordering.system.kafka.producer.service;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducerInterface<K, V>{

    private final KafkaTemplate<K,V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void send(String topicName, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback) {

        log.info("Sending message = {}, to topic = {}", message, topicName);

        try {
            CompletableFuture<SendResult<K, V>> kafkaResult = kafkaTemplate.send(topicName, key, message);

            kafkaResult.whenComplete((res, ex) -> {
                if (ex == null) callback.onSuccess(res);
                else callback.onFailure(ex);
            });

        }catch (KafkaException e){

            log.error("Error on kafka producer with key : {}, message : {} and exception ; {}", key, message, e.getMessage());

            throw  new KafkaProducerException("Error on Kafka producer with key : "+ key + "and message: "+ message);
        }

    }

    @PreDestroy
    public void close(){
       if(kafkaTemplate != null){
           log.info("Closing kafka producer");
           kafkaTemplate.destroy();
       }
    }
}
