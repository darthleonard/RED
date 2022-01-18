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

import java.awt.Container;
import java.awt.Cursor;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import escaner.gui.PingFrame;

public class Ping extends SwingWorker<Void, Void> {
	private PingFrame pingFrame;
	private JProgressBar progressBar;
	private JTextArea textArea;
	private JLabel label;
	private Container container;

	private NetworkInfo networkInfo;

	private int tiempo = 100;

	public Ping(PingArgs args) {
		this.pingFrame = args.pingFrame;
		this.progressBar = args.progressBar;
		this.textArea = args.textArea;
		this.label = args.label;
		this.container = args.container;
	}

	@Override
	protected Void doInBackground() throws Exception {
		InetAddress ping;
		String ip;
		String status;
		int hostsActivos = 0;
		progressBar.setMaximum(networkInfo.getCantidadHosts());
		textArea.setText("");
		try {
			container.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			int i = 1;

			while (i <= networkInfo.getCantidadHosts() && !isCancelled()) {
				ip = networkInfo.siguienteIp().toString();
				status = "probando ".concat(ip);
				ping = InetAddress.getByName(ip);
				if (ping.isReachable(tiempo)) {
					hostsActivos++;
					status = status.concat("\t responde!");
				} else {
					status = status.concat("\t offline");
				}

				progressBar.setValue(i);
				textArea.append(status.concat("\n"));
				textArea.setCaretPosition(textArea.getDocument().getLength());
				label.setText(hostsActivos + " hosts activos de " + i);
				i++;
			}

			textArea.append("El proceso ha finalizado.");
			textArea.setCaretPosition(textArea.getDocument().getLength());
		} catch (IOException ex) {
			System.err.println(ex);
			Mensajes.MensajeError(ex.getMessage());
		}
		container.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		return null;
	}

	@Override
	protected void done() {
		pingFrame.switchControles();
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	public void setNetinfo(NetworkInfo netinfo) {
		this.networkInfo = netinfo;
	}

}
