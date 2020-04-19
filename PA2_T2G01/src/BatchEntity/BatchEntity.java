package BatchEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import Data.Message;
import BatchEntity.GUI.BatchGUI;
import KafkaEntities.Consumer;
import KafkaEntities.RebalanceListener;
import KafkaEntities.SharedRegion;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Entity that represents the batch, it generates three kafka consumers
 * with group.id 0 that subscribe the topic BatchTopic
 * 
 * @author Rafael Teixeira & Pedro Ferreira
 */
public class BatchEntity {
    /**
     * Kafka broker address.
     */
    private static final String kafka_server = "localhost:9092";
    
    /**
     * Disables the automatic creation of topics.
     */
    private static final String create_topics = "false";
    
    /**
     * Disables de automatic commits.
     */
    private static final String enable_commit = "false";
    
    /**
     * Number of messages polled each time.
     */
    private static final String max_records = "50";
    
    /**
     * Subscribed topic.
     */
    private static final String batch_topic = "BatchTopic";
    
    /**
     * Number of consumers launched.
     */
    private static final int n_consumers = 3;
    
    public static void main(String[] args) throws IOException {
        
        /* Applies the previously defined propreties.  */
        Properties props = new Properties();
        props.put("bootstrap.servers", kafka_server);
        props.put("allow.auto.create.topics", create_topics);
        props.put("enable.auto.commit", enable_commit);
        props.put("max.poll.records", max_records);
        props.put("key.deserializer", 
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "Data.MessageDeserializer");
        props.put("group.id", "0");
        
        /* Generates the graphical interface that presents the received messages.*/
        BatchGUI gui = new BatchGUI();
        gui.startGUI(gui);
        
        /* Creates the file that stores the received messages. */
        FileWriter batchFile = new FileWriter(System.getProperty("user.dir")
                .concat("/src/Data/BATCH.txt"));
        
        /* 
            Creates the region that handles the concurrent
            access to the GUUI and file.  
        */
        SharedRegion sr = new SharedRegion(batchFile, gui);
        
        /* Creates the consumers and lauches the threads. */
        for(int i = 0; i < n_consumers; i++){
            KafkaConsumer<String, Message> consumer = new KafkaConsumer<>(props);
            RebalanceListener rl = new RebalanceListener(consumer);
            
            consumer.subscribe(Arrays.asList(batch_topic), rl);
            
            Consumer c = new Consumer(consumer, sr, rl);
            c.start();
        }
        
    }
}
