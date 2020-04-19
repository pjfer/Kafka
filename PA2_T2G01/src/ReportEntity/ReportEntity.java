package ReportEntity;

import java.io.FileWriter;
import java.io.IOException;;
import java.util.Arrays;
import java.util.Properties;
import Data.Message;
import ReportEntity.GUI.ReportGUI;
import org.apache.kafka.clients.consumer.KafkaConsumer;


/**
 * Entidade que representa a Report, gera três consumidores kafka com
 * group.id 0 que subrescrevem o tópico ReportTopic.
 * 
 * @author Rafael Teixeira e Pedro Ferreira
 */
public class ReportEntity {
    
    /**
     * Endereço do broker kafka.
     */
    private static final String kafka_server = "localhost:9092";
    
    /**
     * Proíbe a criação automática de tópicos.
     */
    private static final String create_topics = "false";
    
    /**
     * Proíbe os commits automáticos
     */
    private static final String enable_commit = "false";
    
    /**
     * Número máximo de registos puxados de cada vez.
     */
    private static final String max_records = "50";
    
    /**
     * Tópico a ser subscrito.
     */
    private static final String report_topic = "ReportTopic";
    
    /**
     * Número de consumidores lançados.
     */
    private static final int n_consumers = 3;
    
    public static void main(String[] args) throws IOException {
        
        /* Gera as propriedades definidas anteriormente. */
        Properties props = new Properties();
        props.put("bootstrap.servers", kafka_server);
        props.put("allow.auto.create.topics", create_topics);
        props.put("enable.auto.commit", enable_commit);
        props.put("max.poll.records", max_records);
        props.put("key.deserializer", 
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "Data.MessageDeserializer");
        props.put("group.id", "0");
        
        /* Cria a interface gráfica que apresenta as mensagens recebidas. */
        ReportGUI gui = new ReportGUI();
        gui.startGUI(gui);
        
        /* Cria um ficheiro que guarda as mensagens recebidas. */
        FileWriter reportFile = new FileWriter(System.getProperty("user.dir")
                .concat("/src/Data/REPORT.txt"));
        
        /* 
            Cria a região que trata do acesso concorrente 
            das threads à interface gráfica e ao ficheiro. 
        */
        SharedRegion sr = new SharedRegion(reportFile, gui);
        
        /* Cria os consumidores e lança as threads. */
        for(int i = 0; i < n_consumers; i++){
            KafkaConsumer<String, Message> consumer = new KafkaConsumer<>(props);
            RebalanceListener rl = new RebalanceListener(consumer);
            
            consumer.subscribe(Arrays.asList(report_topic), rl);
            Consumer c = new Consumer(consumer, sr, rl);
            c.start();
        }
    }
}
