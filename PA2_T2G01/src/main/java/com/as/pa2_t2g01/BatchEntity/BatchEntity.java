package com.as.pa2_t2g01.BatchEntity;

import com.as.pa2_t2g01.KafkaConstants.IKafkaConstants;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * 
 * 
 */
public class BatchEntity {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.BROKERS_ADDRESSES);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IKafkaConstants.KEY_DESERIALIZER);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, IKafkaConstants.VALUE_DESERIALIZER);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, IKafkaConstants.BATCH_TOPIC_GROUP);
        
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(IKafkaConstants.BATCH_TOPIC));
            
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                
                records.forEach(System.out::println);
            }
        }
    }
}
