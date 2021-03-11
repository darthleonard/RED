package escaner.services;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import escaner.tools.Adaptador;

public class NetworkInterfacesService {
	public ArrayList<Adaptador> GetInterfaces() throws SocketException {
		ArrayList<Adaptador> interfaces = new ArrayList<>();
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        
    	while (en.hasMoreElements()) {
            NetworkInterface networkAdapter = en.nextElement();
            if(networkAdapter.isUp() && !networkAdapter.isLoopback()) {
                interfaces.add(new Adaptador(networkAdapter));
            }
    	}
    	return interfaces;
	}
}
