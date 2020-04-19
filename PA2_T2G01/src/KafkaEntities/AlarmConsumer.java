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
public class AlarmConsumer extends Thread{
    
    /**
     * Kafka consumer used to obtain the messages.
     */
    private final KafkaConsumer<String, Message> consumer;
    
    /**
     * Shared region that handles the access to the gui and the file.
     */
    private final AlarmSharedRegion sr;
    
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
    public AlarmConsumer(KafkaConsumer<String, Message> consumer, AlarmSharedRegion sr, 
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
                    /* 
                        Verifies if it turns on or of the allarm and 
                        writes it on the GUI and file if true.
                    */
                    if(!sr.isAlarm(m.getCarReg()) && m.getSpeed() > 120){
                        sr.setAlarm(m.getCarReg(), true);
                        sr.writeFile(m.toString() + " ON |");
                        sr.updateAlarm(m.toString() + " ON |");
                    }
                    if(sr.isAlarm(m.getCarReg()) && m.getSpeed() < 120){
                        sr.setAlarm(m.getCarReg(), false);
                        
                        sr.updateAlarm(m.toString() + " OFF |");
                        sr.writeFile(m.toString() + " OFF |");
                    }
                    /* Adds the offset of the speed message. */
                    rl.addOffset(record.topic(), record.partition(), 
                            record.offset());
                }
                /* Writes every message on another GUI section. */
                sr.writeScreen(m.toString());
            }
            /* After processing a batch, commits the offsets. */
            consumer.commitSync(rl.getOffsets());
            /* Cleans the pushed offsets. */
            rl.clearOffsets();
        }
    }
}
