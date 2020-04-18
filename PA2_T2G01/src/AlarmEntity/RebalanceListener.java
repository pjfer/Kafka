package AlarmEntity;

import ReportEntity.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;


public class RebalanceListener implements ConsumerRebalanceListener{
    
    private final KafkaConsumer consumer;
    
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    
    public RebalanceListener(KafkaConsumer consumer){
        this.consumer = consumer;
        
    }
    
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
    public void onPartitionsRevoked(Collection<TopicPartition> clctn) {
        consumer.commitSync(currentOffsets);
        currentOffsets.clear();
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> clctn) {
    }
    
}
