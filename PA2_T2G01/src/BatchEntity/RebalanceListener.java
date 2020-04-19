package BatchEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

/**
 * Class to trigger custom actions when the set of partitions assigned 
 * to the consumer changes.
 */
public class RebalanceListener implements ConsumerRebalanceListener{
    /**
     * Kafka consumer to commit synchronously the offsets.
     */
    private final KafkaConsumer consumer;
    
    /**
     * Map of offsets by partition with associated metadata.
     */
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = 
            new HashMap<>();
    
    /**
     * COnstructor to instantiate the kafka consumer.
     * 
     * @param consumer 
     */
    public RebalanceListener(KafkaConsumer consumer){
        this.consumer = consumer;
    }
    
    /**
     * Method to add a new offset and associated metadata from a topic and 
     * partition to the currentOffsets map.
     * 
     * @param topic kafka topic name.
     * @param partition kafka partition number.
     * @param offset offset to be committed.
     */
    public void addOffset(String topic, int partition, long offset){
        currentOffsets.put(new TopicPartition(topic, partition), 
                new OffsetAndMetadata(offset));
    }
    
    public Map<TopicPartition, OffsetAndMetadata> getOffsets(){
        return currentOffsets;
    }
    
    /**
     * Method to remove all the offsets to be committed.
     */
    public void clearOffsets(){
        currentOffsets.clear();
    }
    
    /**
     * Callback method to provide handling of offset commits to a customized 
     * store. 
     * This method will be called during a rebalance operation when the 
     * consumer has to give up some partitions.
     * 
     * @param clctn list of partitions that were assigned to the consumer and 
     * now need to be revoked.
     */
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> clctn) {
        consumer.commitSync(currentOffsets);
        currentOffsets.clear();
    }

    /**
     * Callback method to provide handling of customized offsets on completion 
     * of a successful partition re-assignment. 
     * This method will be called after the partition re-assignment completes 
     * and before the consumer starts fetching data.
     * 
     * @param clctn list of partitions that are now assigned to the consumer.
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> clctn) {
    }
}
