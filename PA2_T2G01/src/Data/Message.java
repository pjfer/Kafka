package Data;

/**
 *
 *
 */
public class Message {
    
    private int messageType;
    private String carReg;
    private int timestamp;
    private int speed;
    private int carStatus;
    
    public Message() { }
    
    public Message(int messageType, String carReg, int timestamp)
    {
        this.messageType = messageType;
        this.carReg = carReg;
        this.timestamp = timestamp;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int message_type) {
        this.messageType = message_type;
    }

    public String getCarReg() {
        return carReg;
    }

    public void setCarReg(String carReg) {
        this.carReg = carReg;
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

    public int getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(int carStatus) {
        this.carStatus = carStatus;
    }

    @Override
    public String toString() {
        if(messageType == 0){
            return  "| " + carReg + " | " + timestamp + " | 00 | HeartBeat |"; 
        }
        if(messageType == 1){
            return  "| " + carReg + " | " + timestamp + " | 01 | " + speed + " |";
        }
        if(messageType == 2){
            String status = (carStatus == 0) ? "OK" : "KO";
            return  "| " + carReg + " | " + timestamp + " | 02 | " + status + " |";
        }
        
        return "Message{" + "message_type=" + messageType + ", car_reg=" + carReg + ", timestamp=" + timestamp + ", speed=" + speed + ", car_status=" + carStatus + '}';
    }
}
