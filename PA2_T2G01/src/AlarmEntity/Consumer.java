package AlarmEntity;

import java.time.Duration;
import Data.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


public class Consumer extends Thread{
    
    private final KafkaConsumer<String, Message> consumer;
    private final SharedRegion sr;
    private final RebalanceListener rl;
    
    public Consumer(KafkaConsumer<String, Message> consumer, SharedRegion sr, 
            RebalanceListener rl){
        
        this.consumer = consumer;
        this.sr = sr;
        this.rl = rl;
    }
    
    @Override
    public void run(){
        boolean alarm = false;
        while (true) {
            ConsumerRecords<String, Message> records =
                    consumer.poll(Duration.ofMillis(100));
            
            for (ConsumerRecord<String, Message> record : records){
                Message m = record.value();
                
                if(m.getMessageType()== 1){
                    if(!alarm && m.getSpeed() > 120){
                        alarm = true;
                        sr.writeFile(m.toString() + " ON |");
                        sr.updateAlarm(alarm, m.toString() + " ON |");
                    }
                    if(alarm && m.getSpeed() < 120){
                        alarm = false;
                        sr.updateAlarm(alarm, m.toString() + " OFF |");
                        sr.writeFile(m.toString() + " OFF |");
                    }
                    rl.addOffset(record.topic(), record.partition(), record.offset());
                }
                sr.writeScreen(m.toString());
            }
            consumer.commitSync(rl.getOffsets());
            rl.clearOffsets();
        }
    }
}
