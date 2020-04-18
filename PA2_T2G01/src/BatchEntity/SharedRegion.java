package BatchEntity;

import BatchEntity.GUI.BatchGUI;
import java.io.FileWriter;
import java.io.IOException;


public class SharedRegion {
    
    private final FileWriter batchFile;
    
    private final BatchGUI gui;
    
    public SharedRegion(FileWriter batchFile, BatchGUI gui){
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
}
