package escaner.gui;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import escaner.FormatoTabla;
import escaner.tools.Archivos;
import escaner.tools.Mensajes;

public class TablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int EXISTE = 0;
	public static final int EXISTE2 = 1;
	public static final int CAMBIO = 2;
	public static final int NUEVO = 3;
	public static final int NOIDENTIFICADO = 4;

	private JScrollPane scrollPane;
	private JTable tblHosts;
	private ArrayList<String[]> dataSource;

	private final String cabecera[] = new String[] { "", "IP", "MAC", "Descripcion", "Status" };

	public TablePanel() {
		initComponents();

		tblHosts.setModel(new DefaultTableModel(new Object[][] {}, cabecera) {
			private static final long serialVersionUID = 1L;
			boolean[] canEdit = new boolean[] { false, false, false, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		tblHosts.setSelectionForeground(new java.awt.Color(102, 255, 102));
		scrollPane.setViewportView(tblHosts);
		if (tblHosts.getColumnModel().getColumnCount() > 0) {
			tblHosts.getColumnModel().getColumn(0).setResizable(false);
		}

		DefaultTableModel modelo = getDefaultTableModel();
		FormatoTabla ft = new FormatoTabla(this);
		tblHosts.setDefaultRenderer (Object.class, ft);
		tblHosts.setModel(modelo);
		tblHosts.getColumnModel().getColumn(0).setMaxWidth(40);
		hideStatusColumn();
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tblHosts.getModel());
		tblHosts.setRowSorter(sorter);

		addComponents();
	}

	public void setDataSource(ArrayList<String[]> dataSource) {
		if (this.dataSource == null) {
			this.dataSource = dataSource;
		}
		updateTableDta(dataSource);
	}

	public void eliminaRegistrosSeleccionados() {
		DefaultTableModel dtm = (DefaultTableModel) tblHosts.getModel();
		int[] a = tblHosts.getSelectedRows();
		for (int i = a.length - 1; i >= 0; i--) {
			dtm.removeRow(a[i]);
		}
	}
	
	public void guardarTabla() {
        int res = JOptionPane.showConfirmDialog(null, "¿Deseas guardar?", "Guardar", JOptionPane.OK_CANCEL_OPTION);        
        if(res == 0) {
            try {
                Archivos a = new Archivos();
                ArrayList<String[]> datos = new ArrayList<String[]>();
                TableModel tableModel = tblHosts.getModel();
                tableModel.getColumnCount();
                int fils = tableModel.getRowCount();
                
                for(int i = 0; i < fils; i++) {
                    datos.add(
                        new String[]{
                            tableModel.getValueAt(i,1).toString(),
                            tableModel.getValueAt(i,2).toString(),
                            tableModel.getValueAt(i,3).toString()
                        }
                    );
                }
                a.Guardar(datos);
            } catch (FileNotFoundException ex) {
                Mensajes.MensajeError("ERROR", "No se localizo el archivo de datos");
            }
        }
    }
	
	public String BuscaAnterior(String aux) {
        for (int i = 0; i < dataSource.size(); i++) {
            String[] get = dataSource.get(i);
            if(get[1].equals(aux))
                return get[0];
        }
        return "";
    }

	private void initComponents() {
		scrollPane = new JScrollPane();
		tblHosts = new JTable();
	}

	private void addComponents() {
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	private DefaultTableModel getDefaultTableModel() {
		return new DefaultTableModel(null, cabecera) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int i, int i1) {
				return getColumnName(i1).equals(cabecera[3]);
			}
		};
	}

	private void updateTableDta(ArrayList<String[]> dataSource) {
		String ip;
		String mac;
		String descripcion;
		int estado;
		DefaultTableModel modelo = (DefaultTableModel) tblHosts.getModel();
		clearModel(modelo);
		ArrayList<String[]> registrosDelArchivo = new ArrayList<String[]>(this.dataSource);

		for (String get[] : dataSource) {
			estado = NUEVO;
			ip = get[0].trim();
			mac = get[1].trim();
			descripcion = get[2].trim();

			for (String registro[] : this.dataSource) {
				if (registro[1].equals(mac)) { // la mac ya esta en la lista
					registrosDelArchivo.remove(registro);
					if (ip.equals(registro[0])) { // la ip no a cambiado
						if (registro[2].length() != 0) { // el registro se encuentra correcto
							descripcion = registro[2];
							estado = EXISTE;
						} else { // no se ha identificado
							estado = NOIDENTIFICADO;
						}
					} else { // la ip es dferente
						descripcion = registro[2];
						estado = CAMBIO;
					}
					break;
				} else { // la mac no esta en la lista
					estado = NUEVO;
				}
			}
			modelo.addRow(new Object[] { modelo.getRowCount() + 1, ip, mac, descripcion, estado });
		}
		for (String registro[] : registrosDelArchivo) {
			modelo.addRow(new Object[] { modelo.getRowCount() + 1, registro[0], registro[1], registro[2], EXISTE2 });
		}

		tblHosts.setModel(modelo);
	}

	private void clearModel(DefaultTableModel model) {
		// https://docs.oracle.com/javase/7/docs/api/javax/swing/table/DefaultTableModel.html#setRowCount(int)
		model.setRowCount(0);
	}

	private void hideStatusColumn() {
		tblHosts.getColumnModel().getColumn(4).setMinWidth(0);
		tblHosts.getColumnModel().getColumn(4).setMaxWidth(0);
	}
}
