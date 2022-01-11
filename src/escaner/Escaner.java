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
package escaner;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import escaner.gui.EscanerFrame;
import escaner.tools.Mensajes;

/**
 *
 * @author darthleonard
 */
public class Escaner {
	public static void main(String args[]) {
		javax.swing.UIManager.put("ToolTip.background", new javax.swing.plaf.ColorUIResource(163, 245, 82));
		Border border = BorderFactory.createLineBorder(new Color(78, 154, 6));
		javax.swing.UIManager.put("ToolTip.border", border);
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
			new EscanerFrame().setVisible(true);
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