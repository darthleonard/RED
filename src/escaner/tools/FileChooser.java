package escaner.tools;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import escaner.services.PreferencesService;

public class FileChooser {
    public boolean Save() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Red datasource", "sc"));
        int response = fc.showSaveDialog(null);
        if (response != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        
        //if(extension is invalid) not save
        if (fc.getSelectedFile().exists()) {
            Mensajes.MensajeAlerta("El archivo ya existe");
            Save();
            return false;
        }

        new PreferencesService().SetDefaultFilePath(fc.getSelectedFile().getAbsolutePath());
        return true;
    }

    public void Open() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Red datasource", "sc"));
        int response = fc.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            if (fc.getSelectedFile().exists()) {
                new PreferencesService().SetDefaultFilePath(fc.getSelectedFile().getAbsolutePath());
                return;
            }
            Mensajes.MensajeAlerta("El archivo no existe, se usara el valor default '" + PreferencesService.FILE_DEFAULT_VALUE + "'");
            Open();
        }
    }
}
