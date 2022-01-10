package escaner.gui;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu.Separator;

import escaner.listeners.HelpMenuActionListener;
import escaner.listeners.MenuActionListener;
import escaner.listeners.MenuActions;

public class EscanerMenu {
	private JMenuBar menuBar;
	private JMenu jmArchivo;
	private JMenu jmAyuda;
	private JMenu jmHerramientas;
	private EscanerMenuItem itmClearArpTable;
	private EscanerMenuItem itmAcerca;
	private EscanerMenuItem itmAyuda;
	private EscanerMenuItem itmAyudaGuardar;
	private EscanerMenuItem itmAyudaCargarTablaARP;
	private EscanerMenuItem itmAyudaPing;
	private EscanerMenuItem itmAyudaColores;
	private EscanerMenuItem itmGuardar;
	private EscanerMenuItem itmPing;
	private EscanerMenuItem itmTablaArp;
	private Separator separator;

	public EscanerMenu() {
		initComponents();
		configComponents();
		addListeners();
		addComponents();
	}

	public JMenuBar CreateMenu() {
		return menuBar;
	}

	private void initComponents() {
		menuBar = new JMenuBar();
		jmArchivo = new JMenu();
		itmGuardar = new EscanerMenuItem(KeyEvent.VK_S, "Guardar cambios");
		jmHerramientas = new JMenu();
		itmTablaArp = new EscanerMenuItem(KeyEvent.VK_O, "Cargar tabla ARP");
		itmClearArpTable = new EscanerMenuItem(KeyEvent.VK_L, "Limpiar tabla ARP");
		itmPing = new EscanerMenuItem(KeyEvent.VK_P, "Ping sobre red");
		jmAyuda = new JMenu();
		itmAyuda = new EscanerMenuItem("Ayuda");
		itmAyudaColores = new EscanerMenuItem("Colores de la tabla");
		itmAyudaGuardar = new EscanerMenuItem("Que guarda");
		itmAyudaCargarTablaARP = new EscanerMenuItem("Tabla ARP");
		itmAyudaPing = new EscanerMenuItem("Herramienta ping");
		separator = new Separator();
		itmAcerca = new EscanerMenuItem("Acerca de");
	}

	private void configComponents() {
		jmArchivo.setMnemonic('K');
		jmArchivo.setText("Archivo");
		jmHerramientas.setText("Herramientas");
		jmAyuda.setText("Ayuda");
	}
	
	private void addListeners() {
		itmGuardar.addActionListener(new MenuActionListener(MenuActions.Save));
		itmTablaArp.addActionListener(new MenuActionListener(MenuActions.LoadArpTable));
		itmClearArpTable.addActionListener(new MenuActionListener(MenuActions.ClearArpTable));
		itmPing.addActionListener(new MenuActionListener(MenuActions.Ping));
		itmAyuda.addActionListener(new HelpMenuActionListener(MenuActions.Help));
		itmAyudaGuardar.addActionListener(new HelpMenuActionListener(MenuActions.Save));
		itmAyudaCargarTablaARP.addActionListener(new HelpMenuActionListener(MenuActions.LoadArpTable));
		itmAyudaPing.addActionListener(new HelpMenuActionListener(MenuActions.Ping));
		itmAyudaColores.addActionListener(new HelpMenuActionListener(MenuActions.Colors));
		itmAcerca.addActionListener(new HelpMenuActionListener(MenuActions.About));
	}

	private void addComponents() {
		jmArchivo.add(itmGuardar);
		jmHerramientas.add(itmTablaArp);
		jmHerramientas.add(itmClearArpTable);
		jmHerramientas.add(itmPing);
		jmAyuda.add(itmAyuda);
		jmAyuda.add(itmAyudaGuardar);
		jmAyuda.add(itmAyudaCargarTablaARP);
		jmAyuda.add(itmAyudaPing);
		jmAyuda.add(itmAyudaColores);
		jmAyuda.add(separator);
		jmAyuda.add(itmAcerca);
		menuBar.add(jmArchivo);
		menuBar.add(jmHerramientas);
		menuBar.add(jmAyuda);
	}
}
