/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.as.pa2_t2g01.Data;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.common.serialization.Deserializer;

/**
 *
 * 
 */
public class MessageDeserializer implements Deserializer<Message>{

    @Override
    public Message deserialize(String string, byte[] bytes) {
        String encoding = "UTF-8";
        
        Message m = new Message();
        if (bytes != null){
            try {
                ByteBuffer buf = ByteBuffer.wrap(bytes);
                m.setMessage_type(buf.getInt());

                int reg_size = buf.getInt();
                byte[] reg = new byte[reg_size];
                buf.get(reg);
                m.setCar_reg(new String(reg, encoding));
                
                m.setTimestamp(buf.getInt());
                if(m.getMessage_type() == 1){
                     m.setSpeed(buf.getInt());
                }
                if(m.getMessage_type() == 2){
                    m.setCar_status(buf.getInt());
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MessageDeserializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        return m;
    }
    
}
