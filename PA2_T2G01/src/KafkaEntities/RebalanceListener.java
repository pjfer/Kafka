package KafkaEntities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

/**
 * Entity responsible for storing the offsets to be submited and in case of
 * rebalencing submit them.
 * 
 * @author Rafael Teixeira e Pedro Ferreira
 */
public class RebalanceListener implements ConsumerRebalanceListener{
    
    /**
     * Kafka consumer that owns the stored offsets.
     */
    private final KafkaConsumer consumer;
    
    /**
     * Offsets to be submited
     */
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    
    /**
     * Base constructor.
     * 
     * @param consumer Kafka consumer that owns the stored offsets.
     */
    public RebalanceListener(KafkaConsumer consumer){
        this.consumer = consumer;
    }
    
    /**
     * Add an offset to be submited.
     * 
     * @param topic Topic of the offset.
     * @param partition Partition of the offset
     * @param offset  Offset to be submited.
     */
    public void addOffset(String topic, int partition, long offset){
        currentOffsets.put(new TopicPartition(topic, partition), 
                new OffsetAndMetadata(offset));
    }
    
    public Map<TopicPartition, OffsetAndMetadata> getOffsets(){
        return currentOffsets;
    }
    
    public void clearOffsets(){
        currentOffsets.clear();
    }
    
    @Override
    /**
     * Method executed in case of rebalancing.
     */
    public void onPartitionsRevoked(Collection<TopicPartition> clctn) {
        consumer.commitSync(currentOffsets);
        currentOffsets.clear();
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> clctn) {
    }
    
}
