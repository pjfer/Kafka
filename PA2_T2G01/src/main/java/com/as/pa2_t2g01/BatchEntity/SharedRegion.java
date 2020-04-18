package main.java.com.as.pa2_t2g01.BatchEntity;

import java.io.FileWriter;
import java.io.IOException;


public class SharedRegion {
    
    private final FileWriter batchFile;
    
    private final ReportGUI gui;
    
    public SharedRegion(FileWriter batchFile, ReportGUI gui){
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
