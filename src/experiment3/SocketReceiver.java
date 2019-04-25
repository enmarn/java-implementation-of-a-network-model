package experiment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import experiment2.TransmissionUtil;

public class SocketReceiver {
	private static final int PORT=8889;
	private static final int size = 256;
	
	private byte[] recv_buf;
	private DatagramPacket data;
	private DatagramSocket server; 
	public SocketReceiver() throws SocketException {
		server = new DatagramSocket(PORT);
		recv_buf = new byte[size];
		data = new DatagramPacket(recv_buf, size);
	}
	
	public static void main(String[] args) throws Exception {
		SocketReceiver receiver = new SocketReceiver();
	    System.out.println("接收端开始监听");
	    while (true) {
	    	receiver.receive();	
		}
	}
	
	public void send(DatagramPacket data) throws IOException {
	    server.send(data);
	}
	
	public String receive() throws IOException {
		//接收到比特流
	    server.receive(data);
	    //线路上传输的比特流
	    String info = new String(data.getData());
	    //去透明化解码
	    info = TransmissionUtil.toOriginalBitString(info);
	    System.out.println(info);
	    /*
	     * 处理
	     */
	    //回复
	    //透明化编码
	    String res = TransmissionUtil.toParentTransparentBitString(DataLinkLayer.ACK);
	    
	    byte[] stat = res.getBytes();
	    DatagramPacket response = new DatagramPacket(stat,stat.length,data.getAddress(),data.getPort());
	    server.send(response);
	    return info;
	}
}
