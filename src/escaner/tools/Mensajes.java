package escaner.tools;

import javax.swing.JOptionPane;

public class Mensajes {
    public static void MensajeInformacion(String msj) {
        MensajeInformacion("INFORMACION", msj);
    }
    
    public static void MensajeInformacion(String title, String msj) {
        JOptionPane.showMessageDialog(null, msj, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void MensajeAlerta(String msj) {
        MensajeAlerta("ALERTA", msj);
    }
    
    public static void MensajeAlerta(String title, String msj) {
        JOptionPane.showMessageDialog(null, msj, title, JOptionPane.WARNING_MESSAGE);
    }
    
    public static void MensajeError(String msj) {
        MensajeError("ERROR", msj);
    }
    public static void MensajeError(String title, String msj) {
        JOptionPane.showMessageDialog(null, msj, title, JOptionPane.ERROR_MESSAGE);
    }
}