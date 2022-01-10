package escaner.events;

import javax.swing.JOptionPane;

public class SaveEvent implements IEscanerEvent {

	@Override
	public void Execute() {
		JOptionPane.showMessageDialog(null, "save event");
	}

}
