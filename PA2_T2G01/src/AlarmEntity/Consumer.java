package AlarmEntity;

import java.time.Duration;
import Data.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


/**
 * Thread que corre um consumidor kafka.
 * 
 * @author Rafael Teixeira e Pedro Ferreira.
 */
public class Consumer extends Thread{
    
    /**
     * Consumidor Kafka usado para obter as mensagens.
     */
    private final KafkaConsumer<String, Message> consumer;
    
    /**
     * Região partilhada que controla o acesso à interface gráfica e ao ficheiro.
     */
    private final SharedRegion sr;
    
    /**
     * Entidade responsável por fazer commits sincronos em caso de 
     * rebalanceamento de partições e também de guardar os offsets para commit.
     */
    private final RebalanceListener rl;
    
    /**
     * Construtor base de um consumidor
     * @param consumer Consumidor Kafka usado para obter as mensagens.
     * @param sr Região partilhada que controla o acesso à interface gráfica e ao ficheiro.
     * @param rl Rebalance Listener usado pelo consumidor passado por argumento.
     */
    public Consumer(KafkaConsumer<String, Message> consumer, SharedRegion sr, 
            RebalanceListener rl){
        this.consumer = consumer;
        this.sr = sr;
        this.rl = rl;
    }
    
    @Override
    /**
     * Ciclo de vida o consumidor, obtém mensagens, processa e volta a pedir.
     */
    public void run(){
        while (true) {
            /* Obtém as mensagens. */
            ConsumerRecords<String, Message> records =
                    consumer.poll(Duration.ofMillis(100));
            
            /* Processa todas as mensagens. */
            for (ConsumerRecord<String, Message> record : records){
                Message m = record.value();
                /* Verifica se a mensagem é do tipo speed. */
                if(m.getMessageType()== 1){
                    /* 
                        Verifica se ativa ou desativa o alarme e em caso 
                        positivo escreve a mensagem no ficheiro e na gui.
                    */
                    if(!sr.isAlarm() && m.getSpeed() > 120){
                        sr.setAlarm(true);
                        sr.writeFile(m.toString() + " ON |");
                        sr.updateAlarm(sr.isAlarm(), m.toString() + " ON |");
                    }
                    if(sr.isAlarm() && m.getSpeed() < 120){
                        sr.setAlarm(false);
                        sr.updateAlarm(sr.isAlarm(), m.toString() + " OFF |");
                        sr.writeFile(m.toString() + " OFF |");
                    }
                    /* Adiciona o offset da mensagem speed ao listener */
                    rl.addOffset(record.topic(), record.partition(), 
                            record.offset());
                }
                /* Escreve no noutra secção as mensagens que processa. */
                sr.writeScreen(m.toString());
            }
            /* Após processar todas as mensagens recebidas faz o commit. */
            consumer.commitSync(rl.getOffsets());
            /* Limpa os offsets submetidos. */
            rl.clearOffsets();
        }
    }
}
