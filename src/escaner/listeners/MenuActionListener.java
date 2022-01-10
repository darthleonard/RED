package escaner.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class MenuActionListener implements ActionListener {
	private MenuActions menuAction;

	public MenuActionListener(MenuActions menuAction) {
		this.menuAction = menuAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (menuAction) {
		case Save:
			// itmGuardarActionPerformed(evt);
			break;
		case LoadArpTable:
			// itmTablaArpActionPerformed(evt);
			break;
		case ClearArpTable:
			// jMenuItem1ActionPerformed(evt);
			break;
		case Ping:
			// itmPingActionPerformed(evt);
			break;
		default:
			break;
		}
		JOptionPane.showMessageDialog(null, e.paramString());
	}

}
