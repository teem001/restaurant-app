package com.food.ordering.system.kafka.consumer.kafka;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;
import java.util.Locale;

public interface KafkaConsumer <T extends SpecificRecordBase>{

    void receive(List<T> messages, List<Long> keys, List<Integer> partitions, List<Long> offset);
}
