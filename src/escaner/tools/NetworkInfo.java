package escaner.tools;

import java.util.StringTokenizer;

public class NetworkInfo {
	private String networkBin;
	private String maskBin;
	private String broadcastBin;
	private String broadcast;
	private String network;
	private int mask;
	private int cantidadHosts;
	private int A, B, C, D;

	public NetworkInfo(String ip, int mask) {
		this.mask = mask;
		this.cantidadHosts = (int) (Math.pow(2, (32 - mask)) - 2);
		obtenerDatosBinarios(ip);

		StringTokenizer st = new StringTokenizer(networkBin, ".");
		A = Integer.parseInt(st.nextToken(), 2);
		B = Integer.parseInt(st.nextToken(), 2);
		C = Integer.parseInt(st.nextToken(), 2);
		D = Integer.parseInt(st.nextToken(), 2);

		network = A + "." + B + "." + C + "." + D;
	}

	public StringBuffer siguienteIp() {
		StringBuffer ip = new StringBuffer();
		if (D < 255) {
			D++;
		} else if (C < 255) {
			C++;
			D = 0;
		} else if (B < 255) {
			B++;
			C = 0;
		}
		ip.append(A).append(".");
		ip.append(B).append(".");
		ip.append(C).append(".");
		ip.append(D);
		return ip;
	}

	private void obtenerDatosBinarios(String ip) {
		String ipbin = ipToBin(ip);
		String networkBin = "";
		String broadcastBin = "";
		String maskBin = "";

		for (int i = 1; i < ipbin.length() + 1; i++) {
			if (i <= mask) {
				networkBin += ipbin.charAt(i - 1);
				broadcastBin += ipbin.charAt(i - 1);
				maskBin += 1;
			} else {
				networkBin += 0;
				broadcastBin += 1;
				maskBin += 0;
			}

			if (i % 8 == 0 && i < ipbin.length()) {
				networkBin += ".";
				broadcastBin += ".";
				maskBin += ".";
			}
		}
		this.networkBin = networkBin;
		this.broadcastBin = broadcastBin;
		this.maskBin = maskBin;
	}

	private String ipToBin(String ip) {
		StringTokenizer st = new StringTokenizer(ip, ".");
		String ipBin = "";
		String segmentoBin;
		int segmento;
		while (st.hasMoreTokens()) {
			segmento = Integer.parseInt(st.nextToken());
			segmentoBin = Integer.toBinaryString(segmento);
			while (segmentoBin.length() < 8) {
				segmentoBin = "0" + segmentoBin;
			}
			ipBin += segmentoBin;
		}

		return ipBin;
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

	public String getMaskBin() {
		return maskBin;
	}

	public String getBroadcastBin() {
		return broadcastBin;
	}
}
