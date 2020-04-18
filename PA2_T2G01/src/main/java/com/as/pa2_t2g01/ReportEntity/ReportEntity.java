package main.java.com.as.pa2_t2g01.ReportEntity;

import java.io.FileWriter;
import java.io.IOException;;
import java.util.Arrays;
import java.util.Properties;
import main.java.com.as.pa2_t2g01.Data.Message;
import org.apache.kafka.clients.consumer.KafkaConsumer;


/**
 *
 * 
 */
public class ReportEntity {
    private static final String kafka_server = "localhost:9092";
    private static final String create_topics = "false";
    private static final String enable_commit = "false";
    private static final String max_records = "50";
    private static final String report_topic = "ReportTopic";
    private static final int n_consumers = 3;
    
    public static void main(String[] args) throws IOException {
        
        Properties props = new Properties();
        props.put("bootstrap.servers", kafka_server);
        props.put("allow.auto.create.topics", create_topics);
        props.put("enable.auto.commit", enable_commit);
        props.put("max.poll.records", max_records);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "main.java.com.as.pa2_t2g01.Data.MessageDeserializer");
        props.put("group.id", "0");
        
        ReportGUI gui = new ReportGUI();
        gui.startGUI(gui);
        
        FileWriter reportFile = new FileWriter("REPORT.TXT");
        
        SharedRegion sr = new SharedRegion(reportFile, gui);
        
        for(int i = 0; i < n_consumers; i++){
            KafkaConsumer<String, Message> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList(report_topic));
            Consumer c = new Consumer(consumer, sr);
            c.start();
        }
        /*
        while (true) {
            ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
            
            for (ConsumerRecord<String, Message> record : records){
                Message m = record.value();
                
                if(m.getMessage_type()== 1){
                    consumer.commitSync(
                            Collections.singletonMap(
                                    new TopicPartition(record.topic(), record.partition()), 
                                    new OffsetAndMetadata(record.offset() + 1)));
                }
                
                reportFile.write(m.toString()+"\n");
                gui.updateTextArea(m.toString());
            }
            
            consumer.commitSync();
        }*/
    }
}
