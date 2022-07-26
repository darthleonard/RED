package escaner.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import escaner.services.PreferencesService;

public class Archivos {
    private String nombre;
    
    public Archivos() {
        nombre = new PreferencesService().GetDefultFilePath();
    }
    
    public ArrayList<String[]> Leer() throws FileNotFoundException {
        ArrayList<String[]> list = new ArrayList<String[]>();
        if(ExisteArchivo()) {
            Scanner sc = new Scanner(new FileReader(nombre));
            while (sc.hasNext()) {
                list.add(new String[]{sc.nextLine(), sc.nextLine(), sc.nextLine()});
            }
            sc.close();
        }
        return list;
    }
    
    public void Guardar(ArrayList<String[]> datos) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(nombre);
        for (int i = 0; i < datos.size(); i++) {
            pw.println(datos.get(i)[0]);
            pw.println(datos.get(i)[1]);
            pw.println(datos.get(i)[2]);
        }
        pw.close();
    }
    
    public boolean ExisteArchivo() {
        File file = new File(nombre);
        return file.exists();
    }

    public String GetFileName() {
        File file = new File(nombre);
        return file.exists() ? file.getName() : nombre;
    }
}
