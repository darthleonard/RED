package escaner.gui;

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
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
	            setBorder(BorderFactory.createLoweredBevelBorder());
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				if(eventHandler != null) {
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
