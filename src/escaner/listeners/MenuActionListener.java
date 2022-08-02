package escaner.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import escaner.gui.EscanerFrame;
import escaner.tools.FileChooser;
import escaner.tools.Mensajes;

public class MenuActionListener implements ActionListener {
	EscanerFrame escanerFrame;
	private MenuActions menuAction;

	public MenuActionListener(EscanerFrame escanerFrame, MenuActions menuAction) {
		this.escanerFrame = escanerFrame;
		this.menuAction = menuAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (menuAction) {
			case Save:
				escanerFrame.guardarTabla();
				break;
			case SaveAs:
				if(new FileChooser().Save()) {
					escanerFrame.guardarTabla();
					escanerFrame.loadSavedData();
				}
				break;
			case ChooseFile:
				new FileChooser().Open();
				escanerFrame.loadSavedData();
				break;
			case LoadArpTable:
				escanerFrame.loadArpTable();
				break;
			case ClearArpTable:
				/*Tool t = new Tool();
				try {
					t.LimpiaArp();
					JOptionPane.showMessageDialog(this, "La cache ARP del equipo ha sido limpiada");
				} catch (IOException ex) {
					Logger.getLogger(Escaner.class.getName()).log(Level.SEVERE, null, ex);
				}*/
				Mensajes.MensajeError("Lo siento :(", "No implementado aun :/");
				break;
			case Ping:
				escanerFrame.openPingTool();
				break;
			default:
				Mensajes.MensajeError("Lo siento :(", "No implementado aun :/");
				break;
		}
	}

}
