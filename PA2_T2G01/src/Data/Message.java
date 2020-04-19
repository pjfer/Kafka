package Data;

/**
 * Class responsible for a message's infrastructure, used in the communication
 * between kafka producers and kafka consumers.
 */
public class Message {
    /**
     * Type of the message (00 -> HEARTBEAT, 01 -> SPEED or 02 -> STATUS).
     */
    private int messageType;
    
    /**
     * Car plate number.
     */
    private String carReg;
    
    /**
     * Integer number which represents when the message was sent from the car.
     */
    private int timestamp;
    
    /**
     * Car velocity registered when the message was sent.
     */
    private int speed;
    
    /**
     * Car status (OK or KO) registered when the message was sent.
     */
    private int carStatus;
    
    /**
     * Default constructor.
     */
    public Message() { }
    
    /**
     * Construct to instantiate the message.
     * 
     * @param messageType type of the message (HEARTBEAT, SPEED or STATUS).
     * @param carReg
     * @param timestamp 
     */
    public Message(int messageType, String carReg, int timestamp) {
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
            return  "| " + carReg + " | " + timestamp + " | 01 | " + 
                    speed + " |";
        }
        if(messageType == 2){
            String status = (carStatus == 0) ? "OK" : "KO";
            return  "| " + carReg + " | " + timestamp + " | 02 | " + 
                    status + " |";
        }
        
        return "Message{" + "message_type=" + messageType + 
                ", car_reg=" + carReg + ", timestamp=" + timestamp + 
                ", speed=" + speed + ", car_status=" + carStatus + '}';
    }
}
