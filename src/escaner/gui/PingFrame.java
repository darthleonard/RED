package escaner.gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import escaner.tools.Mensajes;
import escaner.tools.NetworkInfo;
import escaner.tools.Ping;
import escaner.tools.PingArgs;

public class PingFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JButton btnRunStop;
	private JLabel lblDirRed;
	private JLabel lblMaskRed;
	private JLabel lblLatencia;
	private JProgressBar progressBar;
	private JScrollPane scrollPane;
	private JLabel lblConteoHosts;
	private JLabel lblImagen;
	private JTextField txtDirRed;
	private JTextArea txtDirecciones;
	private JTextField txtLatencia;
	private JTextField txtMaskRed;

	private Ping ping;
	private NetworkInfo networkInfo;
	private boolean Ejecutando = false;

	public PingFrame(NetworkInfo networkInfo) {
		initComponents();
		this.networkInfo = networkInfo;
		txtDirRed.setText(networkInfo.getNetwork());
		txtMaskRed.setText("" + networkInfo.getMask());

		setLocationRelativeTo(null);

		Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.png"));
		setIconImage(icon);

		lblImagen
				.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/radar.png"))
						.getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_DEFAULT)));
	}

	private void initComponents() {
		progressBar = new JProgressBar();
		btnRunStop = new JButton();
		lblDirRed = new JLabel();
		lblMaskRed = new JLabel();
		txtDirRed = new JTextField();
		txtMaskRed = new JTextField();
		lblLatencia = new JLabel();
		txtLatencia = new JTextField();
		scrollPane = new JScrollPane();
		txtDirecciones = new JTextArea();
		lblConteoHosts = new JLabel();
		lblImagen = new JLabel();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Ping");
		setName("pingFrame");
		setResizable(false);

		btnRunStop.setText("Comenzar");
		btnRunStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		lblDirRed.setText("Direccion de red");
		lblMaskRed.setText("Mascara de red");
		txtDirRed.setText("192.168.1.0");
		txtMaskRed.setText("24");
		txtDirRed.setEditable(false);
		txtMaskRed.setEditable(false);
		lblLatencia.setText("Tiempo de espera");
		txtLatencia.setText("100");
		
		txtDirecciones.setEditable(false);
		txtDirecciones.setBackground(new Color(0, 0, 0));
		txtDirecciones.setColumns(20);
		txtDirecciones.setForeground(new Color(0, 255, 0));
		txtDirecciones.setRows(5);
		scrollPane.setViewportView(txtDirecciones);

		lblConteoHosts.setText("Conteo de hosts");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(scrollPane).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup().addComponent(lblConteoHosts)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnRunStop))))
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
								layout.createSequentialGroup()
										.addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 96,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(lblMaskRed, javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(lblDirRed, javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(lblLatencia, javax.swing.GroupLayout.Alignment.TRAILING))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(txtMaskRed, javax.swing.GroupLayout.PREFERRED_SIZE, 120,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtLatencia, javax.swing.GroupLayout.PREFERRED_SIZE, 120,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(txtDirRed, javax.swing.GroupLayout.PREFERRED_SIZE, 120,
														javax.swing.GroupLayout.PREFERRED_SIZE))))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addGroup(layout.createSequentialGroup().addContainerGap()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(txtDirRed, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(lblDirRed))
										.addGap(8, 8, 8)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(txtMaskRed, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(lblMaskRed))
										.addGap(5, 5, 5)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(txtLatencia, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(lblLatencia)))
								.addComponent(lblImagen, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(18, 18, 18)
						.addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnRunStop).addComponent(lblConteoHosts))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}

	private boolean validaDireccion(final String dir) {
		String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
		return dir.matches(PATTERN);
	}

	private boolean validaDatos(String ip, String mask, String lat) {
		if (!validaDireccion(ip)) {
			Mensajes.MensajeError("ERROR de IP", "Direccion de red invalida");
			return false;
		}

		try {
			int m = Integer.parseInt(mask);
			if (m < 1 || m > 30) {
				Mensajes.MensajeError("Mascara no valida\nMascara no valida, solo se adminten valores entre 1 y 32");
				return false;
			}
		} catch (NumberFormatException e) {
			Mensajes.MensajeError("Mascara no valida\nLa Mascara debe ser expresada en numero decimal entero");
			return false;
		}

		try {
			int l = Integer.parseInt(lat);
			if (l < 10 || l > 5000) {
				Mensajes.MensajeError("El tiempo de espera debe ser entre 10 y 5000");
				return false;
			}
		} catch (NumberFormatException e) {
			Mensajes.MensajeError("Valor de latencia invalido");
			return false;
		}

		return true;
	}

	public void switchControles() {
		if (!Ejecutando) {
			btnRunStop.setText("Cancelar");
//            txtDirRed.setEditable(false);
//            txtMaskRed.setEditable(false);
			txtLatencia.setEditable(false);
			Ejecutando = true;
		} else {
			btnRunStop.setText("Comenzar");
//            txtDirRed.setEditable(true);
//            txtMaskRed.setEditable(true);
			txtLatencia.setEditable(true);
			Ejecutando = false;
		}
	}

	private void ejecutarPing() {
		if (!validaDatos(txtDirRed.getText(), txtMaskRed.getText(), txtLatencia.getText())) {
			return;
		}

		switchControles();
		PingArgs pingArgs = new PingArgs(this, progressBar, txtDirecciones, lblConteoHosts, getContentPane());
		ping = new Ping(pingArgs);
		ping.setNetinfo(networkInfo);
		ping.setTiempo(Integer.parseInt(txtLatencia.getText()));
		ping.execute();
	}

	public void setNetworkDataRed(String red, String mask) {
		txtDirRed.setText(red);
		txtMaskRed.setText(mask);
	}

	private void jButton1ActionPerformed(ActionEvent evt) {
		if (!Ejecutando) {
			ejecutarPing();
		} else {
			ping.cancel(true);
			ping = null;
		}
	}
}
