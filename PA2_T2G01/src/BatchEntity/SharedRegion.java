package BatchEntity;

import BatchEntity.GUI.BatchGUI;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class responsible for controlling/synchronizing the access to the file and 
 * gui between the consumer threads.
 */
public class SharedRegion {
    /**
     * File to write the received messages to (BATCH.txt).
     */
    private final FileWriter batchFile;
    
    /**
     * GUI to display the received messages.
     */
    private final BatchGUI gui;
    
    /**
     * Constructor to instantiate the text file and gui.
     * 
     * @param batchFile file to write the received messages to (BATCH.txt).
     * @param gui GUI to display the received messages.
     */
    public SharedRegion(FileWriter batchFile, BatchGUI gui){
        this.batchFile = batchFile;
        this.gui = gui;
    }
    
    /**
     * Method to write the received message in the text file.
     * 
     * @param text received message in the string format.
     */
    public synchronized void writeFile(String text){
        try {
            batchFile.write(text+"\n");
        } catch (IOException ex) {
        }
    }
    
    /**
     * Method to write the received message in the text area of the gui.
     * 
     * @param text received message in the string format.
     */
    public synchronized void writeScreen(String text){
        gui.updateTextArea(text);
    }
}
