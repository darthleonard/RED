package escaner;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import escaner.gui.EscanerFrame;
import escaner.tools.Mensajes;

public class Escaner {
	public static void main(String args[]) {
		javax.swing.ToolTipManager.sharedInstance().setDismissDelay(10000);

		try {
			java.util.Properties props = new java.util.Properties();
			props.put("logoString", "darthleonard");
			com.jtattoo.plaf.hifi.HiFiLookAndFeel.setCurrentTheme(props);
			javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			Mensajes.MensajeError("Error en LookAndFeel", "No se encontro la libreria de L&F en el equipo");
			defaultLAF();
		}

		java.awt.EventQueue.invokeLater(() -> {
			try {
				new Initializer().Init();
				new EscanerFrame().setVisible(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private static void defaultLAF() {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			Logger.getLogger(Escaner.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}