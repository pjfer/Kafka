package com.as.pa2_t2g01.CollectEntity;

import com.as.pa2_t2g01.KafkaConstants.IKafkaConstants;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * 
 */
public class CollectEntity {
    public static void main(String[] args) {
        String key = "Key1";
        String value = "Value1";
        List<String> messages = new ArrayList<>();
        
        try (Stream<String> stream = Files.lines(Paths.get(IKafkaConstants.CARS_FILENAME))) {
            messages = stream.collect(Collectors.toList());
        }
        catch (IOException e) {
            System.err.println("ERROR: Unable to open the file!");
        }
        
        messages.forEach(System.out::println);
        
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.BROKERS_ADDRESSES);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IKafkaConstants.KEY_SERIALIZER);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IKafkaConstants.VALUE_SERIALIZER);

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(IKafkaConstants.BATCH_TOPIC, key, value);
            producer.send(record);
            System.out.println("Message sent!");
        }
    }
}
