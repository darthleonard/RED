/*
 * Copyright (C) 2017 Leonardo Gonzalez Caracosa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package escaner;

import escaner.tools.Adaptador;
import escaner.tools.Tool;
import escaner.tools.Archivos;
import escaner.tools.NetworkInfo;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author darthleonard
 */
public class Escaner extends javax.swing.JFrame {
    public static final int EXISTE = 0;
    public static final int EXISTE2 = 1;
    public static final int CAMBIO = 2;
    public static final int NUEVO = 3;
    public static final int NOIDENTIFICADO = 4;
    
    ArrayList<Adaptador> interfaces;
    
    private final String cabecera[] = new String[]{"", "IP", "MAC", "Descripcion",""};
    ArrayList<String[]> registros;
    TableModel tabla;

    /**
     * Creates new form Escaner
     */
    public Escaner() {
        initComponents();
        setIconImage(getEscanerIcon());
        setLocationRelativeTo(null);
        try {
            cargaInterfaces();
        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "ERROR",
                    "No se pudieron cargar todas las interfaces",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
        tabla = tblHosts.getModel();
        cargarTablaInicial();
    }
    
    public void cargaInterfaces() throws SocketException {
        interfaces = new ArrayList<>();
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        Adaptador adaptador;
        
    	while (en.hasMoreElements()) {
            NetworkInterface networkAdapter = en.nextElement();
            if(networkAdapter.isUp() && !networkAdapter.isLoopback()) {
                adaptador = new Adaptador(networkAdapter);
                interfaces.add(adaptador);
                cmbInterfaces.addItem(adaptador.getId() + " : " + adaptador.getNombre());
            }
    	}
        
        actualizaDatosRed();
    }
    
    public void cargarTablaInicial() {
        try {
            Archivos a = new Archivos();
            registros = a.Leer();
            cargarTabla(registros);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Escaner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cargarTablaArp() {
        Tool tool = new Tool();
        ArrayList<String[]> datos = tool.getDatos();
        cargarTabla(datos);
    }
    
    private void cargarTabla(ArrayList<String[]> datos) {
        DefaultTableModel modelo = new DefaultTableModel(null, cabecera);
        String ip;
        String mac;
        String com;
        int estado;
        
        ArrayList<String[]> aux = (ArrayList<String[]>) registros.clone();
        
        for(String get[] : datos) {
            estado = NUEVO;
            ip = get[0].trim();
            mac = get[1].trim();
            com = get[2].trim();
            
            for(String reg[] : registros) {
                if(reg[1].equals(mac)) { // la mac ya esta en la lista
                    aux.remove(reg);
                    if(ip.equals(reg[0])) { // la ip no a cambiado
                        if(reg[2].length() != 0) { // el registro se encuentra correcto
                            com = reg[2];
                            estado = EXISTE;
                        } else { // no se ha identificado
                            estado = NOIDENTIFICADO;
                        }
                    } else { // la ip es dferente
                        com = reg[2];
                        estado = CAMBIO;
                    }
                    break;
                } else { // la mac no esta en la lista
                    estado = NUEVO;
                }
            }
            modelo.addRow(new Object[] {modelo.getRowCount()+1, ip, mac, com, estado});
        }
        for(String a[] : aux)
            modelo.addRow(new Object[] {modelo.getRowCount()+1, a[0], a[1], a[2], EXISTE2});
        cargaDatosTabla(modelo);
    }
    
    private void cargaDatosTabla(DefaultTableModel modelo) {
        FormatoTabla ft = new FormatoTabla(this);
        tblHosts.setDefaultRenderer (Object.class, ft );
        tblHosts.setModel(modelo);
        tblHosts.getColumnModel().getColumn(0).setMaxWidth(40);
        tblHosts.getColumnModel().getColumn(4).setMinWidth(0);
        tblHosts.getColumnModel().getColumn(4).setMaxWidth(0);
    }
    
    private void guardarTabla() {
        int res = JOptionPane.showConfirmDialog(null, "¿Deseas guardar?", "Guardar", JOptionPane.OK_CANCEL_OPTION);        
        if(res == 0) {
            try {
                Archivos a = new Archivos();
                ArrayList<String[]> datos = new ArrayList();
                TableModel tableModel = tblHosts.getModel();
                int cols = tableModel.getColumnCount();
                int fils = tableModel.getRowCount();
                
                for(int i=0; i<fils; i++)
                    datos.add(
                        new String[]{
                            tableModel.getValueAt(i,1).toString(),
                            tableModel.getValueAt(i,2).toString(),
                            tableModel.getValueAt(i,3).toString()
                        }
                    );
                
                a.Guardar(datos);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Escaner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void eliminaRegistrosSeleccionados() {
        DefaultTableModel dtm = (DefaultTableModel) tblHosts.getModel();
        int[] a = tblHosts.getSelectedRows();
        for (int i = a.length-1; i >= 0; i--)
            dtm.removeRow(a[i]);
    }
    
    private void revisarRed() {
        Adaptador adaptador = interfaces.get(cmbInterfaces.getSelectedIndex());
        if(adaptador.isSupported()) {
            NetworkInfo netinfo = new NetworkInfo(adaptador.getIp(), adaptador.getMask());
            PingFrame p = new PingFrame(netinfo);
            p.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "El adaptador seleccionado no cuenta con una IPv4 valida",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void configuracion() {
        JOptionPane.showMessageDialog(null, "No implementado aun :/", "Lo siento :(", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Cambia el borde de los JLabel con comportamiento de boton para darle
     * efecto de presion
     * @param btn
     * @param state 
     */
    private void animaBoton(JLabel btn, boolean state) {
        if(state) {
            Border b = BorderFactory.createLoweredBevelBorder();
            btn.setBorder(b);
        } else {
            Border b = BorderFactory.createRaisedBevelBorder();
            btn.setBorder(b);
        }
    }
    
    public Image getEscanerIcon() {
        return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icono.png"));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblHosts = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblIpRed = new javax.swing.JLabel();
        lblMascaraRed = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JLabel();
        btnArp = new javax.swing.JLabel();
        btnPing = new javax.swing.JLabel();
        btnConfig = new javax.swing.JLabel();
        btnEliminarRegistros = new javax.swing.JLabel();
        cmbInterfaces = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmArchivo = new javax.swing.JMenu();
        itmGuardar = new javax.swing.JMenuItem();
        jmHerramientas = new javax.swing.JMenu();
        itmTablaArp = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        itmPing = new javax.swing.JMenuItem();
        jmAyuda = new javax.swing.JMenu();
        itmAyuda = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        itmAcerca = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registro de equipos en red");
        setName("frameMain"); // NOI18N

        tblHosts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "IP", "MAC", "NOTAS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblHosts);
        if (tblHosts.getColumnModel().getColumnCount() > 0) {
            tblHosts.getColumnModel().getColumn(0).setResizable(false);
        }

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Direccion de red:");

        jLabel2.setText("Mascara de red:");

        lblIpRed.setText("192.168.1.0");

        lblMascaraRed.setText("24");

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/guardar.png"))); // NOI18N
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnGuardarMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnGuardarMouseReleased(evt);
            }
        });

        btnArp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/arp.png"))); // NOI18N
        btnArp.setToolTipText("Cargar tabla ARP");
        btnArp.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnArp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnArpMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnArpMouseReleased(evt);
            }
        });

        btnPing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ping.png"))); // NOI18N
        btnPing.setToolTipText("Ping sobre la red");
        btnPing.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnPingMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnPingMouseReleased(evt);
            }
        });

        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/config.png"))); // NOI18N
        btnConfig.setToolTipText("Configuracion");
        btnConfig.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnConfig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnConfigMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnConfigMouseReleased(evt);
            }
        });

        btnEliminarRegistros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eliminar.png"))); // NOI18N
        btnEliminarRegistros.setToolTipText("Eliminar");
        btnEliminarRegistros.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEliminarRegistrosMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEliminarRegistrosMouseReleased(evt);
            }
        });

        cmbInterfaces.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbInterfacesItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnArp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPing)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConfig)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminarRegistros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbInterfaces, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblIpRed)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMascaraRed)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar)
                    .addComponent(btnArp)
                    .addComponent(btnEliminarRegistros)
                    .addComponent(btnPing)
                    .addComponent(btnConfig))
                .addGap(0, 5, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(cmbInterfaces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIpRed)
                    .addComponent(jLabel2)
                    .addComponent(lblMascaraRed)
                    .addComponent(jLabel1)))
        );

        jmArchivo.setMnemonic('K');
        jmArchivo.setText("Archivo");

        itmGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        itmGuardar.setText("Guardar cambios");
        itmGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmGuardarActionPerformed(evt);
            }
        });
        jmArchivo.add(itmGuardar);

        jMenuBar1.add(jmArchivo);

        jmHerramientas.setText("Herramientas");

        itmTablaArp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        itmTablaArp.setText("Cargar tabla ARP");
        itmTablaArp.setToolTipText("");
        itmTablaArp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmTablaArpActionPerformed(evt);
            }
        });
        jmHerramientas.add(itmTablaArp);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Limpiar tabla ARP");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jmHerramientas.add(jMenuItem1);

        itmPing.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        itmPing.setText("Ping sobre red");
        itmPing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmPingActionPerformed(evt);
            }
        });
        jmHerramientas.add(itmPing);

        jMenuBar1.add(jmHerramientas);

        jmAyuda.setText("Ayuda");

        itmAyuda.setText("Ayuda");
        itmAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAyudaActionPerformed(evt);
            }
        });
        jmAyuda.add(itmAyuda);
        jmAyuda.add(jSeparator1);

        itmAcerca.setText("Acerca de");
        itmAcerca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAcercaActionPerformed(evt);
            }
        });
        jmAyuda.add(itmAcerca);

        jMenuBar1.add(jmAyuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itmGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmGuardarActionPerformed
        guardarTabla();
    }//GEN-LAST:event_itmGuardarActionPerformed

    private void itmTablaArpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmTablaArpActionPerformed
        cargarTablaArp();
    }//GEN-LAST:event_itmTablaArpActionPerformed

    private void itmPingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmPingActionPerformed
        revisarRed();
    }//GEN-LAST:event_itmPingActionPerformed

    private void itmAcercaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmAcercaActionPerformed
        Object parent = null;
        String titulo = "Acerca de este software";
        String mensaje = "leolinuxmx@gmail.com";
        int tipo = JOptionPane.INFORMATION_MESSAGE;
        
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipo);
    }//GEN-LAST:event_itmAcercaActionPerformed

    private void btnGuardarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMousePressed
        animaBoton(btnGuardar, true);
    }//GEN-LAST:event_btnGuardarMousePressed

    private void btnGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseReleased
        animaBoton(btnGuardar, false);
        guardarTabla();
    }//GEN-LAST:event_btnGuardarMouseReleased

    private void btnArpMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnArpMousePressed
        animaBoton(btnArp, true);
    }//GEN-LAST:event_btnArpMousePressed

    private void btnArpMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnArpMouseReleased
        animaBoton(btnArp, false);
        cargarTablaArp();
    }//GEN-LAST:event_btnArpMouseReleased

    private void btnPingMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPingMousePressed
        animaBoton(btnPing, true);
    }//GEN-LAST:event_btnPingMousePressed

    private void btnPingMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPingMouseReleased
        animaBoton(btnPing, false);
        revisarRed();
    }//GEN-LAST:event_btnPingMouseReleased

    private void btnConfigMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfigMousePressed
        animaBoton(btnConfig, true);
    }//GEN-LAST:event_btnConfigMousePressed

    private void btnConfigMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfigMouseReleased
        animaBoton(btnConfig, false);
        configuracion();
    }//GEN-LAST:event_btnConfigMouseReleased

    private void itmAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmAyudaActionPerformed
        
    }//GEN-LAST:event_itmAyudaActionPerformed

    private void btnEliminarRegistrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarRegistrosMousePressed
        animaBoton(btnEliminarRegistros, true);
    }//GEN-LAST:event_btnEliminarRegistrosMousePressed

    private void btnEliminarRegistrosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarRegistrosMouseReleased
        animaBoton(btnEliminarRegistros, false);
        eliminaRegistrosSeleccionados();
    }//GEN-LAST:event_btnEliminarRegistrosMouseReleased

    private void cmbInterfacesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbInterfacesItemStateChanged
        actualizaDatosRed();
    }//GEN-LAST:event_cmbInterfacesItemStateChanged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        limpiarTablaArp();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    
    private void limpiarTablaArp() {
        Tool t = new Tool();
        try {
            t.LimpiaArp();
            JOptionPane.showMessageDialog(this, "La cache ARP del equipo ha sido limpiada");
        } catch (IOException ex) {
            Logger.getLogger(Escaner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actualizaDatosRed() {
        Adaptador a = interfaces.get(cmbInterfaces.getSelectedIndex());
        String ipRed;
        String mask;
        if(a.isSupported()) {
            NetworkInfo netinfo = new NetworkInfo(a.getIp(), a.getMask());
            ipRed = netinfo.getNetwork();
            mask = "" + netinfo.getMask();
        } else {
            ipRed = a.getIp();
            mask = "" + a.getMask();
        }
        
        lblIpRed.setText(ipRed);
        lblMascaraRed.setText(mask);
    }
    
    public String BuscaAnterior(String aux) {
        for (int i = 0; i < registros.size(); i++) {
            String[] get = registros.get(i);
            if(get[1].equals(aux))
                return get[0];
        }
        return "";
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.put("ToolTip.background", new javax.swing.plaf.ColorUIResource(163, 245, 82));
            Border border = BorderFactory.createLineBorder(new Color(78, 154, 6));
            javax.swing.UIManager.put("ToolTip.border", border);
            javax.swing.ToolTipManager.sharedInstance().setDismissDelay(10000);
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | 
                 InstantiationException | 
                 IllegalAccessException | 
                 javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Escaner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new Escaner().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnArp;
    private javax.swing.JLabel btnConfig;
    private javax.swing.JLabel btnEliminarRegistros;
    private javax.swing.JLabel btnGuardar;
    private javax.swing.JLabel btnPing;
    private javax.swing.JComboBox<String> cmbInterfaces;
    private javax.swing.JMenuItem itmAcerca;
    private javax.swing.JMenuItem itmAyuda;
    private javax.swing.JMenuItem itmGuardar;
    private javax.swing.JMenuItem itmPing;
    private javax.swing.JMenuItem itmTablaArp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenu jmArchivo;
    private javax.swing.JMenu jmAyuda;
    private javax.swing.JMenu jmHerramientas;
    private javax.swing.JLabel lblIpRed;
    private javax.swing.JLabel lblMascaraRed;
    private javax.swing.JTable tblHosts;
    // End of variables declaration//GEN-END:variables
}