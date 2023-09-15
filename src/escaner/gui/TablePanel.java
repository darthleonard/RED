package escaner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import escaner.models.DeviceDataset;
import escaner.models.DeviceRecord;
import escaner.services.DeviceStatus;
import escaner.tools.Mensajes;

public class TablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JScrollPane scrollPane;
	private JTable hostsTable;
	private DeviceDataset dataSource = new DeviceDataset();

	private final String headers[] = new String[] { "", "IP", "MAC", "Descripcion", "Status" };

	public TablePanel() {
		initComponents();
		configTable();
		addComponents();
	}

	public void SetDataSource(DeviceDataset dataSource) {
		this.dataSource = dataSource;
		updateTableData();
	}

	public void UpdateDataSource(DeviceDataset newRecords) {
		for (DeviceRecord record : dataSource) {
			record.setStatus(DeviceStatus.OFFLINE);
		}
		List<String> currentMacs = dataSource.stream().map(r -> r.getMacAddress()).collect(Collectors.toList());
		for (DeviceRecord newRecord : newRecords) {
			if (!currentMacs.contains(newRecord.getMacAddress())) {
				newRecord.setStatus(DeviceStatus.NEW);
				dataSource.add(newRecord);
				continue;
			}
			DeviceRecord record = dataSource.FirstOrDefault(r -> r.getMacAddress().equals(newRecord.getMacAddress()));
			int currentStatus = DeviceStatus.EXIST;
			if (!record.getIpAddress().equals(newRecord.getIpAddress())) {
				currentStatus = DeviceStatus.CHANGED;
			}
			if (record.getDescription().isEmpty()) {
				currentStatus = DeviceStatus.UNKNOWN;
			}
			record.setStatus(currentStatus);
		}
		updateTableData();
	}

	public void deleteSelectedRecords() {
		int response = Mensajes.MensajeConfirmacion("Borrar", "El registro se eliminara por completo\n¿Deseas borrar?");
		if (response != 0) {
			return;
		}
		DefaultTableModel tableModel = (DefaultTableModel) hostsTable.getModel();
		int[] rows = hostsTable.getSelectedRows();
		for (int row = rows.length - 1; row >= 0; row--) {
			try {
				String macAddress = (String) tableModel.getValueAt(rows[row], 2);
				dataSource.Delete(macAddress);
				tableModel.removeRow(rows[row]);
			} catch (SQLException e) {
				Mensajes.MensajeError("Error al eliminar", e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	public void saveTable() {
		int response = Mensajes.MensajeConfirmacion("Guardar", "¿Deseas guardar?");
		if (response != 0) {
			return;
		}

		try {
			dataSource.SaveAll();
			UpdateDataSource(dataSource);
		} catch (SQLException e) {
			Mensajes.MensajeError("Error al guardar", e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public String searchPrevious(String macAddress) {
		for (DeviceRecord device : dataSource) {
			if (device.getMacAddress().equals(macAddress)) {
				return device.getMacAddress();
			}
		}
		return null;
	}

	private void initComponents() {
		scrollPane = new JScrollPane();
		hostsTable = new JTable();
	}

	private void configTable() {
		hostsTable.setModel(new DefaultTableModel(new Object[][] {}, headers) {
			private static final long serialVersionUID = 1L;
			boolean[] canEdit = new boolean[] { false, false, false, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		hostsTable.setSelectionForeground(new Color(102, 255, 102));
		scrollPane.setViewportView(hostsTable);
		if (hostsTable.getColumnModel().getColumnCount() > 0) {
			hostsTable.getColumnModel().getColumn(0).setResizable(false);
		}

		DefaultTableModel tableModel = getDefaultTableModel();
		FormatoTabla ft = new FormatoTabla(this);
		hostsTable.setDefaultRenderer(Object.class, ft);
		hostsTable.setModel(tableModel);
		hostsTable.getColumnModel().getColumn(0).setMaxWidth(40);
		hideStatusColumn();
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(hostsTable.getModel());
		hostsTable.setRowSorter(sorter);
	}

	private void addComponents() {
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	private DefaultTableModel getDefaultTableModel() {
		return new DefaultTableModel(null, headers) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int i, int i1) {
				return getColumnName(i1).equals(headers[3]);
			}
		};
	}

	private void updateTableData() {
		DefaultTableModel tableModel = (DefaultTableModel) hostsTable.getModel();
		clearModel(tableModel);
		for (DeviceRecord registro : dataSource) {
			tableModel.addRow(new Object[] { tableModel.getRowCount() + 1, registro.getIpAddress(),
					registro.getMacAddress(), registro.getDescription(), registro.getStatus() });
		}

		hostsTable.setModel(tableModel);
	}

	private void clearModel(DefaultTableModel model) {
		// https://docs.oracle.com/javase/7/docs/api/javax/swing/table/DefaultTableModel.html#setRowCount(int)
		model.setRowCount(0);
	}

	private void hideStatusColumn() {
		hostsTable.getColumnModel().getColumn(4).setMinWidth(0);
		hostsTable.getColumnModel().getColumn(4).setMaxWidth(0);
	}
}
