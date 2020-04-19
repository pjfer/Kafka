package AlarmEntity;

import KafkaEntities.AlarmSharedRegion;
import KafkaEntities.AlarmConsumer;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import Data.Message;
import AlarmEntity.GUI.AlarmGUI;
import KafkaEntities.RebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Entity that represents the alarms, it generates three kafka consumers
 * with group.id 0 that subscribe the topic AlarmTopic
 * 
 * @author Rafael Teixeira & Pedro Ferreira
 */
public class AlarmEntity {
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
    private static final String alarm_topic = "AlarmTopic";
    
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
        
        /* 
            Generates the graphical interface that presents the received 
            messages and tha generated alarms.
        */
        AlarmGUI gui = new AlarmGUI();
        gui.startGUI(gui);
        
        /* Creates the file that stores the generated alarms. */
        FileWriter alarmFile = new FileWriter(
                System.getProperty("user.dir")
                        .concat("/src/Data/ALARM.txt"));
        
        /* 
            Creates the region that handles the concurrent
            access to the GUUI and file. 
        */
        AlarmSharedRegion sr = new AlarmSharedRegion(alarmFile, gui);
        
        /* Cria os consumidores e lança as threads. */
        for(int i = 0; i < n_consumers; i++){
            KafkaConsumer<String, Message> consumer = new KafkaConsumer<>(props);
            RebalanceListener rl = new RebalanceListener(consumer);
            
            consumer.subscribe(Arrays.asList(alarm_topic), rl);
            AlarmConsumer c = new AlarmConsumer(consumer, sr, rl);
            c.start();
        }
    }
}
