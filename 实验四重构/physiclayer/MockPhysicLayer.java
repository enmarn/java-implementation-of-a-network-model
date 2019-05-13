package physiclayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import log.Log;

public class MockPhysicLayer implements IPhysicLayer{

	private static final int size = 256;
	private int port1;
	private int port2;
	private byte[] recv_buf;
	private DatagramPacket data;
	private DatagramSocket server1;
	private DatagramSocket server2;
	private InetAddress inetAddress;
	
	public MockPhysicLayer(int port) {
		// TODO Auto-generated constructor stub
		try {
			this.port1 = port;
			if(port < 9000){
				server1 = new DatagramSocket(port1);
				server2 = new DatagramSocket(port1-1);
				this.port2 = port1-1;
			} else {
				server1 = new DatagramSocket(port1);
				server2 = new DatagramSocket(port1+1);
				this.port2 = port1+1;
			}
			server1.setSoTimeout(10);
			server2.setSoTimeout(0);
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
	public void toPhysic(byte[] bits) {
		// TODO Auto-generated method stub
	    try {
		    DatagramPacket packet = new DatagramPacket(bits,
		    		bits.length, inetAddress, 18000-port2);
		    Log.Physic.println("物理层——发送");
			server1.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public byte[] fromPhysic(){
		// TODO Auto-generated method stub
		try {
			server2.receive(data);
			Log.Physic.println("物理层——接收");
			return data.getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
