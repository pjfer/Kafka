package AlarmEntity;

import AlarmEntity.GUI.AlarmGUI;
import java.io.FileWriter;
import java.io.IOException;


public class SharedRegion {
    
    private final FileWriter batchFile;
    
    private final AlarmGUI gui;
    
    public SharedRegion(FileWriter batchFile, AlarmGUI gui){
        this.batchFile = batchFile;
        this.gui = gui;
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
}
