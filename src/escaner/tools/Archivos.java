package escaner.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import escaner.services.PreferencesService;

public class Archivos {

    public ArrayList<String[]> Leer() throws FileNotFoundException {
        ArrayList<String[]> list = new ArrayList<String[]>();
        String nombre = getGileNameFromPreferences();
        if (ExisteArchivo()) {
            Scanner sc = new Scanner(new FileReader(nombre));
            while (sc.hasNext()) {
                list.add(new String[] { sc.nextLine(), sc.nextLine(), sc.nextLine() });
            }
            sc.close();
        }
        return list;
    }

    public void Guardar(ArrayList<String[]> datos) throws FileNotFoundException {
        String nombre = getGileNameFromPreferences();
        PrintWriter pw = new PrintWriter(nombre);
        for (int i = 0; i < datos.size(); i++) {
            pw.println(datos.get(i)[0]);
            pw.println(datos.get(i)[1]);
            pw.println(datos.get(i)[2]);
        }
        pw.close();
    }

    public boolean ExisteArchivo() {
        String nombre = getGileNameFromPreferences();
        File file = new File(nombre);
        return file.exists();
    }

    public String GetFileName() {
        String nombre = getGileNameFromPreferences();
        File file = new File(nombre);
        return file.exists() ? file.getName() : nombre;
    }

    private String getGileNameFromPreferences() {
        return new PreferencesService().GetDefultFilePath();
    }
}
