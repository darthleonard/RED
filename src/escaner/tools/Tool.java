package escaner.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class Tool {
    private int mask = 24;
    private String red;
    private String redb;
    private final ArrayList<String[]> datos;
    
    private StringTokenizer st;
    
    public Tool(String red, int mask) {
        this.red = red;
        this.mask = mask;
        datos = new ArrayList();
        convierteRed();
        Arp();
    }
    
    private void convierteRed() {
        String aux;
        st = new StringTokenizer(red, ".");
        redb = "";
        while(st.hasMoreTokens()) {
            aux = Integer.toBinaryString(Integer.parseInt(st.nextToken()));
            while(aux.length() < 8)
                aux = "0".concat(aux);
            redb += aux;
        }
    }
    
    private boolean validaIp(String ip) {
        String ipb = "", aux;
        try {
            st = new StringTokenizer(ip.trim(), ".");
            while(st.hasMoreTokens()) {
                aux = Integer.toBinaryString(Integer.parseInt(st.nextToken()));
                while(aux.length() < 8)
                    aux = "0".concat(aux);
                ipb += aux;
            }
        } catch(NumberFormatException e) {
            return false;
        }
        
        return ipb.substring(0, mask).equals(redb.substring(0, mask));
    }
    
    private String[] datosLocales() throws SocketException {
        boolean flag = true;
        InetAddress inet = null;
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        
        while(e.hasMoreElements() && flag) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            
            while (ee.hasMoreElements() && flag) {
                inet = (InetAddress) ee.nextElement();
                if(validaIp(inet.getHostAddress()))
                    flag = false;
            }
        }
        
        if(flag) {
            JOptionPane.showMessageDialog(null, "No se encontro direccion IPv4", inet.getHostName(), JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
            
        return new String[] { inet.getHostAddress(), obtenerMacLocal(inet), "Equipo local" };
    }
    
    private String obtenerMacLocal(InetAddress inet) throws SocketException {
        NetworkInterface network = NetworkInterface.getByInetAddress(inet);
        byte[] mac = network.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        
        if(mac == null) {
            JOptionPane.showMessageDialog(null, "mac es null", network.getDisplayName().toString(), JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        
        for (int i = 0; i < mac.length; i++)
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
        
        return sb.toString();
    }
    
    public void LimpiaArp() throws IOException {
        String comando = null;
        String so = System.getProperty("os.name");
        Process p;
        
        File f = new File("test.bat");
        PrintWriter pw = new PrintWriter(f);
        pw.println("runas /user:altermannrt@hotmail.com \"netsh interface ip delete arpcache\"");
        //pw.println("exit");
        pw.close();
        
        if(so.equals("Linux")) {
            comando = new String("ip -s -s neigh flush all");
        } else {
            //comando = new String("cmd /c runas /user:altermannrt@hotmail.com \"netsh interface ip delete arpcache\"");
            comando = new String("cmd /c start test.bat");
        }
        
        //String[] c = {"runas", "/user:altermannrt@hotmail.com", "\"netsh interface ip delete arpcache\""};
        
        p = Runtime.getRuntime().exec(comando);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        System.out.println("_" + br.readLine());
        System.out.println("_" + br.readLine());
        
        
    }
    
    public void Arp() {
        try {
            datos.add(datosLocales());
            
            String so = System.getProperty("os.name");
            Process p;
            
            if(so.equals("Linux")) {
                p = Runtime.getRuntime().exec("arp -n");
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                onLinux(br);
            } else {
                p = Runtime.getRuntime().exec("cmd /c arp -a");
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                onOther(br);
            }
        } catch (IOException ex) {
            System.out.println(ex.getCause());
        }
    }
    
    private void onLinux(BufferedReader br) throws IOException {
        char[] ip = new char[15];
        char[] mac = new char[17];
        br.readLine();
        
        String a, b;
        while(br.ready()) {
            br.read(ip);
            br.skip(18);
            br.read(mac);
            a = new String(ip);
            b = new String(mac);
            if(!b.contains("("))
                datos.add(new String[]{a, b.toUpperCase(), ""});
            br.readLine();
        }
    }
    
    private void onOther(BufferedReader br) throws IOException {
        char[] ip = new char[15];
        char[] mac = new char[17];
        
        String salida, a, b;
        if((salida = br.readLine()) != null) {
            br.readLine(); // lee salto de pantalla
            br.readLine(); // lee cabecera de tabla
            int z;
            while(br.ready()) {
                /*z = br.read();
                System.out.println((char)z + " " + z);
                f.setResultado("" + (char)z);*/
                br.skip(2);
                br.read(ip);
                br.skip(7);
                br.read(mac);
                
                br.readLine();
                
                a = new String(ip);
                
                if(!validaIp(a)) continue;
                
                b = new String(mac);
                if(!a.toUpperCase().contains("INT"))
                    datos.add(new String[]{a, b.toUpperCase(), ""});
            }
        } else
            System.out.println("No se encontraron resultados");
    }
    
    public ArrayList getDatos() {
        return datos;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }
}
