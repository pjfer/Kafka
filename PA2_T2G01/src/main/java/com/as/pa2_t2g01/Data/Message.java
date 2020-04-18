 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.as.pa2_t2g01.Data;

/**
 *
 *
 */
public class Message {
    
    private int message_type;
    private String car_reg;
    private int timestamp;
    private int speed;
    private int car_status;

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getCar_reg() {
        return car_reg;
    }

    public void setCar_reg(String car_reg) {
        this.car_reg = car_reg;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCar_status() {
        return car_status;
    }

    public void setCar_status(int car_status) {
        this.car_status = car_status;
    }

    @Override
    public String toString() {
        if(message_type == 0){
            return  "| " + car_reg + " | " + timestamp + " | 00 | HeartBit"; 
        }
        if(message_type == 1){
            return  "| " + car_reg + " | " + timestamp + " | 01 | " + speed;
        }
        if(message_type == 2){
            String status = (car_status == 0) ? "OK" : "KO";
            return  "| " + car_reg + " | " + timestamp + " | 02 | " + status;
        }
        
        return "Message{" + "message_type=" + message_type + ", car_reg=" 
                + car_reg + ", timestamp=" + timestamp + ", speed=" + speed 
                + ", car_status=" + car_status + '}';
    }    
}
