/*
 * Copyright (C) 2017 Darth
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package escaner.tools;

import java.io.UnsupportedEncodingException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Darth
 */
public class Adaptador {
    private String id;
    private String nombre;
    private String mac;
    private String ip;
    private int mask;
    private int mtu;
    private boolean supported;
    
    public Adaptador(NetworkInterface ni) throws SocketException {
        id = ni.getName();
        nombre = ni.getDisplayName();
        try {
            mac = new String(ni.getHardwareAddress(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            mac = ni.getHardwareAddress().toString();
        }
        mtu = ni.getMTU();

        List<InterfaceAddress> list = ni.getInterfaceAddresses();
        Iterator<InterfaceAddress> it = list.iterator();
        
        while (it.hasNext()) {
            InterfaceAddress ia = it.next();
            String auxip = ia.getAddress().toString();
            auxip = auxip.substring(1, auxip.length());
            if(validaDireccion(auxip)) {
                ip = auxip;
                mask = ia.getNetworkPrefixLength();
                supported = true;
                break;
            } else {
                ip = "No soportado";
                supported = false;
            }
        }
    }
    
    private boolean validaDireccion(String dir) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return dir.matches(PATTERN);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getMtu() {
        return mtu;
    }

    public void setMtu(int mtu) {
        this.mtu = mtu;
    }
    
    public boolean isSupported() {
        return supported;
    }
    
    @Override
    public String toString() {
    	return this.getId() + " : " + this.getNombre();
    }
}
