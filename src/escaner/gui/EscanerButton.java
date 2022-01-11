package escaner.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import escaner.events.IEscanerEvent;

public class EscanerButton extends JLabel {
	private static final long serialVersionUID = 1L;

	private String imagePath;
	private String tooltipText;

	private IEscanerEvent eventHandler;

	public EscanerButton(String imagePath, String tooltipText) {
		this.imagePath = imagePath;
		this.tooltipText = tooltipText;
		init();
	}

	public void setEventHandler(IEscanerEvent eventHandler) {
		this.eventHandler = eventHandler;
	}

	private void init() {
		setIcon(getImageIcon(imagePath));
		setToolTipText(tooltipText);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				setBorder(BorderFactory.createLoweredBevelBorder());
			}

			public void mouseReleased(MouseEvent evt) {
				if (eventHandler != null) {
					eventHandler.Execute();
				}
				setBorder(BorderFactory.createRaisedBevelBorder());
			}
		});
	}

	private ImageIcon getImageIcon(String path) {
		return new ImageIcon(getClass().getResource(path));
	}
}
