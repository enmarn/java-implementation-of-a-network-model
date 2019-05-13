package experiment4;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import experiment4.interfaces.IPysicalLayer;

public class PysicalLayer implements IPysicalLayer {
	private static final int size = 256;
	private int port;
	private byte[] recv_buf;
	private DatagramPacket data;
	private DatagramSocket server;
	private InetAddress inetAddress;
	/**
	 * 假设两主机的监听端口号关于9000对称
	 * @param port
	 */
	public PysicalLayer(int port) {
		// TODO Auto-generated constructor stub
		try {
			this.port = port;
			server = new DatagramSocket(port);
			recv_buf = new byte[size];
			data = new DatagramPacket(recv_buf, size);
			inetAddress = InetAddress.getLocalHost();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void send(String bitstream) {
		// TODO Auto-generated method stub
	    try {
		    byte[] transByte = bitstream.getBytes();
		    DatagramPacket packet = new DatagramPacket(transByte,
		    		transByte.length, inetAddress, 18000-port);
		    Log1.err.println("物理层——向物理线路发送——"+bitstream);
			server.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String listen() throws IOException {
		// TODO Auto-generated method stub
		server.receive(data);
		String bitstream = new String(data.getData());
		Log1.err.println("物理层——从物理线路接收——"+bitstream);
		return bitstream;
	}
}
