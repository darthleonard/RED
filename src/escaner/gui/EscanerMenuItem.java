package escaner.gui;

import java.awt.event.InputEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class EscanerMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;

	public EscanerMenuItem(String text) {
		setText(text);
	}
	
	public EscanerMenuItem(int keyEventCode, String text) {
		setAccelerator(KeyStroke.getKeyStroke(keyEventCode, InputEvent.CTRL_MASK));
		setText(text);
	}
}
