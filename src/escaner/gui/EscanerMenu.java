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
	private JMenu jmFile;
	private JMenu jmHelp;
	private JMenu jmTools;
	private EscanerMenuItem itmClearArpTable;
	private EscanerMenuItem itmAbout;
	private EscanerMenuItem itmHelp;
	private EscanerMenuItem itmHelpSave;
	private EscanerMenuItem itmHelpLoadArpTable;
	private EscanerMenuItem itmHelpPing;
	private EscanerMenuItem itmHelpColors;
	private EscanerMenuItem itmSave;
	private EscanerMenuItem itmPing;
	private EscanerMenuItem itmArpTable;
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
		jmFile = new JMenu();
		itmSave = new EscanerMenuItem(KeyEvent.VK_S, "Guardar cambios");
		jmTools = new JMenu();
		itmArpTable = new EscanerMenuItem(KeyEvent.VK_O, "Cargar tabla ARP");
		itmClearArpTable = new EscanerMenuItem(KeyEvent.VK_L, "Limpiar tabla ARP");
		itmPing = new EscanerMenuItem(KeyEvent.VK_P, "Ping sobre red");
		jmHelp = new JMenu();
		itmHelp = new EscanerMenuItem("Ayuda");
		itmHelpColors = new EscanerMenuItem("Colores de la tabla");
		itmHelpSave = new EscanerMenuItem("Que guarda");
		itmHelpLoadArpTable = new EscanerMenuItem("Tabla ARP");
		itmHelpPing = new EscanerMenuItem("Herramienta ping");
		separator = new Separator();
		itmAbout = new EscanerMenuItem("Acerca de");
	}

	private void configComponents() {
		jmFile.setMnemonic('K');
		jmFile.setText("Archivo");
		jmTools.setText("Herramientas");
		jmHelp.setText("Ayuda");
	}
	
	private void addListeners() {
		itmSave.addActionListener(new MenuActionListener(MenuActions.Save));
		itmArpTable.addActionListener(new MenuActionListener(MenuActions.LoadArpTable));
		itmClearArpTable.addActionListener(new MenuActionListener(MenuActions.ClearArpTable));
		itmPing.addActionListener(new MenuActionListener(MenuActions.Ping));
		itmHelp.addActionListener(new HelpMenuActionListener(MenuActions.Help));
		itmHelpSave.addActionListener(new HelpMenuActionListener(MenuActions.Save));
		itmHelpLoadArpTable.addActionListener(new HelpMenuActionListener(MenuActions.LoadArpTable));
		itmHelpPing.addActionListener(new HelpMenuActionListener(MenuActions.Ping));
		itmHelpColors.addActionListener(new HelpMenuActionListener(MenuActions.Colors));
		itmAbout.addActionListener(new HelpMenuActionListener(MenuActions.About));
	}

	private void addComponents() {
		jmFile.add(itmSave);
		jmTools.add(itmArpTable);
		jmTools.add(itmClearArpTable);
		jmTools.add(itmPing);
		jmHelp.add(itmHelp);
		jmHelp.add(itmHelpSave);
		jmHelp.add(itmHelpLoadArpTable);
		jmHelp.add(itmHelpPing);
		jmHelp.add(itmHelpColors);
		jmHelp.add(separator);
		jmHelp.add(itmAbout);
		menuBar.add(jmFile);
		menuBar.add(jmTools);
		menuBar.add(jmHelp);
	}
}
