package escaner.events;

import javax.swing.JComboBox;

import escaner.PingFrame;
import escaner.tools.Adaptador;
import escaner.tools.Mensajes;
import escaner.tools.NetworkInfo;

// Class unused, kept as example of IEscanerEvent
public class PingEvent implements IEscanerEvent {
	private JComboBox<Adaptador> cmbInterfaces;
	
	public PingEvent(JComboBox<Adaptador> cmbInterfaces) {
		this.cmbInterfaces = cmbInterfaces;
	}

	@Override
	public void Execute() {
		if(cmbInterfaces.getSelectedObjects().length <= 0) {
			Mensajes.MensajeError("ERROR", "No se ha seleccionado ningun adaptador de red");
			return;
		}
		Adaptador adapter = (Adaptador)cmbInterfaces.getSelectedObjects()[0];
        if(adapter.isSupported()) {
            NetworkInfo netInfo = new NetworkInfo(adapter.getIp(), adapter.getMask());
            PingFrame pingFrame = new PingFrame(netInfo);
            pingFrame.setVisible(true);
        } else {
            Mensajes.MensajeError("ERROR", "El adaptador seleccionado no cuenta con una IPv4 valida");
        }
	}
}
