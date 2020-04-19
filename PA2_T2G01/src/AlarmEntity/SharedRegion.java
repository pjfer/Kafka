package AlarmEntity;

import AlarmEntity.GUI.AlarmGUI;
import java.io.FileWriter;
import java.io.IOException;


public class SharedRegion {
    
    private final FileWriter batchFile;
    
    private final AlarmGUI gui;
    
    private boolean alarm;
    
    public SharedRegion(FileWriter batchFile, AlarmGUI gui){
        this.batchFile = batchFile;
        this.gui = gui;
        this.alarm = false;
        
    }
    
    public synchronized void writeFile(String text){
        try {
            batchFile.write(text+"\n");
        } catch (IOException ex) {
        }
    }
    
    public synchronized void writeScreen(String text){
        gui.updateTextArea(text);
    }
    
    public synchronized void updateAlarm(boolean alarm, String text){
        gui.changeAlarm(alarm, text);
    }

    public synchronized  boolean isAlarm() {
        return alarm;
    }

    public synchronized void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
    
    
}
