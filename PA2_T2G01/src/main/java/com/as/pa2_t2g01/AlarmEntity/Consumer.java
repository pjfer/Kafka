package main.java.com.as.pa2_t2g01.AlarmEntity;

import main.java.com.as.pa2_t2g01.BatchEntity.*;
import java.time.Duration;
import java.util.Collections;
import main.java.com.as.pa2_t2g01.Data.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;


public class Consumer extends Thread{
    
    private final KafkaConsumer<String, Message> consumer;
    private final SharedRegion sr;
    
    public Consumer(KafkaConsumer<String, Message> consumer, SharedRegion sr){
        this.consumer = consumer;
        this.sr = sr;
    }
    
    @Override
    public void run(){
        boolean alarm = false;
        while (true) {
            ConsumerRecords<String, Message> records =
                    consumer.poll(Duration.ofMillis(100));
            
            for (ConsumerRecord<String, Message> record : records){
                Message m = record.value();
                
                if(m.getMessage_type()== 1){
                    consumer.commitSync(
                            Collections.singletonMap(
                                    new TopicPartition(record.topic(), 
                                            record.partition()),
                                    new OffsetAndMetadata(record.offset()+1)));
                    if(!alarm && m.getSpeed() > 120){
                        alarm = true;
                        sr.writeFile(m.toString() + " | ON |");
                        sr.updateAlarm(alarm, m.toString() + " | ON |");
                    }
                    if(alarm && m.getSpeed() < 120){
                        alarm = false;
                        sr.updateAlarm(alarm, m.toString() + " | OFF |");
                        sr.writeFile(m.toString() + " | OFF |");
                    }
                }
                sr.writeScreen(m.toString());
            }
            consumer.commitSync();
        }
    }
}
