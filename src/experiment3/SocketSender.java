package experiment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import experiment2.TransmissionUtil;

public class SocketSender {
	private static final int PORT=8888;
	private static final int size = 256;
	
	private byte[] recv_buf;
	private DatagramPacket data;
	private DatagramSocket server; 
	
	public SocketSender() throws SocketException {
		server = new DatagramSocket(PORT);
	    server.setSoTimeout(1000);
		recv_buf = new byte[size];
		data = new DatagramPacket(recv_buf, size);
	}
	
	public static void main(String[] args) throws Exception {
	    
	     /*第二步 实例化用于发送的DatagramPacket和用于接收的DatagramPacket*/
	     InetAddress inetAddress = InetAddress.getLocalHost();
	     SocketSender sender = new SocketSender();
         
	    String src = "0000";
	    String transStr = TransmissionUtil.toParentTransparentBitString(src);
	    byte[] transByte = transStr.getBytes();

	    DatagramPacket msg = new DatagramPacket(transByte,
	    		transByte.length, inetAddress, 8889);
	    sender.send(msg);
	    sender.close();
	}

	public void send(DatagramPacket msg) throws IOException {
		int attemptNum = 5;
		while (attemptNum>0) {
			server.send(msg);
		    server.receive(data);
//			System.out.println(data.getAddress().getHostAddress());
			String response = new String(data.getData());
			response = TransmissionUtil.toOriginalBitString(response);
			if(response.equals(DataLinkLayer.ACK)) return;
			attemptNum -- ;
		}
	}
	
	public void close() {
		server.close();
	}
}
