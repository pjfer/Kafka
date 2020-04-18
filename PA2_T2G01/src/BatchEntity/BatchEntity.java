package BatchEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import Data.Message;
import BatchEntity.GUI.BatchGUI;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * 
 * 
 */
public class BatchEntity {
    private static final String kafka_server = "localhost:9092";
    private static final String create_topics = "false";
    private static final String enable_commit = "false";
    private static final String max_records = "50";
    private static final String batch_topic = "BatchTopic";
    private static final int n_consumers = 3;
    
    public static void main(String[] args) throws IOException {
        
        Properties props = new Properties();
        props.put("bootstrap.servers", kafka_server);
        props.put("allow.auto.create.topics", create_topics);
        props.put("enable.auto.commit", enable_commit);
        props.put("max.poll.records", max_records);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "main.java.com.as.pa2_t2g01.Data.MessageDeserializer");
        props.put("group.id", "0");
        
        BatchGUI gui = new BatchGUI();
        gui.startGUI(gui);
        
        FileWriter batchFile = new FileWriter(System.getProperty("user.dir").concat("/src/Data/BATCH.txt"));
        
        SharedRegion sr = new SharedRegion(batchFile, gui);
        
        for(int i = 0; i < n_consumers; i++){
            KafkaConsumer<String, Message> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList(batch_topic));
            Consumer c = new Consumer(consumer, sr);
            c.start();
        }
        
    }
}
