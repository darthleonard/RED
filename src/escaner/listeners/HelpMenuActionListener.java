package escaner.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import escaner.Ayuda;
import escaner.tools.Mensajes;

public class HelpMenuActionListener implements ActionListener {
	private MenuActions menuAction;

	public HelpMenuActionListener(MenuActions menuAction) {
		this.menuAction = menuAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (menuAction) {
		case Help:
			Ayuda.MuestraAyuda();
			break;
		case Save:
			Ayuda.MuestraGuardar();
			break;
		case LoadArpTable:
			Ayuda.MuestraCargarTablaARP();
			break;
		case Ping:
			Ayuda.MuestraPing();
			break;
		case Colors:
			Ayuda.MuestraSignificadoColores();
			break;
		case About:
			Mensajes.MensajeInformacion("Acerca de este software", "leolinuxmx@gmail.com");
			break;
		default:
			break;
		}
	}

}
