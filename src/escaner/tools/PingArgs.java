package escaner.tools;

import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import escaner.PingFrame;

public class PingArgs {
	public PingFrame pingFrame;
	public JProgressBar progressBar;
	public JTextArea textArea;
	public JLabel label;
	public Container container;

	public PingArgs(PingFrame pingFrame, JProgressBar progressBar, JTextArea textArea, JLabel label,
			Container container) {
		super();
		this.pingFrame = pingFrame;
		this.progressBar = progressBar;
		this.textArea = textArea;
		this.label = label;
		this.container = container;
	}
}
