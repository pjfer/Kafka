package KafkaEntities;

import java.time.Duration;
import Data.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Thread that runs the kafka consumer.
 * 
 * @author Rafael Teixeira & Pedro Ferreira.
 */
public class Consumer extends Thread{
    
    /**
     * Kafka consumer used to obtain the messages.
     */
    private final KafkaConsumer<String, Message> consumer;
    
    /**
     * Shared region that handles the access to the gui and the file.
     */
    private final SharedRegion sr;
    
    /**
     * Entity to handle the offsets and commits in case of rebalansing.
     */
    private final RebalanceListener rl;
    
    /**
     * Base Constructor of a consumer.
     * 
     * @param consumer Kafka consumer used to obtain the messages.
     * @param sr Shared region that handles the access to the gui and the file.
     * @param rl Entity to handle the offsets and commits in case of rebalansing.
     */
    public Consumer(KafkaConsumer<String, Message> consumer, SharedRegion sr,
            RebalanceListener rl){
        
        this.consumer = consumer;
        this.sr = sr;
        this.rl = rl;
    }
    
    /**
     * Thread life cycle.
     */
    @Override
    public void run(){
        
        while (true) {
            /* Obtains Messages. */
            ConsumerRecords<String, Message> records = 
                    consumer.poll(Duration.ofMillis(100));
            
            /* Processes the messages. */
            for (ConsumerRecord<String, Message> record : records){
                Message m = record.value();
                
                /* Checks if message of type speed. */
                if(m.getMessageType()== 1){
                    /* Adds the offset of the speed message. */
                    rl.addOffset(record.topic(), record.partition(), record.offset());
                }
                /* Writes every message on the GUI and in the file section. */
                sr.writeFile(m.toString());
                sr.writeScreen(m.toString());
            }
            /* After processing a batch, commits the offsets. */
            consumer.commitSync(rl.getOffsets());
            /* Cleans the pushed offsets. */
            rl.clearOffsets();
        }
    }
}
