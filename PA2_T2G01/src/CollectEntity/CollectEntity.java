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
 * Class responsible for reading the messages from the CAR.txt file and send 
 * them, through kafka, to all the created topics (Batch, Report, Alarm).
 */
public class CollectEntity {
    /**
     * List of host/port pairs to use for establishing the initial connection 
     * to the Kafka cluster.
     */
    private static final String BROKERS_ADDRESSES = "localhost:9092";
    
    /**
     * The batch topic name to where the messages should be sent to.
     */
    private static final String BATCH_TOPIC = "BatchTopic";
    
    /**
     * The report topic name to where the messages should be sent to.
     */
    private static final String REPORT_TOPIC = "ReportTopic";
    
    /**
     * The alarm topic name to where the messages should be sent to.
     */
    private static final String ALARM_TOPIC = "AlarmTopic";
    
    /**
     * Serializer class for key.
     */
    private static final String KEY_SERIALIZER = 
            "org.apache.kafka.common.serialization.StringSerializer";
    
    /**
     * Serializer class for value.
     */
    private static final String VALUE_SERIALIZER = "Data.MessageSerializer";
    
    /**
     * Filename localization to read the messages from.
     */
    private static final String CARS_FILENAME = 
            System.getProperty("user.dir").concat("/src/Data/CAR.txt");
    
    public static void main(String[] args) {
        // Message read from the text file.
        Message msg;
        
        // Components of the message after being split.
        String[] msgArgs;
        
        // List of all the messages in the text file.
        List<String> messages = new ArrayList<>();
        
        /*
        Set of properties to ensure the constraints 
        for the heartbeat type messages.
        */
        Properties heartbeatProps = new Properties();
        heartbeatProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
                BROKERS_ADDRESSES);
        heartbeatProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
                KEY_SERIALIZER);
        heartbeatProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
                VALUE_SERIALIZER);
        
        // Producer will not wait for any acknowledgment from the server at all.
        heartbeatProps.put(ProducerConfig.ACKS_CONFIG, "0");
        
        /*
        Reduce the number of requests sent, adding up to 5ms of latency to 
        records sent in the absence of load.
        */
        heartbeatProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        
        /*
        Producer will attempt to batch records together into fewer requests,
        whenever multiple records are being sent to the same partition.
        */
        heartbeatProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 102400);
        
        /*
        Set of properties to ensure the constraints 
        for the speed type messages.
        */
        Properties speedProps = new Properties();
        speedProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
                BROKERS_ADDRESSES);
        speedProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
                KEY_SERIALIZER);
        speedProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
                VALUE_SERIALIZER);
        
        /*
        Leader will wait for the full set of in-sync replicas to acknowledge 
        the record. This guarantees that the record will not be lost as 
        long as at least one in-sync replica remains alive.
        */
        speedProps.put(ProducerConfig.ACKS_CONFIG, "all");
        
        /*
        Maximum number of unacknowledged requests the client will send.
        Settings set to 1, so there isn't the risk of message reordering due to
        retries.
        */
        speedProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        
        /*
        Producer will ensure that exactly one copy of each message is written 
        in the stream.
        */
        speedProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        /*
        Set of properties to ensure the constraints 
        for the status type messages.
        */
        Properties statusProps = new Properties();
        statusProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
                BROKERS_ADDRESSES);
        statusProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
                KEY_SERIALIZER);
        statusProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
                VALUE_SERIALIZER);
        
        /*
        Leader will wait for the full set of in-sync replicas to acknowledge 
        the record. This guarantees that the record will not be lost as 
        long as at least one in-sync replica remains alive.
        */
        statusProps.put(ProducerConfig.ACKS_CONFIG, "all");
        
        /*
        Maximum number of unacknowledged requests the client will send.
        Settings set to 1, so there isn't the risk of message reordering due to
        retries.
        */
        statusProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,1);
        
        Producer<String, Message> heartbeatProducer = 
                new KafkaProducer<>(heartbeatProps);
        Producer<String, Message> speedProducer = 
                new KafkaProducer<>(speedProps);
        Producer<String, Message> statusProducer = 
                new KafkaProducer<>(statusProps);
        
        CollectEntityGUI gui = new CollectEntityGUI();
        gui.start(gui);
        
        // Try to open the file and save all the messages inside it into a list.
        try (Stream<String> stream = Files.lines(Paths.get(CARS_FILENAME))) {
            messages = stream.collect(Collectors.toList());
        }
        catch (IOException e) {
            System.err.println("ERROR: Unable to open the file!");
            System.exit(-1);
        }
        
        for (String message : messages) {
            // Remove all the whitespaces and splits into message components.
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
            
            // Update the gui text area with the message which was sent.
            gui.updateTextArea(message);
        }
        
        // Close all the 3 producers.
        heartbeatProducer.close();
        speedProducer.close();
        statusProducer.close();
    }
}
