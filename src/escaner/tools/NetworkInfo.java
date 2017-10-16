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

import java.util.StringTokenizer;

/**
 *
 * @author Darth
 */
public class NetworkInfo {
    private String networkBin;
    private String maskBIn;
    private String broadcastBin;
    
    private String network;
    private int mask;
    private String broadcast;
    
    private int cantidadHosts;
    private int A, B, C, D;
    
    public NetworkInfo(String ip, int mask) {
        this.mask = mask;
        this.cantidadHosts = (int) (Math.pow(2, (32 - mask)) - 2);
        SacaDatosBinarios(ip);
        
        StringTokenizer st = new StringTokenizer(networkBin, ".");
        A = Integer.parseInt(st.nextToken(), 2);
        B = Integer.parseInt(st.nextToken(), 2);
        C = Integer.parseInt(st.nextToken(), 2);
        D = Integer.parseInt(st.nextToken(), 2);
        
        network = A+"."+B+"."+C+"."+D;
    }
    
    public StringBuffer sigIp() {
        StringBuffer ip = new StringBuffer();
        if(D < 255) {
            D++;
        } else if(C < 255) {
            C++;
            D = 0;
        } else if(B < 255) {
            B++;
            C = 0;
        }
        ip.append(A).append(".");
        ip.append(B).append(".");
        ip.append(C).append(".");
        ip.append(D);
        return ip;
    }
    
    private void SacaDatosBinarios(String ip) {
        String ipbin = IpToBin(ip);
        
        String redbin = "";
        String broadcastbin = "";
        String maskbin = "";
        
        for(int i = 1; i < ipbin.length()+1; i++) {
            if(i <= mask) {
                redbin += ipbin.charAt(i-1);
                broadcastbin += ipbin.charAt(i-1);
                maskbin += 1;
            } else {
                redbin += 0;
                broadcastbin += 1;
                maskbin += 0;
            }
            
            if(i % 8 == 0 && i < ipbin.length()) {
                redbin += ".";
                broadcastbin += ".";
                maskbin += ".";
            }
        }
        networkBin = redbin;
        broadcastBin = broadcastbin;
        maskBIn = maskbin;
    }
    
    private String IpToBin(String ip) {
        StringTokenizer st = new StringTokenizer(ip, ".");
        int aux;
        String ipbin = "";
        String auxbin;
        while(st.hasMoreTokens()) {
            aux = Integer.parseInt(st.nextToken());
            auxbin = Integer.toBinaryString(aux);
            while(auxbin.length() < 8) {
                auxbin = "0" + auxbin;
            }
            ipbin += auxbin;
        }
        
        return ipbin;
    }

    public String getNetwork() {
        return network;
    }

    public int getMask() {
        return mask;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public int getCantidadHosts() {
        return cantidadHosts;
    }
}
