package escaner.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;

import escaner.models.DeviceDataset;
import escaner.services.NetworkInterfacesService;
import escaner.tools.Adaptador;
import escaner.tools.Mensajes;
import escaner.tools.NetworkInfo;
import escaner.tools.Tool;

public class EscanerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String title = "Registro de Equipos en Red";

	private TablePanel tablePanel;
	private ButtonsPanel buttonsPanel;
	private Adaptador selectedAdapter;
	private String currentNetworkIp;
	private int currentNetworkMask;

	public EscanerFrame() {
		setTitle(title);
		setSize(600, 400);
		init();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		loadSavedData();
		loadArpTable();
	}

	public void setSelectedAdapter(Adaptador adapter) {
		if (adapter.isSupported()) {
			NetworkInfo netInfo = new NetworkInfo(adapter.getIp(), adapter.getMask());
			currentNetworkIp = netInfo.getNetwork();
			currentNetworkMask = netInfo.getMask();
		} else {
			currentNetworkIp = adapter.getIp();
			currentNetworkMask = adapter.getMask();
		}
		selectedAdapter = adapter;
	}

	public void loadArpTable() {
		if (selectedAdapter.isSupported()) {
			Tool tool = new Tool(currentNetworkIp, currentNetworkMask);
			tablePanel.setDataSource(tool.getDatos());
		} else {
			Mensajes.MensajeError("Error", "El adaptador seleccionado no es soportado");
		}
	}

	public void eliminaRegistrosSeleccionados() {
		tablePanel.deleteSelectedRecords();
	}

	public void guardarTabla() {
		tablePanel.saveTable();
	}

	public void openPingTool() {
		if (selectedAdapter == null) {
			Mensajes.MensajeError("ERROR", "No se ha seleccionado ningun adaptador de red");
			return;
		}
		if (selectedAdapter.isSupported()) {
			NetworkInfo netInfo = new NetworkInfo(selectedAdapter.getIp(), selectedAdapter.getMask());
			PingFrame pingFrame = new PingFrame(netInfo);
			pingFrame.setVisible(true);
		} else {
			Mensajes.MensajeError("ERROR", "El adaptador seleccionado no cuenta con una IPv4 valida");
		}
	}

	public String getCurrentNetworkIp() {
		return currentNetworkIp;
	}

	public int getCurrentNetworkMask() {
		return currentNetworkMask;
	}

	private void init() {
		ArrayList<Adaptador> interfaces = new ArrayList<Adaptador>();
		try {
			interfaces = new NetworkInterfacesService().GetInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		this.buttonsPanel = new ButtonsPanel(interfaces, this);
		this.tablePanel = new TablePanel();

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.png")));
		setJMenuBar(new EscanerMenu(this).CreateMenu());
		setLayout(new BorderLayout());
		add(buttonsPanel, BorderLayout.NORTH);
		add(tablePanel, BorderLayout.CENTER);
	}

	public void loadSavedData() {
		DeviceDataset devices = new DeviceDataset("");
		tablePanel.setDataSource(devices);
	}
}
