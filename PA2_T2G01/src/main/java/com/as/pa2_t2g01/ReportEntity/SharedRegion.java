package main.java.com.as.pa2_t2g01.ReportEntity;

import java.io.FileWriter;
import java.io.IOException;


public class SharedRegion {
    
    private FileWriter reportFile;
    
    private ReportGUI gui;
    
    public SharedRegion(FileWriter batchFile, ReportGUI gui){
        this.reportFile = batchFile;
        this.gui = gui;
    }
    
    public synchronized void writeFile(String text){
        try {
            reportFile.write(text+"\n");
        } catch (IOException ex) {
        }
    }
    
    public synchronized void writeScreen(String text){
        gui.updateTextArea(text);
    }
}
