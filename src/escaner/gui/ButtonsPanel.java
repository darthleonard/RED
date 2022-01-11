package escaner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import escaner.events.PingEvent;
import escaner.events.SaveEvent;
import escaner.tools.Adaptador;
import escaner.tools.Mensajes;

public class ButtonsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private EscanerFrame escanerFrame;
	private EscanerButton btnArp;
	private EscanerButton btnConfig;
	private EscanerButton btnDeleteRecord;
	private EscanerButton btnSave;
	private EscanerButton btnPing;
	private JComboBox<Adaptador> cmbInterfaces;
	private JLabel lblIp;
	private JLabel lblMask;
	private JLabel lblIpValue;
	private JLabel lblMaskVaue;

	public ButtonsPanel(ArrayList<Adaptador> interfaces, EscanerFrame escanerFrame) {
		this.escanerFrame = escanerFrame;
		setBorder(BorderFactory.createEtchedBorder());
		initComponents();
		configComponents(interfaces);
		addListeners();
		addComponents();
	}
	
	public void setIpValue(String lblIpValue) {
		this.lblIpValue.setText(lblIpValue);
	}
	
	public void setMaskVaue(int lblMaskVaue) {
		this.lblMaskVaue.setText("" + lblMaskVaue);
	}

	private void initComponents() {
		lblIp = new JLabel();
		lblMask = new JLabel();
		lblIpValue = new JLabel();
		lblMaskVaue = new JLabel();
		btnSave = new EscanerButton("/images/guardar.png", "Guardar");
		btnArp = new EscanerButton("/images/arp.png", "Cargar tabla ARP");
		btnPing = new EscanerButton("/images/ping.png", "Ping sobre la red");
		btnConfig = new EscanerButton("/images/config.png", "Configuracion");
		btnDeleteRecord = new EscanerButton("/images/eliminar.png", "Eliminar");
		cmbInterfaces = new JComboBox<Adaptador>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void configComponents(ArrayList<Adaptador> interfaces) {
		cmbInterfaces.setModel(new DefaultComboBoxModel(interfaces.toArray()));
		Adaptador adapter = (Adaptador) cmbInterfaces.getSelectedObjects()[0];
		escanerFrame.setSelectedAdapter(adapter);
		lblIp.setText("Direccion de red:");
		lblMask.setText("Mascara de red:");
		lblIpValue.setText(escanerFrame.getCurrentNetworkIp());
		lblMaskVaue.setText(escanerFrame.getCurrentNetworkMask() + "");
	}

	private void addListeners() {
		btnSave.setEventHandler(new SaveEvent());
		btnSave.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent evt) {
				escanerFrame.guardarTabla();
			}
		});

		btnArp.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent evt) {
				escanerFrame.loadArpTable();
			}
		});

		PingEvent pingEvent = new PingEvent(cmbInterfaces);
		btnPing.setEventHandler(pingEvent);

		btnConfig.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent evt) {
				Mensajes.MensajeAlerta("Lo siento :(", "No implementado aun :/");
			}
		});

		btnDeleteRecord.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent evt) {
				escanerFrame.eliminaRegistrosSeleccionados();
			}
		});

		cmbInterfaces.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				escanerFrame.setSelectedAdapter((Adaptador) cmbInterfaces.getSelectedObjects()[0]);
			}
		});
	}

	private void addComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		adjustSpace(constraints, 2);

		setConstraints(constraints, 0, 0, 1, 2);
		add(btnSave, constraints);

		setConstraints(constraints, 1, 0, 1, 2);
		add(btnArp, constraints);

		setConstraints(constraints, 2, 0, 1, 2);
		add(btnPing, constraints);

		setConstraints(constraints, 3, 0, 1, 2);
		add(btnConfig, constraints);

		setConstraints(constraints, 4, 0, 1, 2);
		adjustSpace(constraints, 15);
		add(btnDeleteRecord, constraints);

		setConstraints(constraints, 5, 0, 4, 1);
		constraints.anchor = GridBagConstraints.EAST;
		adjustSpace(constraints, 2);
		add(cmbInterfaces, constraints);

		setConstraints(constraints, 5, 1, 1, 1);
		adjustSpace(constraints, 5);
		add(lblIp, constraints);

		setConstraints(constraints, 6, 1, 1, 1);
		adjustSpace(constraints, 10);
		add(lblIpValue, constraints);

		setConstraints(constraints, 7, 1, 1, 1);
		adjustSpace(constraints, 5);
		add(lblMask, constraints);

		setConstraints(constraints, 8, 1, 1, 1);
		adjustSpace(constraints, 2);
		add(lblMaskVaue, constraints);
	}

	private void setConstraints(GridBagConstraints constraints, int gridx, int gridy, int gridwidth, int gridheight) {
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
	}

	private void adjustSpace(GridBagConstraints constraints, int rightSpace) {
		constraints.insets = new Insets(2, 2, 2, rightSpace);
	}
}
