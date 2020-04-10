package com.as.pa2_t2g01.KafkaConstants;

/**
 *
 * 
 */
public interface IKafkaConstants {
    /**
     * 
     */
    static String BROKERS_ADDRESSES = "localhost:9092";
    
    /**
     * 
     */
    static String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    
    /**
     * 
     */
    static String KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    
    /**
     * 
     */
    static String VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    
    /**
     * 
     */
    static String VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    
    /**
     * 
     */
    static String CARS_FILENAME = System.getProperty("user.dir").concat("/src/main/java/com/as/pa2_t2g01/Data/CAR.txt");
    
    /**
     * 
     */
    static String BATCH_FILENAME = System.getProperty("user.dir").concat("/src/main/java/com/as/pa2_t2g01/Data/BATCH.txt");
    
    /**
     * 
     */
    static String REPORT_FILENAME = System.getProperty("user.dir").concat("/src/main/java/com/as/pa2_t2g01/Data/REPORT.txt");
    
    /**
     * 
     */
    static String ALARM_FILENAME = System.getProperty("user.dir").concat("/src/main/java/com/as/pa2_t2g01/Data/ALARM.txt");
    
    /**
     * 
     */
    static String BATCH_TOPIC = "BatchTopic";
    
    /**
     * 
     */
    static String BATCH_TOPIC_GROUP = "BatchTopicGroup";
    
    /**
     * 
     */
    static String REPORT_TOPIC = "ReportTopic";
    
    /**
     * 
     */
    static String REPORT_TOPIC_GROUP = "ReportTopicGroup";
    
    /**
     * 
     */
    static String ALARM_TOPIC = "AlarmTopic";
    
    /**
     * 
     */
    static String ALARM_TOPIC_GROUP = "AlarmTopicGroup";
}
