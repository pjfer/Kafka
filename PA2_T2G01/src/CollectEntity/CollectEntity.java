package CollectEntity;

import Data.Message;
import CollectEntity.GUI.CollectEntityGUI;

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
    /**
     * 
     */
    private static final String BROKERS_ADDRESSES = "localhost:9092";
    
    /**
     * 
     */
    private static final String BATCH_TOPIC = "BatchTopic";
    
    /**
     * 
     */
    private static final String REPORT_TOPIC = "ReportTopic";
    
    /**
     * 
     */
    private static final String ALARM_TOPIC = "AlarmTopic";
    
    /**
     * 
     */
    private static final String KEY_SERIALIZER = 
            "org.apache.kafka.common.serialization.StringSerializer";
    
    /**
     * 
     */
    private static final String VALUE_SERIALIZER = "Data.MessageSerializer";
    
    /**
     * 
     */
    private static final String CARS_FILENAME = 
            System.getProperty("user.dir").concat("/src/Data/CAR.txt");
    
    public static void main(String[] args) {
        Message msg;
        String[] msgArgs;
        List<String> messages = new ArrayList<>();
        
        Properties heartbeatProps = new Properties();
        heartbeatProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
                BROKERS_ADDRESSES);
        heartbeatProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
                KEY_SERIALIZER);
        heartbeatProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
                VALUE_SERIALIZER);
        heartbeatProps.put(ProducerConfig.ACKS_CONFIG, "0");
        heartbeatProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 5242880);
        heartbeatProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        heartbeatProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 10485760);
        
        Properties speedProps = new Properties();
        speedProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
                BROKERS_ADDRESSES);
        speedProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
                KEY_SERIALIZER);
        speedProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
                VALUE_SERIALIZER);
        speedProps.put(ProducerConfig.ACKS_CONFIG, "all");
        speedProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        speedProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        Properties statusProps = new Properties();
        statusProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
                BROKERS_ADDRESSES);
        statusProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
                KEY_SERIALIZER);
        statusProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
                VALUE_SERIALIZER);
        statusProps.put(ProducerConfig.ACKS_CONFIG, "all");
        statusProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,1);
        
        Producer<String, Message> heartbeatProducer = 
                new KafkaProducer<>(heartbeatProps);
        Producer<String, Message> speedProducer = 
                new KafkaProducer<>(speedProps);
        Producer<String, Message> statusProducer = 
                new KafkaProducer<>(statusProps);
        
        CollectEntityGUI gui = new CollectEntityGUI();
        gui.start();
        
        try (Stream<String> stream = Files.lines(Paths.get(CARS_FILENAME))) {
            messages = stream.collect(Collectors.toList());
        }
        catch (IOException e) {
            System.err.println("ERROR: Unable to open the file!");
            System.exit(-1);
        }
        
        for (String message : messages) {
            msgArgs = message.replaceAll("\\s", "").split("\\|");
            msg = new Message(Integer.parseInt(msgArgs[3]), msgArgs[1], 
                    Integer.parseInt(msgArgs[2]));
            
            switch (msgArgs[3]) {
                case "01":
                    msg.setSpeed(Integer.parseInt(msgArgs[4]));
                    speedProducer
                            .send(new ProducerRecord<>(BATCH_TOPIC, msg));
                    speedProducer
                            .send(new ProducerRecord<>(REPORT_TOPIC, msg));
                    speedProducer
                            .send(new ProducerRecord<>(ALARM_TOPIC, msg));
                    break;
                case "02":
                    msg.setCarStatus("OK".equals(msgArgs[4]) ? 0 : 1);
                    statusProducer
                            .send(new ProducerRecord<>(BATCH_TOPIC, msg));
                    statusProducer
                            .send(new ProducerRecord<>(REPORT_TOPIC, msg));
                    statusProducer
                            .send(new ProducerRecord<>(ALARM_TOPIC, msg));
                    break;
                default:
                    heartbeatProducer
                            .send(new ProducerRecord<>(BATCH_TOPIC, msg));
                    heartbeatProducer
                            .send(new ProducerRecord<>(REPORT_TOPIC, msg));
                    heartbeatProducer
                            .send(new ProducerRecord<>(ALARM_TOPIC, msg));
                    break;
            }
            
            gui.updateTextArea(message);
        }
        
        heartbeatProducer.close();
        speedProducer.close();
        statusProducer.close();
    }
}
