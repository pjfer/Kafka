package Data;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Class to serialize the messages to be sent to a topic by a producer.
 */
public class MessageSerializer implements Serializer<Message>{

    @Override
    public byte[] serialize(String string, Message m) {
        String encoding = "UTF-8";
        int int_size = 4;
        int reg_size;
        byte[] serialized_reg;
        
        if(m != null){
            try {
                serialized_reg = m.getCarReg().getBytes(encoding);
                reg_size = serialized_reg.length;
                
                // If the message type is HEARTBEAT.
                if(m.getMessageType() == 0){
                    ByteBuffer buf = ByteBuffer.allocate(int_size + int_size + 
                            reg_size + int_size);
                    buf.putInt(m.getMessageType());
                    buf.putInt(reg_size);
                    buf.put(serialized_reg);
                    buf.putInt(m.getTimestamp());
                    return buf.array();
                }
                // If the message type is SPEED.
                else if(m.getMessageType() == 1){
                    ByteBuffer buf = ByteBuffer.allocate(int_size + int_size + 
                            reg_size + int_size + int_size);
                    buf.putInt(m.getMessageType());
                    buf.putInt(reg_size);
                    buf.put(serialized_reg);
                    buf.putInt(m.getTimestamp());
                    buf.putInt(m.getSpeed());
                    return buf.array();
                }
                
                // If the message type is STATUS.
                ByteBuffer buf = ByteBuffer.allocate(int_size + int_size + 
                        reg_size + int_size + int_size);
                buf.putInt(m.getMessageType());
                buf.putInt(reg_size);
                buf.put(serialized_reg);
                buf.putInt(m.getTimestamp());
                buf.putInt(m.getCarStatus());
                return buf.array();
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MessageSerializer.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
}
