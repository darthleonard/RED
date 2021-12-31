/*
 * Copyright (C) 2017 Leonardo Gonzalez Caracosa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package escaner.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Archivos {
    String nombre = "directorio.sc";
    
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
}
