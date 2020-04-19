package ReportEntity;

import ReportEntity.GUI.ReportGUI;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe que controla o acesso concorrente ao ficheiro e à interface gráfica.
 * 
 * @author Rafael Teixeira e Pedro Ferreira
 */
public class SharedRegion {
    
    /**
     * Ficheiro onde escrevemos os alarmes.
     */
    private FileWriter reportFile;
    
    /**
     * GUI onde escrevemos as mensagens e os alarmes.
     */
    private ReportGUI gui;
    
    /**
     * Construtor base.
     * 
     * @param batchFile Ficheiro onde escrevemos os alarmes.
     * @param gui GUI onde escrevemos as mensagens e os alarmes.
     */
    public SharedRegion(FileWriter batchFile, ReportGUI gui){
        this.reportFile = batchFile;
        this.gui = gui;
    }
    
    /**
     * Escreve a mensagem no ficheiro.
     * @param text mansagem a ser escrita no ficheiro.
     */
    public synchronized void writeFile(String text){
        try {
            reportFile.write(text+"\n");
        } catch (IOException ex) {
        }
    }
    
    /**
     * Escreve a mensagem na GUI.
     * 
     * @param text Mensagem a ser escrita no GUI.
     */
    public synchronized void writeScreen(String text){
        gui.updateTextArea(text);
    }
}
