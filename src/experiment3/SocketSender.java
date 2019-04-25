package experiment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.xml.ws.handler.MessageContext;

import experiment2.TransmissionUtil;
import utils.BitMap;

public class SocketSender {
	private static final int PORT=8888;
	private static DatagramSocket server;
	private static final int size = 1024;
	private static byte[] recv_buf = new byte[size];
	private static DatagramPacket data = new DatagramPacket(recv_buf, size);
	public static void main(String[] args) throws Exception {
	    server = new DatagramSocket(PORT);
	    server.setSoTimeout(1000);
	    
	     /*第二步 实例化用于发送的DatagramPacket和用于接收的DatagramPacket*/
	     InetAddress inetAddress = InetAddress.getLocalHost();
	     
         
	    String info = "0000";
	    BitMap bitMap = new BitMap();
	    bitMap.valueOf(info);
	    bitMap = TransmissionUtil.toTransparentBitMap(bitMap);
	    System.out.println(bitMap.toByteArray());
	    DatagramPacket msg = new DatagramPacket(bitMap.toByteArray(),
	    		bitMap.toByteArray().length, inetAddress, 8889);
	    send(msg);
	    server.close();
	}

	public static void send(DatagramPacket msg) throws IOException {
		while (true) {
			server.send(msg);
			server.receive(data);
//			System.out.println(data.getAddress().getHostAddress());
			byte[] response = data.getData();
			if(response[0]==DataLinkLayer.ACCEPT[0]) return;
		}
	}
	public static byte[] receive(DatagramPacket data) throws IOException {
	    server.receive(data);
	    return data.getData();
	}
}
