package AlarmEntity;

import AlarmEntity.GUI.AlarmGUI;
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
    private final FileWriter batchFile;

    /**
     * GUI onde escrevemos as mensagens e os alarmes.
     */
    private final AlarmGUI gui;
    
    /**
     * Valor que controla se o alarme está ativo ou não.
     */
    private boolean alarm;
    
    /**
     * Construtor base.
     * 
     * @param batchFile Ficheiro onde escrevemos os alarmes.
     * @param gui GUI onde escrevemos as mensagens e os alarmes.
     */
    public SharedRegion(FileWriter batchFile, AlarmGUI gui){
        this.batchFile = batchFile;
        this.gui = gui;
        this.alarm = false;
        
    }
    
    /**
     * Escreve o alarme no ficheiro.
     * @param text Alarme a ser escrito no ficheiro.
     */
    public synchronized void writeFile(String text){
        try {
            batchFile.write(text+"\n");
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
    
    /**
     * Escreve o alarme na GUI.
     * 
     * @param alarm Representa se o alarme foi ligado ou não.
     * @param text Alarme a ser escrito na GUI.
     */
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
