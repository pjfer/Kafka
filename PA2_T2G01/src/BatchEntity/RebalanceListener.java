package BatchEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

/**
 * Entidade responsável por guardar os offset a serem submetidos e em caso de
 * rebalanceamento submetê-los.
 * 
 * @author Rafael Teixeira e Pedro Ferreira
 */
public class RebalanceListener implements ConsumerRebalanceListener{
    
    /**
     * Consumidor kafka ao qual os offsets pertencem.
     */
    private final KafkaConsumer consumer;
    
    /**
     * Offsets a serem submetidos
     */
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    
    /**
     * Construtor base
     * 
     * @param consumer Consumidor a quem o RebalanceListener pertence.
     */
    public RebalanceListener(KafkaConsumer consumer){
        this.consumer = consumer;
    }
    
    /**
     * Adicionar um offset aos offsets a serem submetidos.
     * 
     * @param topic Tópico ao qual o offset pertence.
     * @param partition Partição ao qual o offset pertence.
     * @param offset  Offset a ser submetido.
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
    /**
     * Método executado em caso de rebalanceamento de partições.
     */
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
