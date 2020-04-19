package BatchEntity;

import java.time.Duration;
import Data.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Class responsible for processing the records to messages and write them
 * on the batch gui and the BATCH.txt file.
 */
public class Consumer extends Thread{
    /**
     * Kafka consumer to retrieve the records and commit offsets.
     */
    private final KafkaConsumer<String, Message> consumer;
    
    /**
     * Region to control/synchronize the access of the file and gui between the
     * threads.
     */
    private final SharedRegion sr;
    
    /**
     * Listener to trigger custom actions when the set of partitions assigned 
     * to the consumer changes.
     */
    private RebalanceListener rl;
    
    /**
     * Constructor to instantiate the kafka consumer, shared region and 
     * rebalance listener.
     * 
     * @param consumer kafka consumer to retrieve the records and commit offsets.
     * @param sr region to control/synchronize the access of the file and gui 
     * between the threads.
     * @param rl Listener to trigger custom actions when the set of partitions 
     * assigned to the consumer changes.
     */
    public Consumer(KafkaConsumer<String, Message> consumer, SharedRegion sr, 
            RebalanceListener rl)
    {
        this.consumer = consumer;
        this.sr = sr;
        this.rl = rl;
    }
    
    @Override
    public void run(){
        while (true) {
            ConsumerRecords<String, Message> records = 
                    consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, Message> record : records){
                Message m = record.value();

                if(m.getMessageType()== 1){
                    rl.addOffset(record.topic(), record.partition(), 
                            record.offset());
                }

                sr.writeFile(m.toString());
                sr.writeScreen(m.toString());
            }
            consumer.commitSync(rl.getOffsets());
            rl.clearOffsets();
        }
    }
}
