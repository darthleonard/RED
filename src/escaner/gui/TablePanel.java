package escaner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import escaner.services.DeviceStatus;
import escaner.tools.Archivos;
import escaner.tools.Mensajes;

public class TablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JScrollPane scrollPane;
	private JTable hostsTable;
	private ArrayList<String[]> dataSource;

	private final String headers[] = new String[] { "", "IP", "MAC", "Descripcion", "Status" };

	public TablePanel() {
		initComponents();
		configTable();
		addComponents();
	}

	public void setDataSource(ArrayList<String[]> dataSource) {
		if (this.dataSource == null) {
			this.dataSource = dataSource;
		}
		updateTableDta(dataSource);
	}

	public void deleteSelectedRecords() {
		DefaultTableModel tableModel = (DefaultTableModel) hostsTable.getModel();
		int[] rows = hostsTable.getSelectedRows();
		for (int row = rows.length - 1; row >= 0; row--) {
			tableModel.removeRow(rows[row]);
		}
	}

	public void saveTable() {
		int response = JOptionPane.showConfirmDialog(null, "¿Deseas guardar?", "Guardar", JOptionPane.OK_CANCEL_OPTION);
		if (response == 0) {
			try {
				Archivos archivos = new Archivos();
				ArrayList<String[]> dataSource = new ArrayList<String[]>();
				TableModel tableModel = hostsTable.getModel();
				tableModel.getColumnCount();
				int rowsCount = tableModel.getRowCount();

				for (int i = 0; i < rowsCount; i++) {
					dataSource.add(new String[] { tableModel.getValueAt(i, 1).toString(),
							tableModel.getValueAt(i, 2).toString(), tableModel.getValueAt(i, 3).toString() });
				}
				archivos.Guardar(dataSource);
			} catch (FileNotFoundException ex) {
				Mensajes.MensajeError("ERROR", "No se localizo el archivo de datos");
			}
		}
	}

	public String searchPrevious(String macAddress) {
		for (int i = 0; i < dataSource.size(); i++) {
			String[] record = dataSource.get(i);
			if (record[1].equals(macAddress)) {
				return record[0];
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

	private void updateTableDta(ArrayList<String[]> dataSource) {
		String ip;
		String mac;
		String description;
		int status;
		DefaultTableModel tableModel = (DefaultTableModel) hostsTable.getModel();
		clearModel(tableModel);
		ArrayList<String[]> fileRecords = new ArrayList<String[]>(this.dataSource);

		for (String data[] : dataSource) {
			status = DeviceStatus.NEW;
			ip = data[0].trim();
			mac = data[1].trim();
			description = data[2].trim();

			for (String record[] : this.dataSource) {
				if (record[1].equals(mac)) { // la mac ya esta en la lista
					fileRecords.remove(record);
					if (ip.equals(record[0])) { // la ip no a cambiado
						if (record[2].length() != 0) { // el registro se encuentra correcto
							description = record[2];
							status = DeviceStatus.EXIST;
						} else { // no se ha identificado
							status = DeviceStatus.UNKNOWN;
						}
					} else { // la ip es dferente
						description = record[2];
						status = DeviceStatus.CHANGED;
					}
					break;
				} else { // la mac no esta en la lista
					status = DeviceStatus.NEW;
				}
			}
			tableModel.addRow(new Object[] { tableModel.getRowCount() + 1, ip, mac, description, status });
		}
		for (String registro[] : fileRecords) {
			tableModel.addRow(new Object[] { tableModel.getRowCount() + 1, registro[0], registro[1], registro[2],
					DeviceStatus.OFFLINE });
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
