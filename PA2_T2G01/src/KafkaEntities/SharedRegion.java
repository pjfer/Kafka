package KafkaEntities;

import BatchEntity.GUI.BatchGUI;
import ReportEntity.GUI.ReportGUI;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Controls the concurrent access to the file and the GUI.
 * 
 * @author Rafael Teixeira e Pedro Ferreira
 */
public class SharedRegion {
    
    /**
     * File where the messages are writen.
     */
    private final FileWriter file;
    
    /**
     * GUI where we write the messages.
     */
    private BatchGUI batchGui;
    
    /**
     * GUI where we write the messages.
     */
    private ReportGUI reportGui;
    
    /**
     * Base Constructor.
     * 
     * @param batchFile File where the alarms are writen.
     * @param gui GUI where we write the alarms and messages.
     */
    public SharedRegion(FileWriter batchFile, BatchGUI gui){
        this.file = batchFile;
        this.batchGui = gui;
    }
    
    /**
     * Base Constructor.
     * 
     * @param reportFile File where the alarms are writen.
     * @param gui GUI where we write the alarms and messages.
     */
    public SharedRegion(FileWriter reportFile, ReportGUI gui){
        this.file = reportFile;
        this.reportGui = gui;
    }
    
    
    /**
     * Writes the message on the file.
     * @param text mesage to be written.
     */
    public synchronized void writeFile(String text){
        try {
            file.write(text+"\n");
        } catch (IOException ex) {
        }
    }
    
    /**
     * Writes message in the gui.
     * 
     * @param text Message to be written.
     */
    public synchronized void writeScreen(String text){
        if(reportGui != null){
            reportGui.updateTextArea(text);
        }else{
            batchGui.updateTextArea(text);
        }
    }
}
