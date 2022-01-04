package escaner;

import javax.swing.JOptionPane;

public class Ayuda {
	public static void MuestraAyuda() {
//		JOptionPane.showMessageDialog(null,
//				"Pasos:\n"
//				+ "1) Carga la tabla arp.\n"
//				+ "   1.a) Si no hay resultados, ejecuta la herramienta Ping.\n"
//				+ "2) Asigna una descripcion/nombre a los registros.\n"
//				+ "3) Guarda la informacion.\n"
//				+ "   3.a) Si se ejecuta en diferentes redes, debes respaldar\n"
//				+ "        el archivo directorio.sc, de otra forma los datos\n"
//				+ "        se perderan.\n"
//				+ "   3.b) Asegurate que el nombre del archivo(directorio.sc) se\n"
//				+ "        encuentre correctamente escrito.\n"
//				+ "   3.c) El archivo directorio.sc guarda los datos en texto\n"
//				+ "        plano.",
//				"Como usar este software",
//				JOptionPane.PLAIN_MESSAGE);
		JOptionPane.showMessageDialog(null,
				"<html>" +
				"<strong>Pasos</strong><br>" +
				"<ol>" + 
				"<li>Carga la tabla arp.</li>" +
				"<ul>" + 
				"<li>Si no hay resultados, ejecuta la herramienta Ping.</li>" +
				"</ul>" + 
				"<li>Asigna una descripcion/nombre a los registros.</li>" + 
				"<li>Guarda la informacion.</li>" +
				"<ul>" + 
				"<li>Si se ejecuta en diferentes redes, debes respaldar el <br> archivo directorio.sc, de otra forma los datos <br>se perderan.</li>" + 
				"<li>Asegurate que el nombre del archivo(directorio.sc) se <br> encuentre correctamente escrito.</li>" + 
				"<li>El archivo directorio.sc guarda los datos en texto plano.</li>" +
				"</ul>" + 
				"</ol>"
				+ "</html>",
				"Como usar este software",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	public static void MuestraGuardar() {
		JOptionPane.showMessageDialog(null,
				"Guarda un archivo de texto llamado directorio.sc\n"
				+ "con los datos recopilados.",
				"Guardar",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void MuestraCargarTablaARP() {
		JOptionPane.showMessageDialog(null,
				"Consulta la tabla ARP del equipo.\n"
				+ "Ejecutar la herramienta Ping actualizara\n"
				+ "la tabla y se obtendran datos actualizados.",
				"Cargar tabla ARP",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void MuestraPing() {
		JOptionPane.showMessageDialog(null,
				"Realiza un barrido ping sobre la red.\n"
				+ "Esto permite encontrar los dispositivos\n"
				+ "conectados a la red.\n\n"
				+ "Aumentar el tiempo(en milisegundos) de espera\n"
				+ "puede ayudar a encontrar mas dispositivos,\n"
				+ "pero tardara mas en terminar de buscar.",
				"Ping",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void MuestraSignificadoColores () {
		JOptionPane.showMessageDialog(null,
				"<html>"
				+ "<font color=\"#e5e2dd\">Blanco: Dispositivo identificado</font><br>"
				+ "<font color=\"#798576\">Gris: Dispositivo identificado pero no esta en linea</font><br>"
				+ "<font color=\"#3b83bd\">Azul: El dispositivo cambio de direccion IP</font><br>"
				+ "<font color=\"#e5be01\">Amarillo: El dispositivo no ha sido identificado</font><br>"
				+ "<font color=\"#cb3234\">Rojo: Dispositivo nuevo</font>"
				+ "</html>",
				"Significado de los colores de la tabla",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
