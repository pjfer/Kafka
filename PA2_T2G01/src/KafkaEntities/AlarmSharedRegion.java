package KafkaEntities;

import AlarmEntity.GUI.AlarmGUI;
import ReportEntity.GUI.ReportGUI;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Controls the concurrent access to the file and the GUI.
 * 
 * @author Rafael Teixeira e Pedro Ferreira
 */
public class AlarmSharedRegion extends SharedRegion{
    
    /**
     * GUI where we write the alarms and messages.
     */
    private final AlarmGUI gui;
    
    /**
     * Value that controls the state of the alarm.
     */
    private final Map<String, Boolean> alarms;
    
    /**
     * Base Constructor.
     * 
     * @param file File where the alarms are writen.
     * @param gui GUI where we write the alarms and messages.
     */
    public AlarmSharedRegion(FileWriter file, AlarmGUI gui){
        super(file, new ReportGUI());
        this.gui = gui;
        this.alarms = new HashMap<>();
        
    }
    
    /**
     * Writes message in the gui.
     * 
     * @param text Message to be written.
     */
    @Override
    public synchronized void writeScreen(String text){
        gui.updateTextArea(text);
    }  
    
    /**
     * Writes alarm in the gui.
     * 
     * @param text Message to be written.
     */
    public synchronized void updateAlarm(String text){
        gui.changeAlarm(text);
    } 
    
    public synchronized  boolean isAlarm(String id) {
        if(alarms.containsKey(id)){
            return alarms.get(id);
        }
        alarms.put(id, Boolean.FALSE);
        return false;
    }

    public synchronized void setAlarm(String id, boolean alarm) {
        alarms.put(id, alarm);
    }
    
    
}
